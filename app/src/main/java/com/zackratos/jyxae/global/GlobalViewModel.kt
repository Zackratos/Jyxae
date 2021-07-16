package com.zackratos.jyxae.global

import android.util.Log
import androidx.lifecycle.*
import com.zackratos.jyxae.ext.filesDir
import com.zackratos.jyxae.tools.DomManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.dom4j.DocumentHelper
import org.dom4j.io.XMLWriter
import java.io.File

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/5  5:15 PM
 * @Describe :
 */
class GlobalViewModel : ViewModel() {

    private val liveData: MutableLiveData<Global> by lazy { MutableLiveData<Global>() }

    private var archiveGlobal: Global? = null
    
    fun observe(owner: LifecycleOwner, observer: Observer<Global>) {
        liveData.observe(owner, observer)
    }

    fun postLoadGlobalArchive() {
        viewModelScope.launch {
            val content = withContext(Dispatchers.IO) {
                Thread.sleep(2000)
                loadGlobalArchive()
            }
            liveData.value = content
        }
    }

    fun postLoadGlobalArchive2() {
        viewModelScope.launch {
            flow {
//                Thread.sleep(2000)
                emit(loadGlobalArchive())
            }.flowOn(Dispatchers.IO)
                .collect {
                    archiveGlobal = it
                    liveData.value = it
                }
        }
    }

    fun save() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                saveGlobalArchive()
            }
        }
    }

    fun save(title: Global.TitleModel?, block: (() -> Unit)? = null) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                archiveGlobal?.titleModel = title
                saveGlobalArchive()
                delay(200)
            }
            block?.invoke()
        }
    }

    private fun loadGlobalArchive(): Global? {
        val file = File("${filesDir}/moddata.xml")
        if (!file.exists()) return null
        if (!file.isFile) return null
        val global = Global().apply {
            titleModel = Global.TitleModel()
        }
        val document = DomManager.instance.saxReader.read(file)
        val rootElement = document.rootElement
        val iterator = rootElement.elementIterator()
        while (iterator.hasNext()) {
            val element = iterator.next()
            if (element.name == "n") {
                global.titleModel?.titles?.add(Global.Title(element.stringValue))
            } else {
                global.other.xmls.add(element.asXML())
            }
            Log.d("parseGlobalArchive", element.asXML())
            val attributes = element.attributes()
            Log.d("parseGlobalArchive", "======获取属性值======")
            for (attribute in attributes) {
                Log.d("parseGlobalArchive", attribute.name)
                Log.d("parseGlobalArchive", attribute.value)
            }
            Log.d("parseGlobalArchive", "======遍历子节点======")
            val childIterator = element.elementIterator()
            while (childIterator.hasNext()) {
                val childElement = childIterator.next()
                Log.d("parseGlobalArchive", "节点名：${childElement.name}---节点值：${childElement.stringValue}")
            }
        }
        return global
    }

    private fun saveGlobalArchive() {
        if (archiveGlobal == null) return
        val doc = DocumentHelper.createDocument()
        val root = doc.addElement("globalsave")
        val titles = archiveGlobal?.titleModel?.titles
        titles?.filter {
            it.complete
        }?.forEach {
            root.addElement("n").addText(it.name)
        }
        val others = archiveGlobal?.other?.xmls
        others?.forEach {
            root.addText(it)
        }
        val file = File("${filesDir}/moddata.xml")
        if (file.exists()) {
            file.delete()
            file.createNewFile()
        }
        val writer = XMLWriter(file.outputStream())
        writer.isEscapeText = false
        writer.write(doc)

    }

}