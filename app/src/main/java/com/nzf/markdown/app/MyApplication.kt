package com.nzf.markdown.app

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.nzf.markdown.utils.FileUtils

/**
 * Created by Administrator on 2017/7/22 0022.
 */
class MyApplication : Application() {
    companion object {
        private var mContext: Context? = null
        private var fileUtils: FileUtils? = null
        private var isSD: Boolean? = null
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("application", "启动++++++++++++++++++")
        mContext = this
        fileUtils = FileUtils(this)
    }

    fun getAppContext(): Context = mContext!!

    fun getAppResources(): Resources = mContext!!.resources

    fun getFileUtils(): FileUtils = fileUtils!!

    fun getisSD():Boolean = true
}