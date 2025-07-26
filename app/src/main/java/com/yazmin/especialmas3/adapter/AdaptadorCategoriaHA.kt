package com.yazmin.especialmas3.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yazmin.especialmas3.R
import com.yazmin.especialmas3.model.CategoriaHA
import com.yazmin.especialmas3.model.Evaluacion
import com.yazmin.especialmas3.model.ItemsPruebaHA

/*Este es el adaptador del ReciclerView que muestra
* las categorias de la prueba
* los items
* Se usa en la pantalla de evaluación...*/

class AdaptadorCategoriaHA(
    private val categorias: List<CategoriaHA>,
    private val evaluacionesGuardadas: List<Evaluacion>? = null
) : RecyclerView.Adapter<AdaptadorCategoriaHA.ViewHolder>() {

    //Uso de map para mantener los items organizados por categoria
    private val itemsPorCategoria = mutableMapOf<String, List<ItemsPruebaHA>>()

    //Aqui se unen el ViewHolder de las categorias con el ReciclerV de los items (interno)
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCategoria: TextView = itemView.findViewById(R.id.txtCategoria)
        val recyclerItems: RecyclerView = itemView.findViewById(R.id.recyclerItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categoria, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoria = categorias[position]
        holder.txtCategoria.text = categoria.nombreCategoria
        Log.d(
            "AdaptadorCategoriaHA",
            "Categoría: ${categoria.nombreCategoria}, Ítems generados: ${itemsPorCategoria.size}"
        )

        val itemsConDatos = categoria.items.map { itemOriginal ->
            val nuevoItem = itemOriginal.copy()
            //Si hay evaluacion guardada previamente, se recuperan los estados y observ.
            evaluacionesGuardadas?.find { it.item == itemOriginal.aspecto && it.categoria == categoria.nombreCategoria }
                ?.let { eval ->
                    nuevoItem.estado = eval.estado
                    nuevoItem.observaciones = eval.observaciones ?: ""

                }
            nuevoItem
        }

        itemsPorCategoria[categoria.nombreCategoria] = itemsConDatos

        //Recicler interno de items
        holder.recyclerItems.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.recyclerItems.adapter = AdaptadorPruebaHA(itemsConDatos)
        holder.recyclerItems.isNestedScrollingEnabled = false
        Log.d(
            "AdaptadorCategoriaHA",
            "Categoría: ${categoria.nombreCategoria}, Ítems enviados al adaptador: ${itemsPorCategoria.size}"
        )
    }

    override fun getItemCount(): Int = categorias.size
    //Devuelve los items por cada categoria, juntos
    fun obtenerTodosItemsConCategoria(): List<Pair<String, ItemsPruebaHA>> {
        return itemsPorCategoria.flatMap { (categoria, items) ->
            items.map { item -> categoria to item }
        }
    }
}