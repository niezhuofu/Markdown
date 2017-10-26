package com.chad.library.adapter.base.utils

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import java.io.File


/**
 * Created by Administrator on 2017/7/21 0021.
 */
class FileUtils() {
    var ROOT_PATH: File? = null

    companion object {
        private var mContext: Context? = null
    }

    constructor(context: Context) : this() {
        mContext = context
        ROOT_PATH = this.getCacheDirectory("")
        if (ROOT_PATH != null) {
            var path: String = ROOT_PATH!!.path
            Log.i("FileUtils : = " , path)
        }
    }

    /**
     * 扫描显示文件列表
     * @param path
     */
    fun showFileDir(path: String) {
        Log.i("FileUtils","扫描")
        var fileDir: File = File(path);
        var mFileName = ArrayList<String>()
        var mFilePath = ArrayList<String>()
        var files: Array<File> = fileDir.listFiles()

        //如果当前目录不是根目录
        if (!ROOT_PATH!!.path.equals(path)) {
            mFileName.add("@1");
            mFilePath.add(ROOT_PATH!!.path);
            mFileName.add("@2");
            mFilePath.add(fileDir.getParent());
        }
        //添加所有文件
        for (f in files) {
            Log.i("FileUtils : " , f.name)
            mFileName.add(f.name)
            mFilePath.add(f.path)
        }
    }

    fun newMDFile(name: String) {

    }

    fun newMDDir(type: String): Boolean {
        var mdFileDir: File = getCacheDirectory(type)
        var isDir: Boolean = false
        if (mdFileDir != null) {
            if (mdFileDir.exists()) {
                isDir = true
            } else {
                isDir = mdFileDir.mkdirs()
            }
        }
        return isDir
    }

    fun getCacheDirectory(type: String): File {
        var mdFileDir: File? = getExternalCacheDirectory(type)
        if (mdFileDir == null) {
            mdFileDir = getInternalCacheDirectory(type)
        }

        if (mdFileDir == null) {
            Log.e("getCacheDirectory", "getCacheDirectory fail ,the reason is mobile phone unknown exception !");
        } else {
            if (!mdFileDir.exists() && !mdFileDir.mkdirs()) {
                Log.e("getCacheDirectory", "getCacheDirectory fail ,the reason is make directory fail !");
            }
        }
        return mdFileDir
    }

    fun getInternalCacheDirectory(type: String): File {
        var mdFileDir: File? = null
        if (TextUtils.isEmpty(type)) {
            mdFileDir = mContext!!.filesDir
        } else {
            mdFileDir = File(mContext!!.filesDir, type)
        }

        if (!mdFileDir!!.exists() && !mdFileDir!!.mkdirs()) {
            Log.i("FileUtils",
                    "getInternalDirectory fail ,the reason is make directory fail !")
        }

        return mdFileDir
    }

    fun getExternalCacheDirectory(type: String): File? {
        var mdFileDir: File? = null
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (TextUtils.isEmpty(type)) {
                mdFileDir = mContext!!.getExternalFilesDir("1").parentFile
            } else {
                mdFileDir = mContext!!.getExternalFilesDir(type)
            }
//            //某些厂商手机可能需要自定义文件夹
//            if (mdFileDir == null) {
//                mdFileDir = File(Environment.getExternalStorageDirectory(), "Android/data/"
//                        + mContext!!.packageName + "/cache/" + type)
//            } else {
//                if (mdFileDir.exists() && !mdFileDir.mkdirs()) {
//                    Log.i("FileUtils", "getExternalDirectory fail ,the reason is make directory fail !")
//                }
//            }
        } else {
            Log.i("FileUtils", "getExternalDirectory fail ,the reason is sdCard nonexistence or sdCard mount fail !")
        }
        return mdFileDir
    }

}