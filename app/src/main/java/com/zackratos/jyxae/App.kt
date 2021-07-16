package com.zackratos.jyxae

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.tencent.bugly.crashreport.CrashReport

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/5  8:22 PM
 * @Describe :
 */
class App: Application() {

    companion object {
        lateinit var application: Application
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        CrashReport.initCrashReport(this, "dc6a419622", false)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}