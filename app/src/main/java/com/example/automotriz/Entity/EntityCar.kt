package com.example.automotriz.Entity

data class EntityCar(
    var id: Long,
    var brand:String,
    var year:Int,
    var image:String,
    var transmission:String,
    var traction:String,
    var motor:String,
    var fuelType:String,
    var horesepower:Int,
    var cost: Double,
    var isFavorite: Boolean,
    var idSaved: Int){
    constructor():this(0,"", 0, "", "", "", "", "", 0, 0.0, false, 0)
}