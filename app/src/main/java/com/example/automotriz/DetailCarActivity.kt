package com.example.automotriz

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.automotriz.Constants.Constants
import com.example.automotriz.Data.ClientDB
import com.example.automotriz.Entity.EntityCar
import com.example.automotriz.Entity.EntityNews
import com.example.automotriz.databinding.ActivityDetailCarBinding
import com.example.automotriz.databinding.ActivityDetailNewsBinding
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.json.JSONObject

class DetailCarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCarBinding
    private lateinit var queue: RequestQueue

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailCarBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val idCar = intent.getLongExtra(Constants.ID_CAR, -1)
        queue = Volley.newRequestQueue(this)

        val db = ClientDB(this)
        val idClient = db.getOne()
        val car = EntityCar()
        if(idCar > 0){
            //getOne
            val stringRequest = StringRequest(Request.Method.GET, Constants.URL_API + "Car/" + idCar + "/" + idClient,
                    Response.Listener<String> { response ->
                        val jsonObject = JSONObject(response)
                        if(jsonObject["code"].toString().toInt() >= 1){
                            val array = jsonObject.getJSONArray("valuesCars")
                            car.id = idCar
                            car.cost =  array.getJSONObject(0).getDouble("cost")
                            car.brand = "${array.getJSONObject(0).getString("brandName")} ${array.getJSONObject(0).getString("name")}"
                            car.year = array.getJSONObject(0).getInt("year")
                            car.traction = array.getJSONObject(0).getString("traction")
                            car.transmission = array.getJSONObject(0).getString("transmissionTypeText")
                            car.motor = array.getJSONObject(0).getString("motor")
                            car.idSaved = array.getJSONObject(0).getInt("isFavorite")
                            car.isFavorite = when(array.getJSONObject(0).getInt("isFavorite")){
                                0 -> false
                                else -> true
                            }
                            car.image = array.getJSONObject(0).getString("imageURL")
                            car.fuelType = array.getJSONObject(0).getString("fuelTypeText")
                            car.horesepower = array.getJSONObject(0).getInt("horsePower")
                            supportActionBar?.setTitle(car.brand + ' ' + car.year)
                            binding.txtCost.setText("$ ${car.cost}")
                            binding.txtBrand.setText(car.brand)
                            binding.txtYear.setText(car.year.toString())
                            binding.txtTraction.setText(getString(R.string.txt_traction) +  car.traction)
                            binding.txtTransmission.setText(getString(R.string.txt_transmission) + car.transmission)
                            binding.txtMotor.setText(getString(R.string.txt_motor) + car.motor)
                            binding.txtFuelType.setText(car.fuelType)
                            binding.txtHorsePower.setText( getString(R.string.txt_horse_power) + car.horesepower.toString())
                            if(car.isFavorite){
                                binding.btnSave.setImageResource(R.drawable.ic_favorite_black_24dp)
                            }else{
                                binding.btnSave.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                            }
                            Picasso.get().load(car.image).fit().into(binding.imgCar)
                        }else{
                            Toast.makeText(this, R.string.txt_load_activity_error, Toast.LENGTH_SHORT).show()
                        }

                    },
                    Response.ErrorListener { error ->
                        Toast.makeText(this, R.string.txt_load_activity_error, Toast.LENGTH_SHORT).show()
                    })
            queue.add(stringRequest)


            binding.btnContact.setOnClickListener {
                if(idClient > 0){
                    val intent = Intent(this@DetailCarActivity, ContactActivity::class.java).apply{
                        putExtra(Constants.ID_CAR, idCar)
                    }
                    startActivity(intent)
                }else{
                    actionDialog(getString(R.string.txt_user_contact_required)).show()
                }

            }

            binding.btnSave.setOnClickListener {
                if(idClient > 0){
                    if(car.isFavorite){
                        val stringRequest = StringRequest(Request.Method.DELETE, Constants.URL_API + "SavedCars/" + car.idSaved,
                                Response.Listener<String> { response ->
                                    val jsonObject = JSONObject(response)
                                    if(jsonObject["code"].toString().toInt() >= 1){
                                        car.isFavorite = !car.isFavorite
                                        if(car.isFavorite){
                                            binding.btnSave.setImageResource(R.drawable.ic_favorite_black_24dp)
                                        }else{
                                            binding.btnSave.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                                        }
                                    }else{
                                        Toast.makeText(this, R.string.txt_transaction_error, Toast.LENGTH_SHORT).show()
                                    }


                                },
                                Response.ErrorListener { error ->
                                    Toast.makeText(this, R.string.txt_transaction_error, Toast.LENGTH_SHORT).show()
                                })
                        queue.add(stringRequest)
                    }else{
                        val jsonBody = JSONObject()
                        jsonBody.put("idClient", idClient)
                        jsonBody.put("idCar", car.id)
                        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, Constants.URL_API + "SavedCars",jsonBody,
                                Response.Listener { response ->
                                    if(response["code"].toString().toInt() >= 1){
                                        car.isFavorite = !car.isFavorite
                                        car.idSaved = response["code"].toString().toInt()
                                        if(car.isFavorite){
                                            binding.btnSave.setImageResource(R.drawable.ic_favorite_black_24dp)
                                        }else{
                                            binding.btnSave.setImageResource(R.drawable.ic_favorite_black_24dp)
                                        }
                                    }else{
                                        Toast.makeText(this, R.string.txt_transaction_error, Toast.LENGTH_SHORT).show()
                                    }

                                },
                                Response.ErrorListener { error ->
                                    Toast.makeText(this, R.string.txt_transaction_error, Toast.LENGTH_SHORT).show()
                                })
                        queue.add(jsonObjectRequest)
                    }


                }else{
                    actionDialog(getString(R.string.txt_user_contact_required)).show()
                }
            }

        }else{
            Toast.makeText(this@DetailCarActivity, R.string.txt_load_activity_error, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    fun actionDialog(message:String): AlertDialog {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(R.string.app_name)
        alert.setMessage(message)
        alert.setPositiveButton(R.string.txt_ok){_,_ ->

        }
        return  alert.create()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itmNews -> {
                val intent = Intent(this@DetailCarActivity, NewsActivity::class.java)
                startActivity(intent)
            }
            R.id.itmSearch -> {
                val intent = Intent(this@DetailCarActivity, FilterActivity::class.java)
                startActivity(intent)
            }
            R.id.itmSaved -> {
                val intent = Intent(this@DetailCarActivity, SavedCarsActivity::class.java)
                startActivity(intent)
            }
            R.id.itmMyAccount -> {
                val intent = Intent(this@DetailCarActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}