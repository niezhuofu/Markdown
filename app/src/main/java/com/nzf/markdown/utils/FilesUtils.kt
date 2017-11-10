package com.nzf.markdown.utils

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import com.nzf.markdown.app.MDApplication
import com.nzf.markdown.bean.MDFileBean
import org.jetbrains.annotations.NotNull
import java.io.File

/**
 * Created by niezhuofu on 17-11-7.
 */
class FilesUtils {
    private var mContext: Context? = null
    var rootPath: String? = null
    var nowPath: String? = null

    val FILEDIR_EXTERNAL: String = "ExternalFileDir"
    val FILEDIR_INTERNAL: String = "InternalFileDir"

    constructor() {
        mContext = MDApplication.getContext()
    }

    companion object {
        val instance: FilesUtils
            get() = SingletonHolder.INSTANCE

        private object SingletonHolder {
            val INSTANCE = FilesUtils()
        }
    }

    fun showAllMDDir(path: String) {
        Log.i("showAllMDDir","start")
        var file: File = File(path)
        var filesList = ArrayList<MDFileBean>()

        var files: Array<File> = file.listFiles()

        for (f in files) {
            var bean: MDFileBean = MDFileBean()
            var fileName: String = f.name
            if (f.isFile) {
                var tmp: List<String> = fileName.split(".")
                for (t in tmp) {
                    Log.i("fileutils", t)
                }
            }
//            bean.fileName = f.name
//            bean.filePath = f.path
//            bean.fileLastTime = f.lastModified()
//            f.length()
        }

    }

    /*
    * 获取应用文件夹
    * @params path  存储类型(FILEDIR_EXTERNAL:外部存储,FILEDIR_INTERNAL内部存储)
    * @params type 需要获取的路径,
    * @return File
    */

    fun getFileDirectory(@NotNull path: String, type: String?): File? {
        var mdFileDir: File? = null
        when (path) {
            FILEDIR_EXTERNAL -> mdFileDir = getExternalFileDirectory(type)
            FILEDIR_INTERNAL -> mdFileDir = getInternalFileDirectory(type)
        }

        if (mdFileDir == null) {
            Log.i("getFileDirectory", "getExternalFileDirectory fail,the reason is sdCard nonexistence or sdCard mount fail !")
            mdFileDir = getInternalFileDirectory(type)
        }

        if (mdFileDir == null) {
            Log.e("getFileDirectory", "getFileDirectory fail ,the reason is mobile phone unknown exception !");
        } else {
            if (!mdFileDir.exists() && !mdFileDir.mkdirs()) {
                Log.e("getFileDirectory", "getFileDirectory fail ,the reason is make directory fail !");
            }
        }
        return mdFileDir
    }

    /*
    *type为空获取File文件夹根目录
    */
    fun getInternalFileDirectory(type: String?): File {
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

    /*
    *type为空获取File文件夹根目录
    */
    fun getExternalFileDirectory(type: String?): File? {
        var mdFileDir: File? = null
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (TextUtils.isEmpty(type)) {
                mdFileDir = mContext!!.getExternalFilesDir(null)
            } else {
                mdFileDir = mContext!!.getExternalFilesDir(type)
            }
        } else {
            Log.i("FileUtils", "getExternalDirectory fail ,the reason is sdCard nonexistence or sdCard mount fail !")
        }
        return mdFileDir
    }

}