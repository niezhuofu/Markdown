package com.chad.library.adapter.base.app

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.chad.library.adapter.base.utils.FileUtils
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

/**
 * Created by Administrator on 2017/7/22 0022.
 */
class MyApplication : Application() {
    companion object {
        private var mContext: Context? = null
        private var fileUtils: FileUtils? = null
        private var isSD: Boolean? = null

        fun context(): Context? = mContext

        fun getRefWatcher(context: Context?): RefWatcher? {
            if (context == null) {
                return null
            }
            val application = context.applicationContext as MyApplication
            if(application.refWatcher == null){
                return null
            }
            return application.refWatcher
        }

    }



    private var refWatcher : RefWatcher? = null

    override fun onCreate() {
        super.onCreate()
        Log.i("application", "启动++++++++++++++++++")
        mContext = this
        fileUtils = FileUtils(this)

        refWatcher = LeakCanary.install(this@MyApplication)
        ExceptionHandler.instance.initConfig(mContext as MyApplication)
    }

    fun getAppContext(): Context = mContext!!

    fun getAppResources(): Resources = mContext!!.resources

    fun getFileUtils(): FileUtils = fileUtils!!

    fun getisSD():Boolean = true
}