package com.example.automotriz.Entity

data class EntityClient(
        var id: Long,
        var name:String,
        var lastName:String,
        var surName:String,
        var email:String,
        var phone:String){
    constructor():this(0,"", "", "", "", "")
}