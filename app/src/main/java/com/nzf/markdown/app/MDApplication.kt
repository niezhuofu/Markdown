package com.nzf.markdown.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.example.baselib.database.BaseDBHelper
import com.example.baselib.database.MkDBHelper

/**
 * Created by niezhuofu on 17-11-8.
 */
class MDApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun getContext(): Context? = mContext

        fun getResources() :Resources = mContext.resources
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        BaseDBHelper.initDataBase(mContext)

        MkDBHelper.getInstance().getWritableDatabase(BaseDBHelper.SECRET_KEY)
    }

}