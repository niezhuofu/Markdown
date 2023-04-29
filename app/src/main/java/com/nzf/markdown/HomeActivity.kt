package com.nzf.markdown

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nzf.markdown.bean.MDFileBean
import com.nzf.markdown.databinding.ActivityHomeBinding
import com.nzf.markdown.utils.FilesUtils

/**
 * Created by niezhuofu on 17-11-15.
 */
class HomeActivity : AppCompatActivity() {

    private lateinit var mHomeBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(mHomeBinding.root)
        initView()
    }

    private fun initView() {
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        mHomeBinding.rvHomeList.layoutManager = llm

        initAdapter()
    }

    private fun initAdapter() {
        val path = FilesUtils.getFileDirectory(FilesUtils.FILEDIR_EXTERNAL, null)
        val list : List<MDFileBean> = FilesUtils.showAllMDDir(path?.path ?: "")

    }
}