package com.zackratos.jyxae.global

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import com.zackratos.jyxae.Other
import kotlinx.android.parcel.Parcelize

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/6  10:43 AM
 * @Describe :
 */
data class Global(
        var save: Int = 0,
        var lastSaveIndex: Int = 0,
        var totalKill: Int = 0,
        var yuanbao: Int = 0,
        var dead: Int = 0,
        var maxRound: Int = 0,
        var endCount: Int = 0,
        var zhenLongQiJu: Int = 0,
        var titleModel: TitleModel? = null,
        var other: Other = Other()
) {

    @JsonClass(generateAdapter = true)
    @Parcelize
    data class TitleModel(val titles: MutableList<Title> = arrayListOf()) : Parcelable

    data class Skill(
            var name: String,
            var level: Int
    )

//    @JsonClass(generateAdapter = false)
    @Parcelize
    data class Title(
            var name: String? = null,
            val desc: String? = null,
            var complete: Boolean = false
    ) : Parcelable

}