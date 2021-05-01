package com.example.automotriz.Entity

class FilterSettings {

    fun setFilterSettings(filter:EntityFilter){
        filterSettings.maxCost = filter.maxCost
        filterSettings.minCost = filter.minCost
        filterSettings.fuelType = filter.fuelType
        filterSettings.transmission = filter.transmission
    }

    fun getFilterSetting(): EntityFilter{
        return filterSettings
    }
    companion object{
        private var filterSettings = EntityFilter()
    }
}