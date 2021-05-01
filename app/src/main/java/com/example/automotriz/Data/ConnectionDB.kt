package com.example.automotriz.Data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.automotriz.Contract.ClientContract

class ConnectionDB(val context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_CLIENTS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DROP_TABLE_CLIENTS)
        onCreate(db)
    }

    //Siempre que se debe acceder a la BD se debe especificar el modo
    fun openConnection(typeConnection: Int): SQLiteDatabase {
        return when(typeConnection){
            MODE_WRITE->{
                writableDatabase
            }
            MODE_READ->{
                readableDatabase
            }
            else->{
                readableDatabase
            }
        }
    }

    companion object{
        //Si se cambia algo del modelo de la BD hay que cambiar la version o hay problemas al exe
        const val DATABASE_NAME = "CARS_APP_DB"
        const val DATABASE_VERSION = 3
        const val TABLE_CLIENTS = "CTL_CLIENTS"
        const val CREATE_TABLE_CLIENTS = "CREATE TABLE ${ClientContract.Entry.TABLE_NAME} " +
                "( ${ClientContract.Entry.COLUMN_REMOTE_ID} INTEGER PRIMARY KEY) "
        const val DROP_TABLE_CLIENTS = "DROP TABLE IF EXISTS $TABLE_CLIENTS"
        const val MODE_READ = 1
        const val MODE_WRITE = 2
    }
}