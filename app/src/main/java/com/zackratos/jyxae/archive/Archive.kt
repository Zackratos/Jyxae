package com.zackratos.jyxae.archive

import android.os.Parcelable
import com.zackratos.jyxae.Other
import kotlinx.android.parcel.Parcelize

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/9  6:30 PM
 * @email    : zhangwenchao@soulapp.cn
 * @Describe :
 */
data class Archive(
    var roleModel: RoleModel = RoleModel(),
    var other: Other = Other()
) {
    data class RoleModel(val roles: MutableList<Role> = mutableListOf())

    @Parcelize
    data class Role(
        var key: String? = null,
        var animation: String? = null,
        var name: String? = null,
        var head: String? = null,
        var maxhp: Int = 0,
        var maxmp: Int = 0,
        var wuxing: Int = 0,
        var shenfa: Int = 0,
        var bili: Int = 0,
        var gengu: Int = 0,
        var fuyuan: Int = 0,
        var dingli: Int = 0,
        var quanzhang: Int = 0,
        var jianfa: Int = 0,
        var daofa: Int = 0,
        var qimen: Int = 0,
        var currentSkillName: String? = null,
        var exp: Int = 0,
        var female: Int = 0,
        // 未分配点数
        var leftPoint: Int = 0,
        var growTemplate: String? = null,
        var level: Int = 0,
        var talent: String? = null,
        var other: Other = Other()
    ): Parcelable {

        fun update(role: Role?) {
            role?.let {
                this.key = it.key
                this.animation = it.animation
                this.name = it.name
                this.head = it.head
                this.maxhp = it.maxhp
                this.maxmp = it.maxmp
                this.wuxing = it.wuxing
                this.shenfa = it.shenfa
                this.bili = it.bili
                this.gengu = it.gengu
                this.fuyuan = it.fuyuan
                this.dingli = it.dingli
                this.quanzhang = it.quanzhang
                this.jianfa = it.jianfa
                this.daofa = it.daofa
                this.qimen = it.qimen
                this.currentSkillName = it.currentSkillName
                this.exp = it.exp
                this.female = it.female
                this.leftPoint = it.leftPoint
                this.growTemplate = it.growTemplate
                this.level = it.level
                this.talent = it.talent
                this.other.update(it.other)
            }
        }

        fun updateAttr(role: Role?) {
            role?.let {
                this.wuxing = it.wuxing
                this.shenfa = it.shenfa
                this.bili = it.bili
                this.gengu = it.gengu
                this.fuyuan = it.fuyuan
                this.dingli = it.dingli
                this.quanzhang = it.quanzhang
                this.jianfa = it.jianfa
                this.daofa = it.daofa
                this.qimen = it.qimen
                this.leftPoint = it.leftPoint
            }
        }

    }

    @Parcelize
    data class AttrModel(val attrs: MutableList<Attr> = mutableListOf()): Parcelable

    @Parcelize
    data class Attr(var key: String? = null, var value: String? = null): Parcelable

}