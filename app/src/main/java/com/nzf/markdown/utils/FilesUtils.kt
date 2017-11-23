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
import java.io.*

/**
 * Created by niezhuofu on 17-11-7.
 */
class FilesUtils {
    private var mContext: Context? = null
    var nowPath: String? = null

    val FILEDIR_EXTERNAL: String = "ExternalFileDir"
    val FILEDIR_INTERNAL: String = "InternalFileDir"


    constructor() {
        mContext = MDApplication.getContext()
    }

    companion object {

        val FILE_MD: String = ".md"
        val FILE_HTML: String = ".html"

        val instance: FilesUtils
            get() = SingletonHolder.INSTANCE

        private object SingletonHolder {
            val INSTANCE = FilesUtils()
        }
    }

    //生成MD文件
    fun newMDFile(pathName: String): Boolean {
        var file: File = File(nowPath + pathName)
        var isCreate: Boolean = false

        if (file.exists()) {
            ToastUtils.showShort(R.string.file_exists)
            return isCreate
        }

        try {
            isCreate = file.createNewFile()
            return isCreate
        } catch (e: IOError) {
            ToastUtils.showShort(R.string.file_exists)
            return false
        }
    }

    // 生成文件夹
    fun mkFileDir(pathName: String): Boolean {
        var file: File = File(nowPath + pathName)
        var isCreate: Boolean = false

        if (file.exists()) {
            ToastUtils.showShort(R.string.file_exists)
            return isCreate
        }

        isCreate = file.mkdir()
        return isCreate
    }

    /**
     * 删除单个文件
     *
     * @param file
     *            要删除的文件对象
     * @return 文件删除成功则返回true
     */
    fun deleteFile(file: File): Boolean {
        var isDelete: Boolean = false

        if (file.exists()) {
            isDelete = file.delete()
            Log.i("FileUtils:", "file delete.")
            return isDelete
        } else {
            Log.i("FileUtils:", "file is null")
            ToastUtils.showShort(R.string.file_noexists)
            return isDelete
        }
    }


    /**
     * 删除文件夹及其包含的所有文件
     *
     * @param file
     * @return
     */
    fun deleteFolder(file: File): Boolean {
        var flag: Boolean = false
        val files: Array<File>? = file.listFiles()
        if (files != null && files.size >= 0)
        // 目录下存在文件列表
        {
            for (i in files.indices) {
                val f = files[i]
                if (f.isFile) {
                    // 删除子文件
                    flag = deleteFile(f)
                    if (flag == false) {
                        return flag
                    }
                } else {
                    // 删除子目录
                    flag = deleteFolder(f)
                    if (flag == false) {
                        return flag
                    }
                }
            }
        }
        flag = file.delete()
        return if (flag == false) {
            flag
        } else {
            true
        }
    }

    /**
     * 展示所有文件
     * @param path  需要展示的路径
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

    /**
     * 判断是否是MD文件
     * @param f  文件对象
     * @return ArrayList<MDFileBean> 当前路径MD文件集合
     */
    fun getMDFile(f: File): MDFileBean? {
        var bean: MDFileBean? = MDFileBean()
        var fileName: String = f.name

        bean!!.fileName = f.name
        bean!!.fileLastTime = f.lastModified()
        bean!!.fileSize = f.length()
        bean!!.filePath = f.path

        if (f.isFile) {
            var fileSuffix: String = getFileSuffix(fileName)


            when (fileSuffix) {
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

    /**
     * 复制文件
     *
     * @param srcPath 源文件绝对路径
     * @param targetDir 目标文件所在目录
     * @return boolean
     */
    fun copyFile(srcPath: String, targetDir: String): Boolean {
        var flag: Boolean = false
        var file: File = File(srcPath)

        if (!file.exists()) {
            ToastUtils.showShort(R.string.file_noexists)
            return flag
        }

        var fileName: String = srcPath.substring(srcPath.lastIndexOf(File.separator))
        var targetPath: String = targetDir + fileName

        if (targetPath.equals(srcPath)) {
            ToastUtils.showShort(R.string.file_exists)
            return flag
        }

        var targetFile: File = File(targetPath)
        if (targetFile.exists() && targetFile.isFile) {
            ToastUtils.showShort(R.string.file_exists)
            return flag
        }

        try {
            val fis = FileInputStream(srcPath)
            val fos = FileOutputStream(targetFile)
            val buf = ByteArray(1024)
            var c: Int
            do {
                c = fis.read(buf)
                fos.write(buf, 0, c)
            } while (c != -1)
            fis.close()
            fos.close()

            flag = true
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (flag) {
            ToastUtils.showShort(R.string.file_copy)
        }
        return flag
    }

    /**
     * 根据文件名获得文件的扩展名
     *
     * @param fileName 文件名
     * @return 文件扩展名
     */
    fun getFileSuffix(fileName: String): String {
        var index: Int = fileName.lastIndexOf(".")
        var fileSuffix: String = fileName.substring(index, fileName.length)
        return fileSuffix
    }

    /**
     * 获取应用文件夹
     * @param path  存储类型(FILEDIR_EXTERNAL:外部存储,FILEDIR_INTERNAL内部存储)
     * @param type 需要获取的路径,
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

    /**
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

    /**
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