package com.nzf.markdown.web

/**
 * Created by joseph on 2017/11/11.
 */

object ConstantWeb {
    val JS_KEYBOARD_DOWN = "window.onKeyboardDown && window.onKeyboardDown"
    val JS_KEYBOARD_UP = "window.onKeyboardUp && window.onKeyboardUp"

    fun getLoadJs(data : String) : String{
        return "javascript:marked(\'${data}\')"

    }
}
