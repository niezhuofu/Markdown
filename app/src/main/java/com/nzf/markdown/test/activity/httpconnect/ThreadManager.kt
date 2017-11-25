package com.nzf.markdown.test.activity.httpconnect


import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by joseph on 2017/11/25.
 */
class ThreadManager {

    companion object {
        private var poolProxy : ThreadPoolProxy? = null

        fun getProxy() : ThreadPoolProxy{
            if(poolProxy == null){
                synchronized(ThreadManager :: class.java.simpleName){
                    if(poolProxy == null){
                        val processCount = Runtime.getRuntime().availableProcessors()
                        val maxAvailable = Math.max(processCount,8)
                        poolProxy = ThreadPoolProxy(processCount,maxAvailable,5)
                    }
                }
            }
            return poolProxy!!
        }


        //代理类
        class ThreadPoolProxy(
                private var corePoolSize: Int,  //核心线程数
                private var maximumPoolSize: Int, //最大线程数
                private var keepAliveTime: Long) {  //超出核心线程数的线程在执行完后保持alive时长

            private var threadPoolExecutor: ThreadPoolExecutor? = null     // 线程池

            fun execute(runnable: Runnable){
                if(threadPoolExecutor == null || threadPoolExecutor!!.isShutdown){
                     synchronized(ThreadManager :: class.java.simpleName){
                         if(threadPoolExecutor == null || threadPoolExecutor!!.isShutdown){
                              threadPoolExecutor = createExecutor()
                              threadPoolExecutor!!.allowCoreThreadTimeOut(false)
                         }
                     }
                }
                threadPoolExecutor!!.execute(runnable)
            }

            private fun createExecutor() : ThreadPoolExecutor = ThreadPoolExecutor(corePoolSize,
                  maximumPoolSize,keepAliveTime,TimeUnit.SECONDS,LinkedBlockingQueue<Runnable>(),
                  DefaultThreadFactory(Thread.NORM_PRIORITY, "markdown-pool"),
                  ThreadPoolExecutor.AbortPolicy())
        }

        //线程工厂
        private class DefaultThreadFactory(private var threadPriority : Int,
                                           threadNamePrefix : String): ThreadFactory{
            init {
                namePrefix = threadNamePrefix + poolNumber.getAndIncrement() + "-thread-"
                group = Thread.currentThread().threadGroup
            }

            companion object {
                private val poolNumber : AtomicInteger = AtomicInteger(1)
                private val threadNumber : AtomicInteger = AtomicInteger(1)
                private lateinit var group : ThreadGroup
                private lateinit var namePrefix : String
                private var threadPriority : Int = 0
            }

            override fun newThread(r: Runnable?): Thread {
                 val thread = Thread(group,r, namePrefix + threadNumber.getAndIncrement(),0)
                 if(thread.isDaemon){
                     thread.isDaemon = false
                 }
                thread.priority = threadPriority
                return thread
            }
        }
    }

}