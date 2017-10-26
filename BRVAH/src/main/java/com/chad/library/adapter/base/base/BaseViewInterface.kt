package com.chad.library.adapter.base.base

import android.os.Bundle
import android.support.annotation.LayoutRes

/**
 * Created by joseph on 2017/10/19.
 */

interface BaseViewInterface {
    /**
     * Activitiy的布局,必须重写
     *
     * @return 布局资源
     */
    @get:LayoutRes
    val layoutId: Int

    /**
     * onCreate之后调用,可以用来初始化view
     */
    fun onCreateAfter(savedInstanceState: Bundle)

    /**
     * 界面渲染完毕，可在这里进行初始化工作，建议在这里启动线程进行初始化工作
     * 数据获取等操作
     */
    fun initData()


}
