package com.zackratos.jyxae

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/6  10:43 AM
 * @Describe :
 */
@Parcelize
data class Other(var xmls: MutableList<String> = arrayListOf()): Parcelable {

    fun update(other: Other?) {
        if (this == other) return
        other?.let {
            this.xmls.clear()
            this.xmls.addAll(it.xmls)
        }
    }

}