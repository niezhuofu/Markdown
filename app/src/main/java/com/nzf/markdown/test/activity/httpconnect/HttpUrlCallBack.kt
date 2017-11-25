package com.nzf.markdown.test.activity.httpconnect

/**
 * Created by joseph on 2017/11/25.
 */
interface HttpUrlCallBack {
    fun success(data : String?)

    fun error(error : String?)
}