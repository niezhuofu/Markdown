package com.nzf.markdown.web

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import java.io.File

/**
 * Created by joseph on 2017/11/11.
 */

class WebMarkView : WebView{
    constructor(context : Context?) : this(context,null)

    constructor(context : Context?,attrs : AttributeSet?) : this(context,attrs,0)

    constructor(context : Context?,attrs: AttributeSet?,defStyle:Int):super(context,attrs,defStyle){
        initData()
    }

    private fun initData() {
        settings.defaultTextEncodingName = "UTF-8"
        settings.javaScriptEnabled = true

        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                this@WebMarkView.loadUrl(ConstantWeb.getLoadJs(data))
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                this@WebMarkView.loadUrl(ConstantWeb.getLoadJs(data))
                loadDefault()
                return true
            }
        }


        webChromeClient = object : WebChromeClient() {

        }


    }

    public var data : String = ""


    override fun onSizeChanged(w: Int, h: Int, ow: Int, oh: Int) {
        super.onSizeChanged(w, h, ow, oh)
        val changeY = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f,
                resources.displayMetrics).toInt()
        if (h > 0 && h - oh > changeY) {
            loadUrl(ConstantWeb.JS_KEYBOARD_UP)
        } else if (h > 0 && oh - h > changeY) {
            loadUrl(ConstantWeb.JS_KEYBOARD_DOWN)
        }
    }


    companion object {

        /**
         * 对读取到的源文件格式化
         */
        fun formatFileData(filename : String) : String{
            val sb = StringBuilder()
            File(filename).useLines { lines -> lines.forEach { sb.append(it).append("\\n")} }

            return sb.toString()
                    .replace("\"","\\"+"\"").
                    replace("\'","\\"+"\'").
                    replace("//","\\/\\/")
        }
    }


    fun loadDefault(){
        loadUrl("file:///android_asset/result.html")
    }


}
