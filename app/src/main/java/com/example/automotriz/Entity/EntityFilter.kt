package com.example.automotriz.Entity

data class EntityFilter(
        var brand:ArrayList<String>,
        var brandsSearch: ArrayList<String>,
        var fuelType: ArrayList<String>,
        var transmission:ArrayList<String>,
        var minCost: Double?,
        var maxCost: Double?){
    constructor():this(arrayListOf<String>(), arrayListOf<String>(),arrayListOf<String>(), arrayListOf<String>(), null, null)
}