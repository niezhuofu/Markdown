package com.chad.library.adapter.base.app

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by joseph on 2017/10/19.
 */

internal class ExceptionHandler private constructor() : Thread.UncaughtExceptionHandler {
    private var defaultHandler: Thread.UncaughtExceptionHandler? = null
    private var saveSpacePath: File? = null
    private var localErrorSave: File? = null
    private var context: Context? = null
    private val sb = StringBuilder()

    fun initConfig(context: Context) {
        this.context = context
        saveSpacePath = File(Environment.getExternalStorageDirectory().absolutePath, "/007")
        localErrorSave = File(saveSpacePath, "error.txt")
        if (!saveSpacePath!!.exists()) {
            saveSpacePath!!.mkdirs()
        }
        if (!localErrorSave!!.exists()) {
            try {
                localErrorSave!!.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        writeErrorToLocal(t, e)
        upLoadException(t)
    }


    /***
     * 上传异常信息到服务器
     * @param t1
     */
    private fun upLoadException(t1: Thread) {

    }

    private fun writeErrorToLocal(t: Thread, e: Throwable) {
        try {
            val fos = BufferedWriter(FileWriter(localErrorSave!!, true))
            val packageManager = context!!.packageManager
            val line = "\n----------------------------------------------------------------------------------------\n"
            sb.append(line)
            val packageInfo = packageManager.getPackageInfo(context!!.packageName, PackageManager.GET_ACTIVITIES)
            sb.append(SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis())) + "<---->" +
                    "包名::" + packageInfo.packageName + "<---->版本名::" + packageInfo.versionName + "<---->版本号::" + packageInfo.versionCode + "\n")
            sb.append("手机制造商::" + Build.MANUFACTURER + "\n")
            sb.append("手机型号::" + Build.MODEL + "\n")
            sb.append("CPU架构::" + Build.CPU_ABI + "\n")
            sb.append(e.toString() + "\n")
            val trace = e.stackTrace
            for (traceElement in trace)
                sb.append("\n\tat " + traceElement)
            sb.append("\n")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val suppressed = e.suppressed
                for (se in suppressed)
                    sb.append("\tat " + se.message)
            }
            fos.write(sb.toString())
            fos.close()


        } catch (e1: PackageManager.NameNotFoundException) {
            e1.printStackTrace()
            defaultHandler!!.uncaughtException(t, e1)
        } catch (e1: IOException) {
            e1.printStackTrace()
            defaultHandler!!.uncaughtException(t, e1)
        }

    }

    companion object {
        val instance = ExceptionHandler()
    }
}
