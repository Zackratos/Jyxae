package com.zackratos.jyxae.tools

import org.dom4j.io.SAXReader

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/12  2:59 PM
 * @email    : zhangwenchao@soulapp.cn
 * @Describe :
 */
class DomManager private constructor() {

    companion object {
        val instance: DomManager
            get() = Holder.INSTANCE
    }

    object Holder {
        val INSTANCE = DomManager()
    }

    val saxReader: SAXReader by lazy { SAXReader() }

}