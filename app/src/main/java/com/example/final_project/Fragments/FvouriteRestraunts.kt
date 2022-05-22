package com.example.final_project.Fragments


import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.final_project.Adapter.AllRestaurantsAdapter

import com.example.final_project.R
import com.example.final_project.database.cartDatabase
import com.example.final_project.model.Restaurants
import com.internshala.foodrunner.database.FavresEntities


class FvouriteRestraunts : Fragment() {
    private lateinit var recyclerFvouriteRestraunts: RecyclerView
    private lateinit var allRestaurantsAdapter: AllRestaurantsAdapter
    private var restaurantList = arrayListOf<Restaurants>()
    private lateinit var progressLayout: RelativeLayout
    private lateinit var relativeFavourites: RelativeLayout
    private lateinit var rlNoFavourites: RelativeLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



       val  view = inflater.inflate(R.layout.fragment_fvourite_restraunts, container, false)
        relativeFavourites = view.findViewById(R.id.relativeFavorites)
        rlNoFavourites = view.findViewById(R.id.rlNoFavorites)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE
        setUpFavouriteFragment(view)
   return view
    }

    fun setUpFavouriteFragment(view:View){
        recyclerFvouriteRestraunts= view.findViewById(R.id.recyclerFavouriteRestaurants)
        val backgroundList = FavouritesAsync(activity as Context).execute().get()
        if (backgroundList.isEmpty()) {
            progressLayout.visibility = View.GONE
            relativeFavourites.visibility = View.GONE
            rlNoFavourites.visibility = View.VISIBLE
        } else {
            relativeFavourites.visibility = View.VISIBLE
            progressLayout.visibility = View.GONE
            rlNoFavourites.visibility = View.GONE
            for (i in backgroundList) {   //  we add the data to the restautrant list which is of data class restauarants like.
                restaurantList.add(
                    Restaurants(
                        i.id,
                        i.resName,
                        i.resRating,
                        i.resCostForTwo,
                        i.resImageUrl
                    )
                )
            }

            allRestaurantsAdapter = AllRestaurantsAdapter(activity as Context,restaurantList)
            val mLayoutManager = LinearLayoutManager(activity)
            recyclerFvouriteRestraunts.layoutManager = mLayoutManager
            recyclerFvouriteRestraunts.itemAnimator = DefaultItemAnimator()
            recyclerFvouriteRestraunts.adapter = allRestaurantsAdapter
            recyclerFvouriteRestraunts.setHasFixedSize(true)
        }


    }

    class FavouritesAsync(context: Context) : AsyncTask<Void, Void, List<FavresEntities>>() {

        val db = Room.databaseBuilder(context, cartDatabase::class.java, "res-db").build()

        override fun doInBackground(vararg params: Void?): List<FavresEntities> {

            return db.favresDao().getAllRestaurants()
        }

    }
}
