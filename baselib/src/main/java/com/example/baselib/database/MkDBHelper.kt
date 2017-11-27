package com.example.baselib.database

import android.annotation.SuppressLint
import net.sqlcipher.database.SQLiteDatabase

/**
 * Created by joseph on 2017/11/27.
 */
class MkDBHelper(name: String?,version: Int)
    : BaseDBHelper(name,version) {

    companion object {
        val DB_VERSION = 1

        val DB_DOWNLOAD = "markdown.db"

        val TABLE_DOWNLOAD_FILE = "file_download"

        val SQL_DOWNLOAD = "create table if not exists ${TABLE_DOWNLOAD_FILE}(_id integer primary key autoincrement,id integer not null,url text not null,progress text not null,length text not null,_extra1 text default(''),_extra2 text default('')"

        @SuppressLint("StaticFieldLeak")
        var dbHelper : MkDBHelper? = null

        fun getInstance() : MkDBHelper{
            if(dbHelper == null){
                synchronized(MkDBHelper:: class.java.simpleName){
                    if(dbHelper == null){
                        dbHelper = MkDBHelper(DB_DOWNLOAD, DB_VERSION)
                    }
                }
            }
            return dbHelper!!
        }
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        super.onCreate(p0)
        p0!!.execSQL(SQL_DOWNLOAD)

    }

}