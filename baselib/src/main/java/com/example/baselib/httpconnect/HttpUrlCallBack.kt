package com.example.baselib.httpconnect

/**
 * Created by joseph on 2017/11/25.
 */
interface HttpUrlCallBack {
    fun success(data : String?)

    fun error(error : String?)
}