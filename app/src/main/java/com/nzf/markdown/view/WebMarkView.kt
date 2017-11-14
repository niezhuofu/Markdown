package com.nzf.markdown.view

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.TypedValue
import android.webkit.*
import android.widget.Toast
import com.nzf.markdown.ui.fragment.BigViewerFragment
import com.nzf.markdown.web.ConstantWeb
import com.nzf.markdown.web.ResultWebViewActivity
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

    class AndroidToast(context: Context) {
        private var context : Context? = context

        @JavascriptInterface
        fun show(img : String?){
            if(context is ResultWebViewActivity){
                val bigViewer = BigViewerFragment()
                val bundle = Bundle()
                bundle.putString(IMG_PATH,img)
                bigViewer.arguments = bundle
                bigViewer.show((context as ResultWebViewActivity).fragmentManager,"jia")
            }
        }
    }



    private fun initData() {
        settings.defaultTextEncodingName = "UTF-8"
        settings.javaScriptEnabled = true

        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                checkThePage(view,url)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return true
            }
        }

        webChromeClient = object : WebChromeClient(){}

    }

    /**
     * 校验加载完成的页面,进行相应的js注入操作
     */
    private fun checkThePage(view : WebView?,url: String?) {
        if(ConstantWeb.BLANK_PAGE_CONTAINER.equals(url)){
            Toast.makeText(context,"URL:"+ url,Toast.LENGTH_SHORT).show()
            view!!.loadUrl(ConstantWeb.getLoadJs(data))  //加载md解析获得的数据
            view.loadUrl(ConstantWeb.JS_IMAGE_CLICK_LISTENER) //js给img添加点击监听
        }
    }

    var data : String = ""

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

        val IMG_PATH = "imagePath"

    }


    fun loadDefault(){
        loadUrl(ConstantWeb.BLANK_PAGE_CONTAINER)
    }

}
