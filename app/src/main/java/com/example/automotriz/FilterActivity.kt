package com.example.automotriz

import android.R
import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.automotriz.databinding.ActivityFilterBinding
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.automotriz.Adapters.BrandsAdapter
import com.example.automotriz.Adapters.NewsAdapter
import com.example.automotriz.Constants.Constants
import com.example.automotriz.Entity.EntityBrands
import com.example.automotriz.Entity.EntityFilter
import com.example.automotriz.Entity.EntityNews
import com.example.automotriz.Entity.FilterSettings
import org.json.JSONObject


class FilterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilterBinding
    private lateinit var queue: RequestQueue
    private val url= Constants.URL_API + "Brands"

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFilterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        queue = Volley.newRequestQueue(this)

        val hasFilter = intent.getIntExtra(Constants.FILTER_SETTINGS, -1)

        loadBrands(hasFilter)

        if(hasFilter == 1){
            val filterSettings = FilterSettings().getFilterSetting()
            binding.chkAutomatic.isChecked = filterSettings.transmission.contains(binding.chkAutomatic.text)
            binding.chkManual.isChecked = filterSettings.transmission.contains(binding.chkManual.text)
            binding.chkGasoline.isChecked = filterSettings.fuelType.contains(binding.chkGasoline.text)
            binding.chkElectric.isChecked = filterSettings.fuelType.contains(binding.chkElectric.text)
            binding.chkDiesel.isChecked = filterSettings.fuelType.contains(binding.chkDiesel.text)
            binding.edtMinCost.setText(filterSettings.minCost?.toString() ?: "")
            binding.edtMaxCost.setText(filterSettings.maxCost?.toString() ?: "")
        }

        binding.btnSearch.setOnClickListener {
            val filters: EntityFilter = getFileterParameters()
            val filterSettings = FilterSettings()
            filterSettings.setFilterSettings(filters)
            val intent = Intent(this@FilterActivity, ListCarsActivity::class.java)
            startActivity(intent)
        }

        binding.btnCleanFilter.setOnClickListener {
            binding.chkAutomatic.isChecked = false
            binding.chkManual.isChecked = false
            binding.chkGasoline.isChecked = false
            binding.chkElectric.isChecked = false
            binding.chkDiesel.isChecked = false
            binding.edtMinCost.setText("")
            binding.edtMaxCost.setText("")
            val filters = EntityFilter()
            val filterSettings = FilterSettings()
            filterSettings.setFilterSettings(filters)
            loadBrands(0)
        }

   }
    fun getFileterParameters(): EntityFilter {
        val filterSettings = EntityFilter()
        val brands = arrayListOf<String>()
        val fuelTypes = arrayListOf<String>()
        val transmission = arrayListOf<String>()
        if(binding.chkManual.isChecked){
            transmission.add("${binding.chkManual.text}")
        }
        if(binding.chkAutomatic.isChecked){
            transmission.add("${binding.chkAutomatic.text}")
        }
        if(binding.chkGasoline.isChecked){
            fuelTypes.add("${binding.chkGasoline.text}")
        }
        if(binding.chkDiesel.isChecked){
            fuelTypes.add("${binding.chkDiesel.text}")
        }
        if(binding.chkElectric.isChecked){
            fuelTypes.add("${binding.chkElectric.text}")
        }

        //filterSettings.brand = brands
        filterSettings.brand = filterSettings.brand
        filterSettings.fuelType = fuelTypes
        filterSettings.transmission = transmission
        if(binding.edtMinCost.text.isNotEmpty()){
            filterSettings.minCost = binding.edtMinCost.text.toString().toDouble()
        }
         if(binding.edtMaxCost.text.isNotEmpty()){
            filterSettings.maxCost = binding.edtMaxCost.text.toString().toDouble()
        }
        return filterSettings
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.example.automotriz.R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            com.example.automotriz.R.id.itmNews -> {
                val intent = Intent(this@FilterActivity, NewsActivity::class.java)
                startActivity(intent)
            }
            com.example.automotriz.R.id.itmSearch -> {
                val intent = Intent(this@FilterActivity, FilterActivity::class.java)
                startActivity(intent)
            }
            com.example.automotriz.R.id.itmSaved -> {
                val intent = Intent(this@FilterActivity, SavedCarsActivity::class.java)
                startActivity(intent)
            }
            com.example.automotriz.R.id.itmMyAccount -> {
                val intent = Intent(this@FilterActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun loadBrands(hasFilter: Int) {
        val list = arrayListOf<EntityBrands>()
        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response ->
                    val jsonObject = JSONObject(response)
                    if(jsonObject["code"] == 1) {
                        val array = jsonObject.getJSONArray("valuesBrands")
                        for(i in 0 until array.length()){
                            val brand = EntityBrands();
                            brand.id = array.getJSONObject(i).getLong("id")
                            brand.brandName = array.getJSONObject(i).getString("brandName")
                            list.add(brand)
                        }
                        val adapter = BrandsAdapter(list, this@FilterActivity, hasFilter)
                        val linearLayout = LinearLayoutManager(
                                this@FilterActivity, LinearLayoutManager.VERTICAL,
                                false
                        )
                        binding.rwsBrands.layoutManager = linearLayout
                        binding.rwsBrands.adapter = adapter
                    }


                },
                Response.ErrorListener { error ->
                    Toast.makeText(this, com.example.automotriz.R.string.txt_load_activity_error, Toast.LENGTH_SHORT).show()
                })
        queue.add(stringRequest)

    }
}