package com.example.automotriz.Data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.util.Log
import com.example.automotriz.Contract.ClientContract
import com.example.automotriz.Entity.EntityClient

class ClientDB(val context: Context) {
    val connectionDb = ConnectionDB(context)
    private lateinit var db: SQLiteDatabase

    fun add(idClient: Long): Long{
        db = connectionDb.openConnection(ConnectionDB.MODE_WRITE)

        //Objeto donde se guarda lo que se va a pasar al insert
        val values = ContentValues().apply {
            put(ClientContract.Entry.COLUMN_REMOTE_ID, idClient)
        }
        return db.insert(ClientContract.Entry.TABLE_NAME, null, values)
    }

    fun getOne(): Long
    {
        var id:Long = 0;
        db = connectionDb.openConnection(ConnectionDB.MODE_READ)
        //Atributos que quiere que se vena
        val projection = arrayOf(ClientContract.Entry.COLUMN_REMOTE_ID) //0

        //selection: where, argWhere, sentenciagrpupby, having
        val cursor = db.query(ClientContract.Entry.TABLE_NAME, projection, null, null,
                null, null, null)

        if(cursor.moveToFirst()){
            id = cursor.getLong(0)
        }
        return id
    }


}