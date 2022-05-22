package com.internshala.foodrunner.model

import org.json.JSONArray

data class orderItemDetails(
    val orderId: Int,
    val resName: String,
    val orderDate: String,
    val foodItems: JSONArray
)