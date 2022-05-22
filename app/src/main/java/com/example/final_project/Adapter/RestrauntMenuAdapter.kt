package com.example.final_project.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project.R
import com.example.final_project.model.menu

class RestrauntMenuAdapter(val context : Context, val menuList:ArrayList<menu>, private val listener: OnItemClickListener): RecyclerView.Adapter<RestrauntMenuAdapter.RestaurantsMenuViewHolder>() {


    companion object {
        var CartEmpty = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantsMenuViewHolder {
        val view = LayoutInflater.from(parent. context).inflate(R.layout.menu_single_row,parent,false)
        return RestrauntMenuAdapter.RestaurantsMenuViewHolder(view)
    }
    interface OnItemClickListener {
        fun onAddItemClick(menuItem: menu)
        fun onRemoveItemClick(menuItem: menu)
    }


    override fun getItemCount(): Int {
return menuList.size   }

    override fun onBindViewHolder(holder: RestaurantsMenuViewHolder, position: Int) {
        val menu= menuList[position]
        holder.txtItemName.text=menu.itemName
        val price= menu.itemPrice
        holder.txtItemCost.text="Tg. ${price}"
        holder.txtSNo.text="${position+1}"
        holder.btnAddToCart.setOnClickListener {
            holder.btnAddToCart.visibility= View.GONE
            holder.btnRemoveFromCart.visibility=View.VISIBLE
            listener.onAddItemClick(menu)
        }

        holder.btnRemoveFromCart.setOnClickListener {
            holder.btnRemoveFromCart.visibility=View.GONE
            holder.btnAddToCart.visibility=View.VISIBLE
            listener.onRemoveItemClick(menu)
        }
    }override fun getItemViewType(position: Int): Int {
        return position
    }


    class RestaurantsMenuViewHolder(view: View): RecyclerView.ViewHolder(view){

        val txtItemName: TextView = view.findViewById(R.id.txtItemName)
        val txtItemCost: TextView = view.findViewById(R.id.txtItemCost)
        val txtSNo: TextView =view.findViewById(R.id.txtSNo)
        val btnAddToCart: Button =view.findViewById(R.id.btnAddToCart)
        val btnRemoveFromCart: Button= view.findViewById(R.id.btnRemoveFromCart)
    }
}