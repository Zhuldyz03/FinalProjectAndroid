package com.example.final_project.Adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.final_project.Activities.RestaurantMenuActivity
import com.example.final_project.R
import com.example.final_project.database.cartDatabase
import com.example.final_project.model.Restaurants
import com.internshala.foodrunner.database.FavresEntities
import com.squareup.picasso.Picasso

class AllRestaurantsAdapter(val context : Context, val RestaurantList: ArrayList<Restaurants>): RecyclerView.Adapter<AllRestaurantsAdapter.AllRestaurantsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllRestaurantsViewHolder {
        val view = LayoutInflater.from(parent. context).inflate(R.layout.allrestaurants_recyler_single_row,parent,false)
        return AllRestaurantsViewHolder(view)
    }

    override fun getItemCount(): Int {
         return RestaurantList.size
    }

    override fun onBindViewHolder(holder: AllRestaurantsViewHolder, position: Int) {


        val Restaurants = RestaurantList[position]
        holder.txtRestaurantName.text = Restaurants.restaurantName
        val txtPricePerPerson = "Tg.${Restaurants.restaurantPrice}/person"
        holder.txtPrice.text = txtPricePerPerson
        holder.txtRating.text = Restaurants.restaurantRating
        Picasso.get().load(Restaurants.restaurantImageUrl).into(holder.imgRestaurantImage)

        holder.imgFav.setOnClickListener {
            val favresEntitiesEntity = FavresEntities(
                Restaurants.restaurantId,
                Restaurants.restaurantName,
                Restaurants.restaurantRating,
                Restaurants.restaurantPrice,
                Restaurants.restaurantImageUrl
            )

            if (!DBAsyncTask(context, favresEntitiesEntity, 1).execute().get()) {
                val async =
                    DBAsyncTask(context, favresEntitiesEntity, 2).execute()
                val result = async.get()
                if (result) {
                    holder.imgFav.setImageResource(R.drawable.ic_favourited_icon)
                }
            } else {
                val async = DBAsyncTask(context, favresEntitiesEntity, 3).execute()
                val result = async.get()
                if (result) {
                holder.imgFav.setImageResource(R.drawable.ic_favourites_icon)
                }
            }
        }

        val listOfFavourites = GetAllFavAsyncTask(context).execute().get()
        if (listOfFavourites.isNotEmpty() && listOfFavourites.contains(Restaurants.restaurantId.toString())) {
            holder.imgFav.setImageResource(R.drawable.ic_favourited_icon)
        } else {
            holder.imgFav.setImageResource(R.drawable.ic_favourites_icon)
        }



        holder.cardRestaurant.setOnClickListener {
            val intent = Intent(context,RestaurantMenuActivity::class.java)
            intent.putExtra("id",Restaurants.restaurantId as Int)
            intent.putExtra("name",Restaurants.restaurantName)

            context.startActivity(intent)
        }
    }

    class AllRestaurantsViewHolder(view: View): RecyclerView.ViewHolder(view){

        val txtRestaurantName: TextView = view.findViewById(R.id.txtRestrauntName)
        val txtPrice: TextView = view.findViewById(R.id.txtPrice)
        val txtRating: TextView =view.findViewById(R.id.txtRating)
        val imgRestaurantImage: ImageView =view.findViewById(R.id.imgRestaurantImage)
        val cardRestaurant: CardView= view.findViewById(R.id.cardRestaurant)
        val imgFav: ImageView=view.findViewById(R.id.imgFav)

    }

    class DBAsyncTask(context: Context, val favresEntities:FavresEntities , val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(context, cartDatabase::class.java, "res-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            /*
            Mode 1 -> Check DB if the book is favourite or not
            Mode 2 -> Save the book into DB as favourite
            Mode 3 -> Remove the favourite book
            */

            when (mode) {

                1 -> {
                    val res: FavresEntities? =
                        db.favresDao().getRestaurantById(favresEntities.id.toString())
                    db.close()
                    return res != null
                }

                2 -> {
                    db.favresDao().insertRestaurant(favresEntities)
                    db.close()
                    return true
                }

                3 -> {
                    db.favresDao().deleteRestaurant(favresEntities)
                    db.close()
                    return true
                }
            }

            return false
        }

    }
    class GetAllFavAsyncTask(
        context: Context
    ) :
        AsyncTask<Void, Void, List<String>>() {

        val db = Room.databaseBuilder(context, cartDatabase::class.java, "res-db").build()
        override fun doInBackground(vararg params: Void?): List<String> {
            val list = db.favresDao().getAllRestaurants()
            val listOfFavid = arrayListOf<String>()
            for (i in list) {
                listOfFavid.add(i.id.toString())
            }
            return listOfFavid
        }
    }
}


