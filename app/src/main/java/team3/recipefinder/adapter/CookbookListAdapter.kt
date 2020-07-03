package team3.recipefinder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import team3.recipefinder.R
import team3.recipefinder.model.Cookbook

class CookbookListAdapter(context: Context, val onClick: (Cookbook) -> Unit) :
    RecyclerView.Adapter<CookbookListAdapter.ViewHolder>() {

    private val inflater by lazy { LayoutInflater.from(context) }

    var cookbooks = mutableListOf<Cookbook>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recipeName = view.findViewById<TextView>(R.id.recipeName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.cookbook, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = cookbooks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        cookbooks[position].let {
            holder.recipeName.text = it.name
            holder.itemView.setOnClickListener { _ -> onClick(it) }
        }
    }
}
