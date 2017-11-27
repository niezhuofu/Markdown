package com.example.baselib.database

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteOpenHelper

/**
 * Created by joseph on 2017/11/27.
 */
open class BaseDBHelper(name: String?,version: Int)
    : SQLiteOpenHelper(mContext,name,null,version) {
    override fun onCreate(p0: SQLiteDatabase?) {
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    companion object {
         @SuppressLint("StaticFieldLeak")
         lateinit var mContext : Context
        val SECRET_KEY = MD5Util.md5Encode("${Build.DEVICE}${Build.BRAND}${Build.PRODUCT}")

        fun initDataBase(context : Context){
            mContext = context
            SQLiteDatabase.loadLibs(mContext)
        }
    }


}