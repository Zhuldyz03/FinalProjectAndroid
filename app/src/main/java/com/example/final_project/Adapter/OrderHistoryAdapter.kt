package com.example.final_project.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project.R
import com.example.final_project.model.menu
import com.internshala.foodrunner.model.orderItemDetails
import java.text.SimpleDateFormat
import java.util.*

class OrderHistoryAdapter(val context: Context,
                          private val orderHistoryList: ArrayList<orderItemDetails>
) :
    RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OrderHistoryViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.order_history_single_row, p0, false)
        return OrderHistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderHistoryList.size
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, p1: Int) {
        val orderHistoryObject = orderHistoryList[p1]
        holder.txtResName.text = orderHistoryObject.resName
        holder.txtDate.text = formatDate(orderHistoryObject.orderDate)
        setUpRecycler(holder.recyclerResHistory, orderHistoryObject)
    }

    class OrderHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtResName: TextView = view.findViewById(R.id.txtHistoryResName)
        val txtDate: TextView = view.findViewById(R.id.txtOrderDate)
        val recyclerResHistory: RecyclerView = view.findViewById(R.id.recyclerOrderHistoryDetail)
    }
// here we use the same adapter to display item price as in cart activity to reduce the code
    private fun setUpRecycler(recyclerResHistory: RecyclerView, orderHistoryList: orderItemDetails) {
        val foodItemsList = ArrayList<menu>()
        for (i in 0 until orderHistoryList.foodItems.length()) {
            val foodJson = orderHistoryList.foodItems.getJSONObject(i)
            foodItemsList.add(
                menu(
                    foodJson.getString("food_item_id"),
                    foodJson.getString("name"),
                    foodJson.getString("cost").toInt()
                )
            )
        }
        val cartItemAdapter = cartrecylerAdapter(foodItemsList, context)
        val mLayoutManager = LinearLayoutManager(context)
        recyclerResHistory.layoutManager = mLayoutManager
        recyclerResHistory.itemAnimator = DefaultItemAnimator()
        recyclerResHistory.adapter = cartItemAdapter
    }

    private fun formatDate(dateString: String): String? {
        val inputFormatter = SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.ENGLISH)
        val date: Date = inputFormatter.parse(dateString) as Date

        val outputFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        return outputFormatter.format(date)
    }

}