package com.yazmin.especialmas3.ResultadosInformes

import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.yazmin.especialmas3.R
import com.yazmin.especialmas3.database.EspecialMasDataBase
import kotlinx.coroutines.launch

class GraficosResultados : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_graficos_resultados)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val layoutGraficas = findViewById<LinearLayout>(R.id.layoutGraficas)
        val estudianteId = intent.getIntExtra("ESTUDIANTE_ID", -1)
        val tipoEvaluacion = intent.getStringExtra("TIPO_EVALUACION") ?: "Inicial"

        if (estudianteId == -1) {
            finish()
            return
        }

        cargarGraficasPorCategoria(estudianteId, tipoEvaluacion, layoutGraficas)
    }

    private fun cargarGraficasPorCategoria(estudianteId: Int, tipoEvaluacion: String, layout: LinearLayout) {
        lifecycleScope.launch {
            val db = EspecialMasDataBase.getDatabase(this@GraficosResultados)
            val resultados = db.itemsPorCategoriaDao().obtenerItemsPorCategoria(estudianteId, tipoEvaluacion)

            val categorias = resultados.map { it.categoria }.distinct()
            layout.removeAllViews()

            for (categoria in categorias) {
                val itemsCategoria = resultados.filter { it.categoria == categoria }

                val conteoIndependiente = itemsCategoria
                    .filter { it.estado.trim().lowercase() == "independiente" }
                    .sumOf { it.cantidad }

                val conteoEnProceso = itemsCategoria
                    .filter { it.estado.trim().lowercase() == "en proceso" }
                    .sumOf { it.cantidad }

                val conteoDependiente = itemsCategoria
                    .filter { it.estado.trim().lowercase() == "dependiente" }
                    .sumOf { it.cantidad }

                val entries = listOf(
                    BarEntry(0f, conteoIndependiente.toFloat()),
                    BarEntry(1f, conteoEnProceso.toFloat()),
                    BarEntry(2f, conteoDependiente.toFloat())
                )

                val dataSet = BarDataSet(entries, "Estados").apply {
                    setColors(Color.GREEN, Color.YELLOW, Color.RED)
                    valueTextSize = 14f
                }

                val barData = BarData(dataSet)
                barData.barWidth = 0.5f

                val chart = BarChart(this@GraficosResultados)
                chart.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    500
                ).apply {
                    setMargins(0, 32, 0, 32)
                }

                chart.data = barData
                chart.setFitBars(true)
                chart.description.isEnabled = false
                chart.legend.isEnabled = false

                val xAxis = chart.xAxis
                xAxis.valueFormatter = IndexAxisValueFormatter(listOf("Independiente", "En proceso", "Dependiente"))
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.granularity = 1f
                xAxis.setDrawGridLines(false)
                xAxis.textSize = 14f

                chart.axisLeft.axisMinimum = 0f
                chart.axisLeft.granularity = 1f
                chart.axisLeft.setDrawGridLines(true)
                chart.axisLeft.valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return value.toInt().toString()
                    }
                }
                chart.axisRight.isEnabled = false
                chart.invalidate()

                val titulo = TextView(this@GraficosResultados).apply {
                    text = categoria
                    textSize = 18f
                    setTextColor(Color.BLACK)
                    setPadding(0, 16, 0, 8)
                }

                layout.addView(titulo)
                layout.addView(chart)
            }
        }
    }
}
