package com.nzf.markdown.web

/**
 * Created by joseph on 2017/11/11.
 */

object ConstantWeb {
    val JS_KEYBOARD_DOWN = "window.onKeyboardDown && window.onKeyboardDown"
    val JS_KEYBOARD_UP = "window.onKeyboardUp && window.onKeyboardUp"

    val JS_IMAGE_CLICK_LISTENER = ("javascript:(function(){" +
            "var objs = document.getElementsByTagName(\"img\"); " +
            "for(var i=0;i<objs.length;i++)  " +
            "{"
            + "    objs[i].onclick=function()  " +
            "    {  "
            + "        window.AndroidToast.show(this.src);  " +
            "    }  " +
            "}" +
            "})()")


    fun getLoadJs(data : String) : String{
        return "javascript:marked(\'${data}\')"

    }
}
