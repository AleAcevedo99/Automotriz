package com.example.automotriz.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlin.collections.ArrayList
import com.example.automotriz.Constants.Constants
import com.example.automotriz.Data.ClientDB
import com.example.automotriz.DetailCarActivity
import com.example.automotriz.Entity.EntityCar
import com.example.automotriz.Entity.EntityNews
import com.example.automotriz.R
import com.example.automotriz.databinding.ItemCarBinding
import org.json.JSONObject

class CarAdapter(var carsList:ArrayList<EntityCar>, val context: Context, val type:Int): RecyclerView.Adapter<CarHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CarHolder(inflater.inflate(R.layout.item_car, parent, false))
    }

    override fun getItemCount(): Int {
        return carsList.size
    }

    override fun onBindViewHolder(holder: CarHolder, position: Int) {

        //holder.image
        holder.txtBrand.text = "${carsList[position].brand}"
        holder.txtYear.text = "${carsList[position].year}"
        holder.txtTransmission.text = "${carsList[position].transmission}"
        holder.txtCost.text = "$ ${carsList[position].cost}"
        if(carsList[position].isFavorite){
            holder.imgFavorite.setImageResource(R.drawable.ic_favorite_black_24dp)
        }else{
            holder.imgFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailCarActivity::class.java).apply{
                putExtra(Constants.ID_CAR, carsList[position].id)
            }
            context.startActivity(intent)
        }

        holder.imgFavorite.setOnClickListener {
            var queue: RequestQueue = Volley.newRequestQueue(context)
            val db = ClientDB(context)
            val idClient = db.getOne()
            if(idClient > 0){
                if(carsList[position].isFavorite){
                    val stringRequest = StringRequest(Request.Method.DELETE, Constants.URL_API + "SavedCars/" + idClient + "/" + carsList[position].id,
                            Response.Listener<String> { response ->
                                val jsonObject = JSONObject(response)
                                if(jsonObject["code"].toString().toInt() >= 1){
                                    carsList[position].isFavorite = !carsList[position].isFavorite
                                    if(type == 1){
                                        carsList.removeAt(position)
                                    }
                                    notifyDataSetChanged()

                                }else{
                                    Toast.makeText(context, R.string.txt_transaction_error, Toast.LENGTH_SHORT).show()
                                }

                            },
                            Response.ErrorListener { error ->
                                  Toast.makeText(context, R.string.txt_transaction_error, Toast.LENGTH_SHORT).show()
                            })
                    queue.add(stringRequest)
                }else{
                    val jsonBody = JSONObject()
                    jsonBody.put("idClient", idClient)
                    jsonBody.put("idCar", carsList[position].id)
                    val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, Constants.URL_API + "SavedCars",jsonBody,
                            Response.Listener { response ->
                                if(response["code"].toString().toInt() >= 1){
                                    carsList[position].isFavorite = !carsList[position].isFavorite
                                    notifyDataSetChanged()
                                }else{
                                    Toast.makeText(context, R.string.txt_transaction_error, Toast.LENGTH_SHORT).show()
                                }

                            },
                            Response.ErrorListener { error ->
                                Toast.makeText(context, R.string.txt_transaction_error, Toast.LENGTH_SHORT).show()
                            })
                    queue.add(jsonObjectRequest)
                }


            }else{
                actionDialog().show()
            }

        }


    }

    fun actionDialog(): AlertDialog {
        val alert = AlertDialog.Builder(context)
        alert.setTitle(R.string.app_name)
        alert.setMessage(R.string.txt_user_contact_required)

        alert.setPositiveButton(R.string.txt_ok){_,_ ->

        }
        return  alert.create()
    }

}

class CarHolder(view: View): RecyclerView.ViewHolder(view){
    val binding = ItemCarBinding.bind(view)

    val imgCar = binding.imgCar
    val txtBrand = binding.txtBrand
    val txtYear = binding.txtYear
    val txtTransmission = binding.txtTransmission
    val txtCost = binding.txtCost
    val imgFavorite = binding.imgFavorite
}