package com.example.automotriz.Entity

class FilterSettings {

    fun setFilterSettings(filter:EntityFilter){
        filterSettings = filter
    }

    fun getFilterSetting(): EntityFilter{
        return filterSettings
    }
    companion object{
        private var filterSettings = EntityFilter()
    }
}