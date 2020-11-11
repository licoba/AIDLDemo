package com.licoba.binderserver

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.util.*

class AidlService : Service() {
    private lateinit var catBinder: Binder
    private var timer = Timer()
    private var TAG = "AidlService"

    private var colors = arrayOf("红色", "黄色", "黑色")
    private var weights = doubleArrayOf(2.3, 3.1, 1.58) // 双精度数组
    private lateinit var color: String //lateint 代表延迟初始化
    private var weight: Double = 0.0
    // 继承Stub 也就是实现了ICat接口，并实现了IBndder接口

    inner class CatBinder : ICat.Stub() {
        override fun getColor(): String {
            // this表达式 https://www.kotlincn.net/docs/reference/this-expressions.html
            // 在内部类使用this，应该使用@符号，@表示作用域，下面这句话，意思是返回AidlService的当前对象
            return this@AidlService.color
        }

        override fun getWeight(): Double {
            return this@AidlService.weight

        }

    }

    override fun onCreate() {
        Log.e(TAG,"onCreate");
        super.onCreate()
        catBinder = CatBinder()
        timer.schedule(object : TimerTask() {
            override fun run() {
                //随机改变Service组件内的color和weight值
                val rand = (Math.random() * 3).toInt()
                color = colors[rand]
                weight = weights[rand]
            }
        }, 0, 800)
    }


    override fun onBind(intent: Intent?): IBinder? {
        // 绑定本地Service，直接返回cat Binder
        Log.e(TAG, "onBind")
        return catBinder

    }

}