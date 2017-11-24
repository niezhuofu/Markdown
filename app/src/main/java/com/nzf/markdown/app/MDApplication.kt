package com.nzf.markdown.app

import android.app.Application
import android.content.Context
import android.content.res.Resources

/**
 * Created by niezhuofu on 17-11-8.
 */
class MDApplication : Application() {
    companion object {
        var mContext: Context? = null

        fun getContext(): Context? = mContext

        fun getResources() :Resources = mContext!!.resources
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }


}