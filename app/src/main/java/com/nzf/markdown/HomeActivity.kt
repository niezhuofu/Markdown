package com.nzf.markdown

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.nzf.markdown.bean.MDFileBean
import com.nzf.markdown.utils.FilesUtils
import kotlinx.android.synthetic.main.activity_home.*

/**
 * Created by niezhuofu on 17-11-15.
 */
class HomeActivity: AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initView()
    }

    fun initView(){
        var llm: LinearLayoutManager = LinearLayoutManager(this)
        llm.orientation = LinearLayout.VERTICAL
        rv_home_list.layoutManager = llm

        initAdapter()
        }

    fun initAdapter(){
        var fileUtils = FilesUtils.instance
        var path = fileUtils.getFileDirectory(fileUtils.FILEDIR_EXTERNAL,null)
        var list : List<MDFileBean>? = fileUtils.showAllMDDir(path!!.path)!!

    }
}