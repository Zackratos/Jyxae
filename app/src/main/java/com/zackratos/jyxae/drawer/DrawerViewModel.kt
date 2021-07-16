package com.zackratos.jyxae.drawer

import androidx.lifecycle.*
import com.zackratos.jyxae.ext.filesDir
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileFilter
import java.util.regex.Pattern

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/8  9:13 PM
 * @email    : zhangwenchao@soulapp.cn
 * @Describe :
 */
class DrawerViewModel: ViewModel() {

    private val liveData: MutableLiveData<List<File>> by lazy { MutableLiveData<List<File>>() }

    fun observe(owner: LifecycleOwner, observer: Observer<List<File>>) {
        liveData.observe(owner, observer)
    }

    fun postLoadArchiveFile() {
        viewModelScope.launch {
            val files = mutableListOf<File>()
            withContext(Dispatchers.IO) {
                val global = File("${filesDir}/moddata.xml")
                if (global.exists() && global.isFile) {
                    files.add(global)
                }
                val archiveDir = File("${filesDir}/saves")
                if (archiveDir.exists() && archiveDir.isDirectory) {
                    val archives = archiveDir.listFiles(FileFilter {
                        checkArchiveFile(it.name)
                    })
                    archives?.let { files.addAll(it) }
                }
            }
            liveData.value = files
        }
    }

    private val pattern by lazy { Pattern.compile("^save[0-9]{1,2}$") }

    private fun checkArchiveFile(name: String): Boolean {
        val matcher = pattern.matcher(name)
        return matcher.find()
    }


}