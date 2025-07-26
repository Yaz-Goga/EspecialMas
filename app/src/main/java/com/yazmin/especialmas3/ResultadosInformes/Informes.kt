package com.yazmin.especialmas3.ResultadosInformes

import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yazmin.especialmas3.R
import com.yazmin.especialmas3.adapter.AdaptadorInforme
import com.yazmin.especialmas3.database.EspecialMasDataBase
import com.yazmin.especialmas3.database.EvaluacionDao
import com.yazmin.especialmas3.model.Estudiante
import com.yazmin.especialmas3.model.Evaluacion
import com.yazmin.especialmas3.model.ItemsInforme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class Informes : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var spinnerEstudianteInforme: Spinner
    private lateinit var spinnerTipoEvalInforme: Spinner
    private lateinit var spinnerEstadoFiltro: Spinner

    private lateinit var evaluacionDao: EvaluacionDao
    private lateinit var estudiantes: List<Estudiante>
    private lateinit var listaOriginal: List<ItemsInforme>
    private lateinit var adaptador: AdaptadorInforme

    private var tipoEvalSeleccionado = "Inicial"
    private var estudianteSeleccionado: Estudiante? = null
    private var estadoSeleccionado = "Todos"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_informes)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.RecViewInformes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        spinnerTipoEvalInforme = findViewById(R.id.spinnerTipoEvalInforme)
        spinnerEstudianteInforme = findViewById(R.id.spinnerEstudianteInforme)
        spinnerEstadoFiltro = findViewById(R.id.spinnerEstadoFiltro)

        evaluacionDao = EspecialMasDataBase.getDatabase(this).evaluacionDao()

        val docenteId = intent.getIntExtra("docenteId", -1)
        if (docenteId == -1) {
            finish()
            return
        }

        val tipos = listOf("Inicial", "Seguimiento", "Final")
        val adapterTipos = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipos)
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoEvalInforme.adapter = adapterTipos

        spinnerTipoEvalInforme.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                tipoEvalSeleccionado = tipos[position]
                cargarEstudiantesConEvaluaciones(docenteId)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.opciones_estado,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerEstadoFiltro.adapter = adapter
        }

        spinnerEstadoFiltro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                estadoSeleccionado = parent?.getItemAtPosition(position).toString()
                actualizarInforme()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        cargarEstudiantesConEvaluaciones(docenteId)

        findViewById<FloatingActionButton>(R.id.btnPDF).setOnClickListener {
            estudianteSeleccionado?.let { estudiante ->
                lifecycleScope.launch {
                    val evaluaciones = evaluacionDao.obtenerEvaluacionesPorMomento(estudiante.id, tipoEvalSeleccionado)
                    val agrupadas = evaluaciones.groupBy { it.categoria }
                    val graficos = generarGraficosComoBitmaps(estudiante.id, tipoEvalSeleccionado)
                    generarInformePDF(estudiante, agrupadas, graficos)
                }
            }
        }
    }

    private suspend fun generarGraficosComoBitmaps(estudianteId: Int, tipoEvaluacion: String): List<Pair<String, Bitmap>> {
        val db = EspecialMasDataBase.getDatabase(this@Informes)
        val resultados = db.itemsPorCategoriaDao().obtenerItemsPorCategoria(estudianteId, tipoEvaluacion)

        val categorias = resultados.map { it.categoria }.distinct()
        val bitmaps = mutableListOf<Pair<String, Bitmap>>()

        for (categoria in categorias) {
            val itemsCategoria = resultados.filter { it.categoria == categoria }

            val conteoInd = itemsCategoria.filter { it.estado.trim().lowercase() == "independiente" }.sumOf { it.cantidad }
            val conteoProc = itemsCategoria.filter { it.estado.trim().lowercase() == "en proceso" }.sumOf { it.cantidad }
            val conteoDep = itemsCategoria.filter { it.estado.trim().lowercase() == "dependiente" }.sumOf { it.cantidad }

            val entries = listOf(
                BarEntry(0f, conteoInd.toFloat()),
                BarEntry(1f, conteoProc.toFloat()),
                BarEntry(2f, conteoDep.toFloat())
            )

            val dataSet = BarDataSet(entries, "Estados").apply {
                setColors(Color.GREEN, Color.YELLOW, Color.RED)
                valueTextSize = 12f
            }

            val chart = BarChart(this@Informes).apply {
                layoutParams = RecyclerView.LayoutParams(600, 400)
                data = BarData(dataSet)
                setFitBars(true)
                description.isEnabled = false
                legend.isEnabled = false
                xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(listOf("Independiente", "En proceso", "Dependiente"))
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    setDrawGridLines(false)
                    textSize = 8f
                }
                axisLeft.apply {
                    axisMinimum = 0f
                    granularity = 1f
                    setDrawGridLines(true)
                }
                axisRight.isEnabled = false
                measure(View.MeasureSpec.makeMeasureSpec(600, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(400, View.MeasureSpec.EXACTLY))
                layout(0, 0, 600, 400)
            }

            val bitmap = createBitmap(600, 400)
            val canvas = Canvas(bitmap)
            chart.draw(canvas)
            bitmaps.add(Pair(categoria, bitmap))
        }

        return bitmaps
    }

    private suspend fun generarInformePDF(
        estudiante: Estudiante,
        evaluaciones: Map<String, List<Evaluacion>>,
        graficos: List<Pair<String, Bitmap>>
    ) {
        val pdfDocument = PdfDocument()

        val paint = Paint().apply { textSize = 14f; typeface = Typeface.DEFAULT }
        val titlePaint = Paint().apply { textSize = 16f; typeface = Typeface.DEFAULT_BOLD; color = Color.BLACK }
        val categoriaPaint = Paint().apply { textSize = 16f; typeface = Typeface.DEFAULT_BOLD; color = Color.rgb(25, 118, 210) }
        val estadoPaint = Paint().apply { textSize = 14f; typeface = Typeface.MONOSPACE }
        val obsPaint = Paint().apply { textSize = 12f; typeface = Typeface.SANS_SERIF; color = Color.DKGRAY }

        var y = 100
        var pageNumber = 1

        fun nuevaPagina(): PdfDocument.Page {
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, pageNumber).create()
            val page = pdfDocument.startPage(pageInfo)
            y = 100
            return page
        }

        var page = nuevaPagina()
        val canvas = page.canvas

        canvas.drawText("INFORME DE PROTOCOLO DE HABILIDADES ADAPTATIVAS", 40f, 60f, titlePaint)
        canvas.drawText("Nombre: ${estudiante.nombre} ${estudiante.apellidos}", 40f, y.toFloat(), paint); y += 20
        canvas.drawText("Fecha de nacimiento: ${estudiante.fechaNacimiento}", 40f, y.toFloat(), paint); y += 20
        canvas.drawText("Diagnóstico: ${estudiante.diagnostico}", 40f, y.toFloat(), paint); y += 20
        canvas.drawText("Tipo de Evaluación: $tipoEvalSeleccionado", 40f, y.toFloat(), paint); y += 30

        for ((categoria, lista) in evaluaciones) {
            if (y > 780) { pdfDocument.finishPage(page); pageNumber++; page = nuevaPagina() }
            page.canvas.drawText("Categoría: ${categoria.uppercase()}", 40f, y.toFloat(), categoriaPaint); y += 24
            for (eval in lista) {
                if (y > 780) { pdfDocument.finishPage(page); pageNumber++; page = nuevaPagina() }
                page.canvas.drawText("• ${eval.item}", 60f, y.toFloat(), paint); y += 18
                estadoPaint.color = when (eval.estado.lowercase()) {
                    "independiente" -> "#388E3C".toColorInt()
                    "en proceso" -> "#FBC02D".toColorInt()
                    "dependiente" -> "#D32F2F".toColorInt()
                    else -> Color.BLACK
                }
                page.canvas.drawText("Estado: ${eval.estado}", 80f, y.toFloat(), estadoPaint); y += 18
                if (!eval.observaciones.isNullOrBlank()) {
                    page.canvas.drawText("Obs: ${eval.observaciones}", 80f, y.toFloat(), obsPaint); y += 18
                }
                y += 8
            }
            y += 20
        }

        pdfDocument.finishPage(page)

        val anchoGrafica = 250
        val altoGrafica = 200
        val margenIzq = 40f
        val margenSup = 80f
        val espacioH = 30f
        val espacioV = 40f
        val titleGrafPaint = Paint().apply { textSize = 14f; typeface = Typeface.DEFAULT_BOLD; color = Color.BLACK }

        for ((i, pair) in graficos.withIndex()) {
            val (categoria, graf) = pair
            if (i % 4 == 0) {
                pageNumber++
                val pageInfo = PdfDocument.PageInfo.Builder(595, 842, pageNumber).create()
                page = pdfDocument.startPage(pageInfo)
            }
            val grafCanvas = page.canvas
            val resized = graf.scale(anchoGrafica, altoGrafica)
            val columna = i % 2
            val fila = (i % 4) / 2
            val x = margenIzq + columna * (anchoGrafica + espacioH)
            val yGraf = margenSup + fila * (altoGrafica + espacioV + 20f)
            grafCanvas.drawText(categoria.uppercase(), x, yGraf - 10f, titleGrafPaint)
            grafCanvas.drawBitmap(resized, x, yGraf, null)
            if ((i + 1) % 4 == 0 || i == graficos.size - 1) {
                pdfDocument.finishPage(page)
            }
        }

        val fileName = "Informe_${estudiante.nombre}_${System.currentTimeMillis()}.pdf"
        val file = File(getExternalFilesDir(null), fileName)

        try {
            withContext(Dispatchers.IO) { pdfDocument.writeTo(FileOutputStream(file)) }
            withContext(Dispatchers.Main) {
                Toast.makeText(this@Informes, "PDF guardado: ${file.absolutePath}", Toast.LENGTH_LONG).show()
                val uri: Uri = FileProvider.getUriForFile(this@Informes, "${applicationContext.packageName}.provider", file)
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/pdf")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY)
                }
                if (intent.resolveActivity(packageManager) != null) startActivity(intent)
                else Toast.makeText(this@Informes, "No hay visor de PDF instalado", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@Informes, "Error al generar PDF: ${e.message}", Toast.LENGTH_LONG).show()
            }
            e.printStackTrace()
        } finally {
            pdfDocument.close()
        }
    }

    private fun cargarEstudiantesConEvaluaciones(docenteId: Int) {
        lifecycleScope.launch {
            val idsConEvaluacion = evaluacionDao.obtenerIdsEstudiantesConEvaluaciones(tipoEvalSeleccionado)
            val estudiantesCompletos = EspecialMasDataBase.getDatabase(applicationContext).estudianteDao().mostrarEstudiantesPorDocente(docenteId)
            estudiantes = estudiantesCompletos.filter { idsConEvaluacion.contains(it.id) }

            val nombres = estudiantes.map { "${it.nombre} ${it.apellidos}" }
            val adapterEstudiantes = ArrayAdapter(this@Informes, android.R.layout.simple_spinner_item, nombres)
            adapterEstudiantes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerEstudianteInforme.adapter = adapterEstudiantes

            spinnerEstudianteInforme.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    estudianteSeleccionado = estudiantes[position]
                    actualizarInforme()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

            if (estudiantes.isNotEmpty()) {
                estudianteSeleccionado = estudiantes[0]
                actualizarInforme()
            } else {
                adaptador = AdaptadorInforme(emptyList())
                recyclerView.adapter = adaptador
            }
        }
    }

    private fun actualizarInforme() {
        val estudiante = estudianteSeleccionado ?: return
        lifecycleScope.launch {
            val listaInforme = evaluacionDao.obtenerInformePorEstudianteyTipo(estudiante.id, tipoEvalSeleccionado)
            listaOriginal = listaInforme

            val listaFiltrada = when (estadoSeleccionado.lowercase()) {
                "independiente" -> listaOriginal.filter { it.estado?.lowercase() == "independiente" }
                "en proceso" -> listaOriginal.filter { it.estado?.lowercase() == "en proceso" }
                "dependiente" -> listaOriginal.filter { it.estado?.lowercase() == "dependiente" }
                else -> listaOriginal
            }

            adaptador = AdaptadorInforme(listaFiltrada)
            recyclerView.adapter = adaptador
        }
    }
}
