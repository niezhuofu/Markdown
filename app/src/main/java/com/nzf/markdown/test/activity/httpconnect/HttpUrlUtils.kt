package com.nzf.markdown.test.activity.httpconnect

import android.util.Log
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by joseph on 2017/11/25.
 */
class HttpUrlUtils {

    companion object {
        private val TAG = "HttpUrlUtils"

        fun doGet(urlLink : String,callback:HttpUrlCallBack) {
            val conn = URL(urlLink).openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connectTimeout = 5000
            conn.readTimeout = 8000
            if(conn.responseCode != 200){
                Log.e(TAG,"responseCode: ${conn.responseCode} responseMessage:${conn.responseMessage}")
                callback.error("responseCode: ${conn.responseCode} responseMessage:${conn.responseMessage}")
            }
            try {
                val data = conn.inputStream.bufferedReader(Charsets.UTF_8).readText()
                callback.success(data)
            }catch (e : Exception){
                e.printStackTrace()
            }finally {
                conn.disconnect()
            }
        }


        fun doPost(urlLink: String,httpBuilder: HttpBuilder,callback:HttpUrlCallBack){
            val conn = URL(urlLink).openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.connectTimeout = 5000
            conn.readTimeout = 8000
            conn.headerFields
            conn.content




        }


        class HttpBuilder{
           lateinit var headers : Map<String,String>
           lateinit var contents : Any

           fun setHeaders(headers : Map<String,String>):HttpBuilder {
               this.headers = headers
               return this
           }

           fun setContents(contents : Any):HttpBuilder{
               this.contents = contents
               return this
           }
        }

    }
}