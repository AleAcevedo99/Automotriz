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
import com.example.automotriz.Constants.Constants
import com.example.automotriz.Entity.EntityFilter
import com.example.automotriz.Entity.FilterSettings


class FilterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFilterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val hasFilter = intent.getIntExtra(Constants.FILTER_SETTINGS, -1)

        if(hasFilter == 1){
            val filterSettings = FilterSettings().getFilterSetting()
            binding.chkSentra.isChecked = filterSettings.brand?.contains(binding.chkSentra.text) ?: false
            binding.chkVersa.isChecked = filterSettings.brand?.contains(binding.chkVersa.text) ?: false
            binding.chkTsuru.isChecked = filterSettings.brand?.contains(binding.chkTsuru.text) ?: false
            binding.chkTida.isChecked = filterSettings.brand?.contains(binding.chkTida.text) ?: false
            binding.chkMarch.isChecked = filterSettings.brand?.contains(binding.chkMarch.text) ?: false
            binding.chkAutomatic.isChecked = filterSettings.transmission?.contains(binding.chkAutomatic.text) ?: false
            binding.chkManual.isChecked = filterSettings.transmission?.contains(binding.chkManual.text) ?: false
            binding.chkGasoline.isChecked = filterSettings.fuelType?.contains(binding.chkGasoline.text) ?: false
            binding.chkElectric.isChecked = filterSettings.fuelType?.contains(binding.chkElectric.text) ?: false
            binding.chkDiesel.isChecked = filterSettings.fuelType?.contains(binding.chkDiesel.text) ?: false
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
            binding.chkSentra.isChecked = false
            binding.chkVersa.isChecked = false
            binding.chkTsuru.isChecked = false
            binding.chkTida.isChecked = false
            binding.chkMarch.isChecked = false
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
        }

   }
    fun getFileterParameters(): EntityFilter {
        val filterSettings = EntityFilter()
        val brands = arrayListOf<String>()
        val fuelTypes = arrayListOf<String>()
        val transmission = arrayListOf<String>()

        if(binding.chkSentra.isChecked){
            brands.add("${binding.chkSentra.text}")

        }
        if(binding.chkTsuru.isChecked){
            brands.add("${binding.chkTsuru.text}")
        }
        if(binding.chkVersa.isChecked){
            brands.add("${binding.chkVersa.text}")
        }
        if(binding.chkTida.isChecked){
            brands.add("${binding.chkTida.text}")
        }
        if(binding.chkMarch.isChecked){
            brands.add("${binding.chkMarch.text}")
        }
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

        filterSettings.brand = brands
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
}