package com.yazmin.especialmas3.evaluacionesApp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yazmin.especialmas3.R
import com.yazmin.especialmas3.adapter.AdaptadorCategoriaHA
import com.yazmin.especialmas3.model.CategoriaHA
import com.yazmin.especialmas3.model.Evaluacion
import com.yazmin.especialmas3.model.ItemsPruebaHA
import com.yazmin.especialmas3.repositorio.EvaluacionReposit
import kotlinx.coroutines.launch

//Prueba con categorias, items, estados y observaciones.
class EvaluarPrueba : AppCompatActivity() {
    private lateinit var adaptadorCategoria: AdaptadorCategoriaHA //Se declara el adaptador de categ. e items...

    //Categorias e items de cada una en la prueba.
    private val categoriasBase = listOf(
        CategoriaHA(
            "AUTOCUIDADO", listOf(
                ItemsPruebaHA("Come con la mano"),
                ItemsPruebaHA("Bebe con vaso/taza"),
                ItemsPruebaHA("Utiliza cubiertos"),
                ItemsPruebaHA("Pone y retira la mesa para sí"),
                ItemsPruebaHA("Sacude migajas / limpia líquidos comida derramada"),
                ItemsPruebaHA("Saca alimentos de la despensa o del refrigerador"),
                ItemsPruebaHA("Prepara alimentos sencillos para sí mismo FRIOS"),
                ItemsPruebaHA("Prepara alimentos sencillos para sí mismo CALIENTES"),
                ItemsPruebaHA("QUITA ropa parte inferior"),
                ItemsPruebaHA("QUITA ropa parte superior"),
                ItemsPruebaHA("PONE ropa parte inferior"),
                ItemsPruebaHA("PONE ropa parte superior"),
                ItemsPruebaHA("Abotona/Acordona/Cierres/Broches"),
                ItemsPruebaHA("Repara (Vestido)"),
                ItemsPruebaHA("Distingue ropa limipa/sucia"),
                ItemsPruebaHA("Distingue ropa según clima /Elige"),
                ItemsPruebaHA("Avisa si ensució sus pañales"),
                ItemsPruebaHA("Controla esfínteres/babeo"),
                ItemsPruebaHA("Utiliza correctamente WC"),
                ItemsPruebaHA("Realiza higiene personal"),
                ItemsPruebaHA("Se baña / lava su pelo"),
                ItemsPruebaHA("Se maquilla/rasura"),
                ItemsPruebaHA("Conoce los cuidados específicos de distintas partes de su cuerpo"),
                ItemsPruebaHA("Conoce-maneja su sexualidad"),
                ItemsPruebaHA("Usa preservativos")
            )
        ),
        CategoriaHA(
            "VIDA EN EL HOGAR", listOf(
                ItemsPruebaHA("Reconoce ropa limpia y sucia"),
                ItemsPruebaHA("Lava a mano / a máquina"),
                ItemsPruebaHA("Tiende/ Plancha/ Guarda la ropa"),
                ItemsPruebaHA("Repara la ropa"),
                ItemsPruebaHA("Elige su ropa diaria (según el clima)"),
                ItemsPruebaHA("Compra su propia ropa en negocios o por catálogo"),
                ItemsPruebaHA("Guarda alimentos en alacena, refrigerador, estantes"),
                ItemsPruebaHA("Realiza recetas sencillas (sigue instrucciones escritas o gráficas"),
                ItemsPruebaHA("Usa distintos modos de cocción"),
                ItemsPruebaHA("Reconoce el buen estado de los alimentos"),
                ItemsPruebaHA("Puede balancear su dieta"),
                ItemsPruebaHA("Realiza lista de faltantes/compras (alimento)"),
                ItemsPruebaHA("Barre/trapea/sacude"),
                ItemsPruebaHA("Lava ventanas"),
                ItemsPruebaHA("Lava y ordena trastes"),
                ItemsPruebaHA("Limpia la cocina/refrigerador"),
                ItemsPruebaHA("Destiende/tiende la cama"),
                ItemsPruebaHA("Lava artefactos del baño"),
                ItemsPruebaHA("Utiliza el teléfono"),
                ItemsPruebaHA("Utiliza artefactos eléctricos"),
                ItemsPruebaHA("Reconoce / soluciona situaciones de riesgo"),
                ItemsPruebaHA("Cambia focos / destapa drenajes"),
                ItemsPruebaHA("Realiza pasajes (silla de ruedas) cama-baño")
            )
        ),
        CategoriaHA(
            "AUTODIRECCIÓN", listOf(
                ItemsPruebaHA("Tiene en su casa alguna responsabilidad"),
                ItemsPruebaHA("Sabe cuáles son sus actividades diarias"),
                ItemsPruebaHA("Conoce su dirección particular"),
                ItemsPruebaHA("Reconoce horarios y fechas"),
                ItemsPruebaHA("Conoce el uso del reloj (tipo de reloj)"),
                ItemsPruebaHA("Regula tiempos y puntualidad"),
                ItemsPruebaHA("Avisa en casos de retrasos"),
                ItemsPruebaHA("Conoce su agenda semanal"),
                ItemsPruebaHA("Administra su medicación"),
                ItemsPruebaHA("Maneja calendario"),
                ItemsPruebaHA("Conoce-recuerda fechas familiares"),
                ItemsPruebaHA("Realiza recorridos habituales"),
                ItemsPruebaHA("Planifica sus actividades de ocio"),
                ItemsPruebaHA("Organiza sus reuniones o salidas")
            )
        ),
        CategoriaHA(
            "USO DE RECURSOS DE LA COMUNIDAD", listOf(
                ItemsPruebaHA("Reconoce trayectos habituales"),
                ItemsPruebaHA("Se traslada a lugares cercanos/lejanos"),
                ItemsPruebaHA("Utiliza medios de transporte"),
                ItemsPruebaHA("Conoce trayectos alternativos a los habituales"),
                ItemsPruebaHA("Maneja dinero"),
                ItemsPruebaHA("Realiza compras(por recados, discrimina tipo de negocio/autoservicio)"),
                ItemsPruebaHA("Reconoce centro de salud más cercanos"),
                ItemsPruebaHA("Conoce recursos y servicios de su comunidad: instituciones, organizaciones, centros, públicos o privados, etc."),
                ItemsPruebaHA("Sabe solicitar turnos médicos"),
                ItemsPruebaHA("Sabe marcar teléfonos de emergencia"),
                ItemsPruebaHA("Identifica-previene situaciones de riesgo"),
                ItemsPruebaHA("Reconoce símbolos sociales: peligro, alto, semáforos, líneas del metro, pasos peatonales, sirenas, policía"),
                ItemsPruebaHA("Participa en actividades de su comunidad")
            )
        ),
        CategoriaHA(
            "ACADÉMICAS FUNCIONALES", listOf(
                ItemsPruebaHA("Discrimina colores (emparejamiento, reconoce, nombra)"),
                ItemsPruebaHA("Discrimina formas (con material concreto/ en papel/ mentalmente)"),
                ItemsPruebaHA("Discrimina tamaños (cant. piezas"),
                ItemsPruebaHA("Respeta espacios gráficos"),
                ItemsPruebaHA("Presenta nociones temporoespaciales"),
                ItemsPruebaHA("Nociones de lateralidad en sí / otros"),
                ItemsPruebaHA("Reconoce formas figuras"),
                ItemsPruebaHA("Presenta conservación de cantidad"),
                ItemsPruebaHA("Clasifica"),
                ItemsPruebaHA("Presenta nociones prenuméricas"),
                ItemsPruebaHA("Reconoce su esquema corporal"),
                ItemsPruebaHA("Reconoce vocales may-min"),
                ItemsPruebaHA("Reconoce consonantes may-min"),
                ItemsPruebaHA("Reconoce su nombre escrito"),
                ItemsPruebaHA("Escribe su nombre"),
                ItemsPruebaHA("Estructura enunciados"),
                ItemsPruebaHA("Lee sílabas - palabras"),
                ItemsPruebaHA("Comprende frases - textos"),
                ItemsPruebaHA("Escribe al dictado"),
                ItemsPruebaHA("Reconoce números ¿hasta?"),
                ItemsPruebaHA("Reconoce numeros anteriores y posteriores"),
                ItemsPruebaHA("Concepto de cantidad numérica"),
                ItemsPruebaHA("Secuencia numérica escrita"),
                ItemsPruebaHA("Resuelve problemas sencillos (Suma y resta especificar si con material concreto, papel o mentalmente)"),
                ItemsPruebaHA("Usa calculadora"),
                ItemsPruebaHA("Usa planos, mapas"),
            )
        ),
        CategoriaHA(
            "SALUD Y SEGURIDAD", listOf(
                ItemsPruebaHA("Presenta reacciones instintivas ante el peligro"),
                ItemsPruebaHA("Reconoce sensaciones de malestar"),
                ItemsPruebaHA("Señala si se siente mal"),
                ItemsPruebaHA("Identifica situaciones de peligro y las evita"),
                ItemsPruebaHA("Toma medicamentos en los horarios indicados"),
                ItemsPruebaHA("Templa adecuadamente el agua para el baño"),
                ItemsPruebaHA("Denuncia las agresiones que sufre"),
                ItemsPruebaHA("Dice No a propuestas inconvenientes"),
                ItemsPruebaHA("Reconoce alimentos en mal estado"),
                ItemsPruebaHA("Anticipa situaciones de riesto (en qué entornos)"),
                ItemsPruebaHA("Identifica y previene situaciones de riesgo en distintos entornos"),
                ItemsPruebaHA("Lee fechas de vencimiento"),
            )
        ),
        CategoriaHA(
            "COMUNICACIÓN", listOf(
                ItemsPruebaHA("Responde a estímulos Visuales, Auditivos, Táctiles"),
                ItemsPruebaHA("Presenta patrón básico de Respuesta"),
                ItemsPruebaHA("Expresa Placer-Displacer"),
                ItemsPruebaHA("Reconoce y expresa emociones"),
                ItemsPruebaHA("Maneja código elemental de comunicación"),
                ItemsPruebaHA("Se expresa por: facies/ mov. ocular/ gesticulación/ señala/ habla/ tablero de comunicación/ LSM/ Leng. escrito/ Braille/ Exp. corporal "),
                ItemsPruebaHA("Estructura enunciados"),
                ItemsPruebaHA("Reconoce y respeta códigos sociales"),
                ItemsPruebaHA("Reconoce bromas o absurdos"),
                ItemsPruebaHA("Mantiene comunicación en distintos entornos"),
                ItemsPruebaHA("Mantiene comunicación por distintos medios"),
            )
        ),
        CategoriaHA(
            "OCIO Y TIEMPO LIBRE", listOf(
                ItemsPruebaHA("Sigue objetos, se conecta al entorno"),
                ItemsPruebaHA("Presenta destrezas motrices generales"),
                ItemsPruebaHA("Arrastra-empuja objetos"),
                ItemsPruebaHA("Realiza juegos simbólicos"),
                ItemsPruebaHA("Participa en juegos tradicionales ¿cuáles y con quién?"),
                ItemsPruebaHA("Participa en juegos de mesa ¿con quién?"),
                ItemsPruebaHA("Realiza actividades preferidas en el hogar"),
                ItemsPruebaHA("Realiza actividades preferidas en el exterior"),
                ItemsPruebaHA("Realiza deportes"),
                ItemsPruebaHA("Participa en actividades de arte"),
                ItemsPruebaHA("Conoce la actualidad de su ciudad/país"),
                ItemsPruebaHA("Utiliza servicios de ocio de su comunidad"),
                ItemsPruebaHA("Participa en forma permanente/esporádica"),
            )
        ),
        CategoriaHA(
            "SOCIALES", listOf(
                ItemsPruebaHA("Presenta / responde gestos sociales básicos"),
                ItemsPruebaHA("Presenta normas básicas de cortesía"),
                ItemsPruebaHA("Expresa asertivamente su desagrado"),
                ItemsPruebaHA("Reconoce/expresa sentimientos"),
                ItemsPruebaHA("Reconoce y respeta componentes visuales y no visuales de la comunicación"),
                ItemsPruebaHA("Inicia y mantiene relaciones"),
                ItemsPruebaHA("Diferencia pautas sociales según grupos y entornos"),
                ItemsPruebaHA("Se defiende o busca ayuda en situaciones de peligro o agresión"),
                ItemsPruebaHA("Respeta y guarda turnos"),
                ItemsPruebaHA("Reconoce y respeta a autoridades"),
                ItemsPruebaHA("Reconoce y repara un error-pide disculpas"),
                ItemsPruebaHA("Organiza sus actividades sociales"),
                ItemsPruebaHA("Pertenece a grupos de su edad"),
            )
        ),
        CategoriaHA(
            "TRABAJO", listOf(
                ItemsPruebaHA("Sigue instrucciones simples/complejas"),
                ItemsPruebaHA("Conoce, imita o anticipa gestos profesionales"),
                ItemsPruebaHA("Cuida materiales y elementos"),
                ItemsPruebaHA("Mantiene el orden en su ámbito y elementos"),
                ItemsPruebaHA("Acepta indicaciones"),
                ItemsPruebaHA("Cumple normas de trabajo"),
                ItemsPruebaHA("Recuerda/respeta secuencias: orales, escritas o gráficas"),
                ItemsPruebaHA("Presenta resistencia a la fatiga"),
                ItemsPruebaHA("Atiende acorde a las demandas del empleo"),
                ItemsPruebaHA("Presenta las Habilidades Sociales necesarias con los compañeros"),
                ItemsPruebaHA("Presenta las Habilidades Sociales necesarias con los superiores"),
                ItemsPruebaHA("Respeta horarios"),
                ItemsPruebaHA("Avisa ante inasistencias"),
                ItemsPruebaHA("Solicita autorizaciones"),
                ItemsPruebaHA("Enfrenta acertadamente situaciones problemáticas y solicita ayuda"),
                ItemsPruebaHA("Reconoce/previene riesgos de sus acciones"),
                ItemsPruebaHA("Realiza solo el trayecto desde su casa"),
                ItemsPruebaHA("Porta y conoce el uso de la identificación"),
            )
        )

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_evaluar_prueba)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val docenteId = intent.getIntExtra("docenteId", -1)

        val recyclerCategorias = findViewById<RecyclerView>(R.id.recyclerCategorias)
        recyclerCategorias.layoutManager = LinearLayoutManager(this)


        val btnGuardarEval = findViewById<FloatingActionButton>(R.id.btnGuardarEvaluacion)
        val btnBorrarEval = findViewById<FloatingActionButton>(R.id.btnBorrarEvaluacion)

        //Valores del intent
        val idEstudiante = intent.getIntExtra("idEstudiante", -1)
        val tipoEvaluacion = intent.getStringExtra("tipoEvaluacion") ?: "Inicio"
        val repo = EvaluacionReposit.getInstance(applicationContext)
        val esContinuar = intent.getBooleanExtra("esContinuar", false)

        Log.d(
            "EvaluarPrueba",
            "Recepcion idEstudiante=$idEstudiante, tipoEvaluacion=$tipoEvaluacion, esContinuar =$esContinuar"
        )

        if (idEstudiante == -1 || tipoEvaluacion.isEmpty()) {
            Toast.makeText(this, "Error en datos", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        //coroutine para operaciones en la bbdd..
        lifecycleScope.launch {
            //EvalPrevias - recuperar datos, nuevas-eval en blanco
            val evaluacionesPrevias = repo.obtenerEvaluaciones(idEstudiante, tipoEvaluacion)
            Log.d(
                "EvaluarPrueba-Carga",
                "Recuperadas ${evaluacionesPrevias.size} evaluaciones para id=$idEstudiante, tipoEvaluacion=$tipoEvaluacion"
            )

            val categoriasConDatos = if (evaluacionesPrevias.isNotEmpty()) {
                categoriasBase.map { categoria ->
                    val itemsActualizados = categoria.items.map { item ->
                        val evalGuardada = evaluacionesPrevias.find {
                            it.item == item.aspecto && it.categoria == categoria.nombreCategoria
                        }
                        //ItemsPruebaHA
                        val itemModificado = ItemsPruebaHA(
                            aspecto = item.aspecto,
                            estado = evalGuardada?.estado ?: "",
                            observaciones = evalGuardada?.observaciones ?: ""
                        )
                        Log.d(
                            "EvaluarPrueba",
                            "Procesando categoría: ${categoria.nombreCategoria}, Ítem: ${item.aspecto}, Estado: ${itemModificado.estado}, Observaciones: ${itemModificado.observaciones}"
                        )
                        itemModificado
                    }
                    CategoriaHA(categoria.nombreCategoria, itemsActualizados)
                }
            } else {
                categoriasBase.map { categoriaHA ->
                    val itemsVacios = categoriaHA.items.map { item ->
                        ItemsPruebaHA(aspecto = item.aspecto, estado = "", observaciones = "")
                    }
                    CategoriaHA(categoriaHA.nombreCategoria, itemsVacios)
                }
            }
            //Debug para verificar categorias e items enviados
            Log.d("EvaluarPrueba", "Total de categorías generadas: ${categoriasConDatos.size}")
            categoriasConDatos.forEach { categoria ->
                Log.d(
                    "EvaluarPrueba",
                    "Categoría: ${categoria.nombreCategoria}, Ítems asignados: ${categoria.items.size}"
                )
            }

            adaptadorCategoria = AdaptadorCategoriaHA(categoriasConDatos)
            recyclerCategorias.adapter = adaptadorCategoria
            recyclerCategorias.adapter?.notifyDataSetChanged()

            //Comportamiento de los botones flotantes para Guardar y Borrar datos de las evaluaciones
            btnBorrarEval.setOnClickListener {
                AlertDialog.Builder(this@EvaluarPrueba).setTitle("Eliminar Evaluación")
                    .setMessage("¿Desea borrar esta evaluación?")
                    .setPositiveButton("Sí") { _, _ ->
                        lifecycleScope.launch {
                            repo.eliminarEvaluacionesPorMomento(idEstudiante, tipoEvaluacion)
                            Toast.makeText(
                                this@EvaluarPrueba,
                                "Evaluación eliminada",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }
                    }
                    .setNegativeButton("No, regresar", null).show()
            }

            btnGuardarEval.setOnClickListener {
                lifecycleScope.launch {
                    val listaEvaluaciones = adaptadorCategoria.obtenerTodosItemsConCategoria()
                        .map { (categoria, item) ->
                            Evaluacion(
                                id = 0,
                                docenteId = docenteId,
                                idEstudiante = idEstudiante,
                                tipoEvaluacion = tipoEvaluacion,
                                categoria = categoria,
                                item = item.aspecto,
                                estado = item.estado ?: "",
                                observaciones = item.observaciones
                            )
                        }
                    Log.d(
                        "EvaluarPrueba",
                        "Guardadas ${listaEvaluaciones.size} evaluaciones para el estudiante $idEstudiante, tipo $tipoEvaluacion"
                    )

                    repo.eliminarEvaluacionesPorMomento(idEstudiante, tipoEvaluacion)
                    repo.insertarEvaluaciones(listaEvaluaciones)

                    Toast.makeText(this@EvaluarPrueba, "Evaluación Guardada", Toast.LENGTH_SHORT)
                        .show()
                    finish()

                }

            }

        }
    }
}

