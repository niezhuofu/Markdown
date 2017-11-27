package com.nzf.markdown.test.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.nzf.markdown.R
import com.nzf.markdown.test.activity.httpconnect.HttpUrlUtils

/**
 * Created by niezhuofu on 17-11-25.
 */
class TestActivity : AppCompatActivity() {
     private val TAG = "AppCompatActivity"

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_home)



        Thread(Runnable {

          HttpUrlUtils.Companion.HttpBuilder.builder()

//            run {
//                while(true){
//                    Log.i(TAG,"js")
//                }
//            }

//            kotlin.run {
//                while(true){
//                    Log.i(TAG,"js")
//                }
//            }
        }).start()



    }
}