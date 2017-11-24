package com.nzf.markdown.web

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.ViewTreeObserver
import com.nzf.markdown.view.WebMarkView

/**
 * Created by joseph on 2017/11/11.
 */

class ResultWebViewActivity : AppCompatActivity(),ViewTreeObserver.OnGlobalLayoutListener{

    private var isFirst : Boolean = true

    private lateinit var mWebView : WebMarkView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mWebView = WebMarkView(this)
        setContentView(mWebView)
        mWebView.addJavascriptInterface(WebMarkView.AndroidToast(this),"AndroidToast")
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mWebView.viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mWebView.viewTreeObserver.removeOnGlobalLayoutListener(this)
    }


    override fun onGlobalLayout() {
          if(isFirst){
              mWebView.data = WebMarkView.formatFileData(Environment.getExternalStorageDirectory().absolutePath + "/README.md")
              mWebView.loadDefault()
              isFirst = false
          }

    }


}
