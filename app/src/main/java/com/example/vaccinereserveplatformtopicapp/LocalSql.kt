package com.example.vaccinereserveplatformtopicapp

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LocalSql(context: Context) : SQLiteOpenHelper(context,"local.db",null,4){
    override fun onCreate(p0: SQLiteDatabase) {
        p0.execSQL("create table if not exists TempRec (id integer primary key autoincrement , timeData text , tempData integer)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun run(sqlCommand:String){
        writableDatabase.execSQL(sqlCommand)
    }

    fun read(sqlCommand:String): Cursor {
        return readableDatabase.rawQuery(sqlCommand , null)
    }


}