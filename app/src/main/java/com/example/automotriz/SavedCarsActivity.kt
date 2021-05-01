package com.example.automotriz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.automotriz.Adapters.CarAdapter
import com.example.automotriz.Constants.Constants
import com.example.automotriz.Data.ClientDB
import com.example.automotriz.Entity.EntityCar
import com.example.automotriz.databinding.ActivityContactBinding
import com.example.automotriz.databinding.ActivitySavedCarsBinding
import org.json.JSONObject

class SavedCarsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySavedCarsBinding
    private lateinit var queue: RequestQueue
    private val url = Constants.URL_API + "SavedCars/"

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySavedCarsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.setTitle(R.string.txt_saved_cars)
        queue = Volley.newRequestQueue(this)

        loadSavedCars()

    }

    override fun onRestart() {
        super.onRestart()
        loadSavedCars()
    }

    fun loadSavedCars(){
        val db = ClientDB(this)
        val idClient = db.getOne()
        val list = arrayListOf<EntityCar>()
        val stringRequest = StringRequest(Request.Method.GET, url + "/" + idClient,
                Response.Listener<String> { response ->
                    val jsonObject = JSONObject(response)
                    if(jsonObject["code"] == 1){
                        val array = jsonObject.getJSONArray("valuesCars")
                        for(i in 0 until array.length()){
                            val myCar = EntityCar();
                            myCar.id = array.getJSONObject(i).getLong("id")
                            myCar.brand = "${array.getJSONObject(i).getString("brandName")} ${array.getJSONObject(i).getString("name")}"
                            myCar.year = array.getJSONObject(i).getInt("year")
                            myCar.transmission = array.getJSONObject(i).getString("transmissionTypeText")
                            myCar.idSaved = array.getJSONObject(i).getInt("isFavorite")
                            myCar.isFavorite = when(array.getJSONObject(i).getInt("isFavorite")){
                                0 -> false
                                else -> true
                            }
                            myCar.image = array.getJSONObject(i).getString("imageURL")
                            myCar.cost = array.getJSONObject(i).getDouble("cost")
                            list.add(myCar)
                        }
                    }
                    else{
                        Toast.makeText(this, R.string.txt_no_cars_saved, Toast.LENGTH_LONG).show()
                    }
                    val adapter = CarAdapter(list, this@SavedCarsActivity, 1)
                    val linearLayout = LinearLayoutManager(
                            this@SavedCarsActivity, LinearLayoutManager.VERTICAL,
                            false
                    )
                    binding.rwsCars.layoutManager = linearLayout
                    binding.rwsCars.adapter = adapter

                },
                Response.ErrorListener { error ->
                    Toast.makeText(this, R.string.txt_load_activity_error, Toast.LENGTH_SHORT).show()
                })
        queue.add(stringRequest)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itmNews -> {
                val intent = Intent(this@SavedCarsActivity, NewsActivity::class.java)
                startActivity(intent)
            }
            R.id.itmSearch -> {
                val intent = Intent(this@SavedCarsActivity, FilterActivity::class.java)
                startActivity(intent)
            }
            R.id.itmSaved -> {
                val intent = Intent(this@SavedCarsActivity, SavedCarsActivity::class.java)
                startActivity(intent)
            }
            R.id.itmMyAccount -> {
                val intent = Intent(this@SavedCarsActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}