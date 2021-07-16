package com.zackratos.jyxae.ext

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/13  11:50 AM
 * @email    : zhangwenchao@soulapp.cn
 * @Describe :
 */
val Int.dp: Int
    get() = (displayMetrics.density * this + 0.5f).toInt()