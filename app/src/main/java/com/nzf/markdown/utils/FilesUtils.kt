package com.nzf.markdown.utils

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.nzf.markdown.R
import com.nzf.markdown.app.MDApplication
import com.nzf.markdown.bean.MDFileBean
import org.jetbrains.annotations.NotNull
import java.io.File

/**
 * Created by niezhuofu on 17-11-7.
 */
class FilesUtils {
    private var mContext: Context? = null
    var nowPath: String? = null

    val FILEDIR_EXTERNAL: String = "ExternalFileDir"
    val FILEDIR_INTERNAL: String = "InternalFileDir"

    val FILE_MD: String = "md"
    val FILE_HTML: String = "html"

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

    //生成MD文件
    fun newMDFile(pathName: String): Boolean {
        var file: File = File(nowPath + pathName)

        if (file.exists()) {
            ToastUtils.showShort(R.string.file_exists)
            return false
        }

        file.createNewFile()
        return true
    }

    // 生成文件夹
    fun mkFileDir(pathName: String): Boolean {
        var file: File = File(nowPath + pathName)

        if (file.exists()) {
            ToastUtils.showShort(R.string.file_exists)
            return false
        }

        if (file.mkdir()) {
            return true
        } else {
            return false
        }
    }

    /*
    * 展示所有文件
    * @params path  需要展示的路径
    * @return ArrayList<MDFileBean> 当前路径MD文件集合
    */
    fun showAllMDDir(path: String?): ArrayList<MDFileBean>? {
        var file: File = File(path)

        var filesList: java.util.ArrayList<MDFileBean>? = ArrayList<MDFileBean>()

        var files: Array<File> = file.listFiles()

        if (files.size != 0) {
            for (f in files) {
                var fileBean: MDFileBean? = getMDFile(f)
                if (fileBean != null)
                    filesList!!.add(fileBean)
            }
        }

        return filesList
    }


    val FILETYPE_DIR: Int = 0
    val FILETYPE_MD: Int = 1
    val FILETYPE_HTML: Int = 2

    fun getMDFile(f: File): MDFileBean? {
        var bean: MDFileBean? = MDFileBean()
        var fileName: String = f.name

        bean!!.fileName = f.name
        bean!!.fileLastTime = f.lastModified()
        bean!!.fileSize = f.length()
        bean!!.filePath = f.path

        if (f.isFile) {
            var tmp: List<String> = fileName.split(".")
            when (tmp[1]) {
                FILE_MD -> bean.fileType = FILETYPE_MD
                FILE_HTML -> bean.fileType = FILETYPE_HTML
                else -> bean = null
            }
        }

        if (f.isDirectory) {
            bean!!.fileType = FILETYPE_DIR
        }

        return bean
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
        nowPath = mdFileDir.path
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