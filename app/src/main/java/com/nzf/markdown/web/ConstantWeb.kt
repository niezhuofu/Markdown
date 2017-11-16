package com.nzf.markdown.web

/**
 * Created by joseph on 2017/11/11.
 */

object ConstantWeb {

    /**
     *  ################################## JS 方法相关 ########################################
     */

    val JS_KEYBOARD_DOWN = "window.onKeyboardDown && window.onKeyboardDown"
    val JS_KEYBOARD_UP = "window.onKeyboardUp && window.onKeyboardUp"

    val JS_IMAGE_CLICK_LISTENER = ("javascript:(function(){" +                 //给图片添加点击监听
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
        return "javascript:parseMarkdown(\'${data}\')"
    }


    /**
     *  ########################## 本地路由相关 #############################
     */

    val BLANK_PAGE_CONTAINER = "file:///android_asset/result.html"

}


//    JS和安卓互相调用以及Scheme协议完成外部链接跳转app指定页面