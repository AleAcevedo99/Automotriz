package com.example.automotriz.Entity

data class EntityNews(
    var id: Long,
    var title:String,
    var imageUrl:String,
    var date:String,
    var content:String){
    constructor():this(0,"", "", "", "")
}