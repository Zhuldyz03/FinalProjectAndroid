package com.example.final_project.Fragments


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.final_project.Adapter.AllRestaurantsAdapter
import com.example.final_project.R
import com.example.final_project.Util.ConnectionManager
import com.example.final_project.model.Restaurants
import java.lang.Exception
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class AllRestraunts : Fragment() {
  private  lateinit var recyclerRestaurants: RecyclerView
   private lateinit var layoutManager: RecyclerView.LayoutManager
  private  var previousMenuItem: MenuItem? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var progressLayout: RelativeLayout

    lateinit var recyclerAdapter: AllRestaurantsAdapter
    val RestaurantInfoList = arrayListOf<Restaurants>()


    var priceComparator = Comparator<Restaurants> { res1, res2 ->
        val priceOne = res1.restaurantPrice.toInt()
        val priceTwo = res2.restaurantPrice.toInt()
        if (priceOne.compareTo(priceTwo) == 0) {
            ratingComparator.compare(res1, res2)
        } else {
            priceOne.compareTo(priceTwo)
        }
    }

    var ratingComparator = Comparator<Restaurants> { res1, res2 ->
        val ratingOne = res1.restaurantRating as String
        val ratingTwo = res2.restaurantRating as String
        if (ratingOne.compareTo(ratingTwo) == 0) {
            val costOne = res1.restaurantPrice.toInt()
            val costTwo = res2.restaurantPrice.toInt()
            costOne.compareTo(costTwo)
        } else {
            ratingOne.compareTo(ratingTwo)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_restraunts, container, false)
        recyclerRestaurants = view.findViewById(R.id.recyclerAllRestaurants)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressLayout.visibility=View.VISIBLE

        setUPRestaurant()
        return  view
    }


private fun setUPRestaurant(){
      layoutManager = LinearLayoutManager(activity)
        setHasOptionsMenu(true)
        if (ConnectionManager().isNetworkAvailable(activity as Context)) {
            val queue = Volley.newRequestQueue(activity as Context)// volley is a library
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/ "
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {
                    progressLayout.visibility=View.GONE

                    try {


                        val jsonObject = it.getJSONObject("data")
                        val success = jsonObject.getBoolean("success")
                        if (success) {

                            val data = jsonObject.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val restaurantJsonObject = data.getJSONObject(i)
                                val restaurantObject = Restaurants(
                                    restaurantJsonObject.getString("id").toInt(),
                                    restaurantJsonObject.getString("name"),
                                    restaurantJsonObject.getString("rating"),
                                    restaurantJsonObject.getString("cost_for_one"),
                                    restaurantJsonObject.getString("image_url")
                                )
                                RestaurantInfoList.add(restaurantObject)// parsing the data received to the array
                                recyclerAdapter =
                                    AllRestaurantsAdapter(activity as Context, RestaurantInfoList)
                                recyclerRestaurants.adapter = recyclerAdapter
                                recyclerRestaurants.layoutManager = layoutManager
                            }
                        } else {
                            Toast.makeText(
                                activity as Context,
                                " Error has occured",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    }catch (e : Exception){
                        e.printStackTrace()
                    }
                }, Response.ErrorListener {
                    // Here we will handle the error
                    println(" Error is $it")

                }) {
                override fun getHeaders(): MutableMap<String, String> {

                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    headers["token"] = "b312a26fe9bf61"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)

        }else{
            val dialog = android.app.AlertDialog.Builder(activity as Context)
            dialog.setTitle(" Error ")
            dialog.setMessage(" Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings"){text,listener->

                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)// open settings
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text, listener->
                ActivityCompat.finishAffinity(activity as Activity) // this code is used to finish the app at any moment
                //do Nothing
            }
            dialog.create()
            dialog.show()
        }
  }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.res_sorter, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


    val id = item.itemId
    if (previousMenuItem != null) {
        previousMenuItem?.setChecked(false)
    }
    item.setChecked(true)

    when (id) {
        R.id.costHigh -> {
            Collections.sort(RestaurantInfoList, priceComparator)
            RestaurantInfoList.reverse()
            recyclerAdapter.notifyDataSetChanged()
        }
        R.id.costLow -> {
            Collections.sort(RestaurantInfoList, priceComparator)
            recyclerAdapter.notifyDataSetChanged()
        }
        R.id.rating -> {
            Collections.sort(RestaurantInfoList, ratingComparator)
            RestaurantInfoList.reverse()
            recyclerAdapter.notifyDataSetChanged()
        }
    }
    previousMenuItem = item
    return super.onOptionsItemSelected(item)
}


}

