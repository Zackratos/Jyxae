package com.zackratos.jyxae.tools

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/12  2:32 PM
 * @email    : zhangwenchao@soulapp.cn
 * @Describe :
 */
class MoshiManager private constructor() {

    init {
        Log.d("MoshiManager", "init")
    }


    companion object {
        val instance: MoshiManager
            get() = Holder.INSTANCE
    }

    object Holder {
        val INSTANCE = MoshiManager()
    }

    val moshi by lazy { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }

}