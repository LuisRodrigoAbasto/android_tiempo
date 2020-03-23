package com.luis.rodrigo.abasto.android.mianuncio.mianuncio.utils

import com.luis.rodrigo.abasto.android.mianuncio.mianuncio.BuildConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy


class L {
    private var NAME = "MI_ANUNCIO"
    init {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            //.showThreadInfo(false)
            .methodCount(1)
            .methodOffset(6)
            //.logStrategy(customLog)
            .tag(NAME)
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy){
            override fun isLoggable(priority: Int, tag: String?):Boolean{
                return BuildConfig.DEBUG_MODE
            }
        })
    }

    companion object{
        private var instance: L? =null

        fun d(message: String){
            inciarLog()
            Logger.d(message)
        }
        fun v(message: String){
            inciarLog()
            Logger.v(message)
        }
        private fun inciarLog(){
            if(instance == null){
                instance = L()
            }
        }
    }
}