package com.example.automotriz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.automotriz.Adapters.CarAdapter
import com.example.automotriz.Adapters.NewsAdapter
import com.example.automotriz.Constants.Constants
import com.example.automotriz.Data.ClientDB
import com.example.automotriz.Entity.EntityCar
import com.example.automotriz.Entity.EntityFilter
import com.example.automotriz.Entity.EntityNews
import com.example.automotriz.Entity.FilterSettings
import com.example.automotriz.databinding.ActivityListCarsBinding
import com.example.automotriz.databinding.ActivitySavedCarsBinding
import org.json.JSONObject

class ListCarsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListCarsBinding
    private val url= Constants.URL_API + "Car/Get/"
    private lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityListCarsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.setTitle(R.string.txt_cars)
        queue = Volley.newRequestQueue(this)

        loadListCars()


        binding.flbtnFilter.setOnClickListener {
            val intent = Intent(this@ListCarsActivity, FilterActivity::class.java).apply{
                putExtra(Constants.FILTER_SETTINGS, 1)
            }
            startActivity(intent)
        }
    }

    override fun onRestart() {
        super.onRestart()
        loadListCars()
    }

    fun loadListCars(){
        val filterSettings = FilterSettings().getFilterSetting()
        val minCost = filterSettings.minCost?.toString() ?: ""
        val brands = getSearchBrands(filterSettings)
        val fuel = getFuelTypes(filterSettings)
        val db = ClientDB(this@ListCarsActivity)
        val idClient = db.getOne()
        val maxCost = filterSettings.maxCost?.toString() ?: ""
        val transmission = getTransmission(filterSettings)


        val myURL = url + "?minCost=${minCost}&brands=${brands}&fuelTypes=${fuel}&idClient=${idClient}&maxCost=${maxCost}&transmission=${transmission}"

        val list = arrayListOf<EntityCar>()
        val stringRequest = StringRequest(Request.Method.GET, myURL,
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
                            myCar.idSaved = array.getJSONObject(0).getInt("isFavorite")
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
                        Toast.makeText(this, R.string.txt_no_coincidences, Toast.LENGTH_LONG).show()
                    }
                    val adapter = CarAdapter(list, this@ListCarsActivity, 0)
                    val linearLayout = LinearLayoutManager(
                            this@ListCarsActivity, LinearLayoutManager.VERTICAL,
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

    fun getSearchBrands(filterSettings: EntityFilter): String{
        var brands = ""
        var count = 0;
        if(filterSettings.brand?.size ?: 0 > 0){
            if(filterSettings.brand?.contains(getString(R.string.txt_sentra)) ?: false){
                brands = brands + "1:1"
                count = 1;
            }
            if(filterSettings.brand?.contains(getString(R.string.txt_versa)) ?: false){
                if(count > 0){
                    brands = brands + ","
                }
                brands = brands + "2:1"
                count = 1;
            }
            if(filterSettings.brand?.contains(getString(R.string.txt_tsuru)) ?: false){
                if(count > 0){
                    brands = brands + ","
                }
                brands = brands + "3:1"
                count = 1;
            }
            if(filterSettings.brand?.contains(getString(R.string.txt_march)) ?: false){
                if(count > 0){
                    brands = brands + ","
                }
                brands = brands + "4:1"
                count = 1;
            }
            if(filterSettings.brand?.contains(getString(R.string.txt_tida)) ?: false){
                if(count > 0){
                    brands = brands + ","
                }
                brands = brands + "5:1"
                count = 1;
            }
        }else{
            brands = ""
        }
        return  brands
    }

    fun getFuelTypes(filterSettings: EntityFilter): String{
        var fuel = ""
        var count = 0;
        if(filterSettings.fuelType?.size ?: 0 > 0){
            if(filterSettings.fuelType?.contains(getString(R.string.txt_gasoline)) ?: false){
                fuel = fuel + "1:1"
                count = 1;
            }
            if(filterSettings.fuelType?.contains(getString(R.string.txt_electric)) ?: false){
                if(count > 0){
                    fuel = fuel + ","
                }
                fuel = fuel + "2:1"
                count = 1;
            }
            if(filterSettings.fuelType?.contains(getString(R.string.txt_diesel)) ?: false){
                if(count > 0){
                    fuel = fuel + ","
                }
                fuel = fuel + "3:1"
                count = 1;
            }

        }else{
            fuel = ""
        }
        return  fuel
    }

    fun getTransmission(filterSettings: EntityFilter): String{
        var fuel = ""
        var count = 0;
        if(filterSettings.transmission?.size ?: 0 > 0){
            if(filterSettings.transmission?.contains(getString(R.string.txt_manual)) ?: false){
                fuel = fuel + "1:1"
                count = 1;
            }
            if(filterSettings.transmission?.contains(getString(R.string.txt_automatic)) ?: false){
                if(count > 0){
                    fuel = fuel + ","
                }
                fuel = fuel + "2:1"
                count = 1;
            }
        }else{
            fuel = ""
        }
        return  fuel
    }
}