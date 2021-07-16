package com.zackratos.jyxae.ext

import android.os.Environment
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.github.promeg.pinyinhelper.Pinyin
import com.zackratos.jyxae.HomeFragment
import com.zackratos.jyxae.MainFragment
import com.zackratos.jyxae.archive.Archive
import org.dom4j.DocumentHelper
import org.dom4j.io.XMLWriter
import java.io.File
import java.util.*

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/13  5:13 PM
 * @email    : zhangwenchao@soulapp.cn
 * @Describe :
 */
val filesDir: String
    get() {
        val rootFile = Environment.getExternalStorageDirectory()
        val rootPath = rootFile.absolutePath
        return "${rootPath}/Android/data/${packageName}/files/"
    }

var packageName: String? = null
    get() {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        field = sp.getString("package_name", "com.hanjiasongshu.jygame")
        return field
    }
    set(value) {
        field = value
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val edit = sp.edit()
        edit.putString("package_name", value).apply()
    }

val Fragment.mainFragment: MainFragment?
    get() {
        val parent = parentFragment
        if (parent is MainFragment) {
            return parent
        }
        val parent2 = parent?.parentFragment
        if (parent2 is MainFragment) {
            return parent2
        }
        val parent3 = parent2?.parentFragment
        if (parent3 is MainFragment) {
            return parent3
        }
        val parent4 = parent3?.parentFragment
        if (parent4 is MainFragment) {
            return parent4
        }
        val parent5 = parent4?.parentFragment
        if (parent5 is MainFragment) {
            return parent5
        }
        return null
    }

val Fragment.homeFragment: HomeFragment?
    get() {
        val parent = parentFragment
        if (parent is HomeFragment) {
            return parent
        }
        val parent2 = parent?.parentFragment
        if (parent2 is HomeFragment) {
            return parent2
        }
        val parent3 = parent2?.parentFragment
        if (parent3 is HomeFragment) {
            return parent3
        }
        return null
    }

val Archive.Role.headUrl: String
    get() {
        val head = head?.split(".")?.get(1)
        val headPy = Pinyin.toPinyin(head, "").toLowerCase(Locale.getDefault())
        return "https://gitee.com/zackratos/JyxRes/raw/master/Heads/${headPy}.png"
    }

val String.archiveTitle: String
    get() {
        val number = substring(4).toInt() + 1
        return "存档${number}"
    }

fun saveArchive(archive: Archive?) {
    if (archive == null) return
    val doc = DocumentHelper.createDocument()
    val root = doc.addElement("gamesave")
    root.addAttribute("name", "save")
    root.addAttribute("newbieTask", "")
    archive.other.xmls.forEach {
        root.addText(it)
    }
    val file = File("$filesDir/saves/save6")
    if (file.exists()) {
        file.delete()
        file.createNewFile()
    }
    val writer = XMLWriter(file.outputStream())
    writer.isEscapeText = false
    writer.write(doc)
}