package com.nzf.markdown.test.activity.httpconnect

import android.util.Log
import java.io.BufferedOutputStream
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


/**
 * Created by joseph on 2017/11/25.
 */
class HttpUrlUtils {

    companion object {
        private val TAG = "HttpUrlUtils"
        private val TYPE_GET = 101
        private val TYPE_POST = 202

        fun doGet(httpBuilder : HttpBuilder,callback:HttpUrlCallBack) {
            val conn = URL(httpBuilder.getParams(TYPE_GET)).openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connectTimeout = 5000
            conn.readTimeout = 8000
            conn.useCaches = true
            conn.doInput = true
            conn.doOutput = true

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


        fun doPost(httpBuilder: HttpBuilder,callback:HttpUrlCallBack){
            val conn = URL(httpBuilder.getUrlLink()).openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.connectTimeout = 5000
            conn.readTimeout = 8000
            conn.doInput = true
            conn.doOutput = true

            httpBuilder.getHeaders(conn)


//            conn.addRequestProperty("Authorization","")
//            conn.addRequestProperty("Content-Type","application/x-www-form-urlencoded")
//            conn.addRequestProperty("Content-Language","zh-cn,zh;q=0.5")
//            conn.addRequestProperty("User-Agent",String.format("%s/%s (Linux; Android %s;%s;%s Build/%s)",
//                    R.string.app_name,BuildConfig.VERSION_NAME, Build.VERSION.RELEASE,Build.MANUFACTURER,Build.ID))


            val out = BufferedOutputStream(conn.outputStream)
            val writer = BufferedWriter(OutputStreamWriter(out, "UTF-8"))
            writer.write(httpBuilder.getParams(TYPE_POST))
            writer.flush()
            writer.close()
            out.close()
            conn.connect()

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


        class HttpBuilder{
            private var headers : HashMap<String,String>? = null
            private var params : HashMap<String,String>? = null
            private var urlLink : String? = null


            fun getUrlLink() : String = urlLink!!

            fun setUrlLink(urlLink : String):HttpBuilder{
                this.urlLink = urlLink
                return this
            }

           fun setHeaders(headers : HashMap<String,String>):HttpBuilder {
               this.headers = headers
               return this
           }

           fun getHeaders(conn : HttpURLConnection){
               for((key,value) in headers!!){
                   conn.setRequestProperty(key,value)
               }
           }

           fun setParams(params : HashMap<String,String>):HttpBuilder{
               this.params = params
               return this
           }

           fun getParams(requestType : Int) : String{
               when(requestType){
                   TYPE_GET->{            //--------------------GET---------------------
                       var isFirst = true

                       for ((k,v) in params!!){
                           if(isFirst){
                               urlLink = "$urlLink?$k=$v&"
                               isFirst = false
                           }else{
                               urlLink = "$urlLink$k=$v&"
                           }
                       }
                       urlLink = urlLink!!.substring(0,urlLink!!.length - 1)
                       return urlLink!!
                   }
                   TYPE_POST->{           //-------------------POST----------------------
                       for((k,v) in params!!){
                           urlLink = "$urlLink$k=$v&"
                       }
                       urlLink = urlLink!!.substring(0,urlLink!!.length - 1)
                       return urlLink!!
                   }
               }
               return ""
           }
        }


        fun downloadFile(){


        }


    }
}