package com.zackratos.jyxae.archive

import android.text.TextUtils
import androidx.lifecycle.*
import com.zackratos.jyxae.ext.filesDir
import com.zackratos.jyxae.tools.DomManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.dom4j.io.OutputFormat
import org.dom4j.io.XMLWriter
import java.io.File

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/9  6:07 PM
 * @email    : zhangwenchao@soulapp.cn
 * @Describe :
 */
class ArchiveViewModel: ViewModel() {

    private val liveData: MutableLiveData<Archive> by lazy { MutableLiveData<Archive>() }

    private var archive: Archive? = null

    fun observe(owner: LifecycleOwner, observer: Observer<Archive>) {
        liveData.observe(owner, observer)
    }

    fun postLoadArchive(filePath: String?) {
        viewModelScope.launch {
            archive = withContext(Dispatchers.IO) {
                loadArchive(filePath)
            }
            liveData.value = archive
        }
    }

    fun save(role: Archive.Role?, block: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                archive?.roleModel?.roles?.forEach {
                    if (it.name == role?.name) {
                        it.update(role)
                        return@forEach
                    }
                }
                saveArchive(archive)
                delay(200)
            }
            block.invoke()
        }
    }


    private fun loadArchive(filePath: String?): Archive? {
        if (TextUtils.isEmpty(filePath)) return null
        val file = File(filePath)
        if (!file.exists()) return null
        if (!file.isFile) return null
        val archive = Archive()
        val document = DomManager.instance.saxReader.read(file)
        val rootElement = document.rootElement
        val iterator = rootElement.elementIterator()
        while (iterator.hasNext()) {
            val element = iterator.next()
            when (element.name) {
                "Roles" -> archive.roleModel.roles.add(parseRole(element))
//                "k" -> {
//                    val attr = element.attributeValue("k")
//                    if (attr != "log") {
//                        archive.other.xmls.add(element.asXML())
//                    }
//                }
                else -> archive.other.xmls.add(element.asXML())
            }
//            archive.other.xmls.add(element.asXML())
        }
        return archive
    }

    private fun parseRole(element: Element): Archive.Role{
        return Archive.Role().apply {
            element.attributes().forEach {
                when (it.name) {
                    "key" -> key = it.value
                    "animation" -> animation = it.value
                    "name" -> name = it.value
                    "head" -> head = it.value
                    "maxhp" -> maxhp = it.value.toInt()
                    "maxmp" -> maxmp = it.value.toInt()
                    "wuxing" -> wuxing = it.value.toInt()
                    "shenfa" -> shenfa = it.value.toInt()
                    "bili" -> bili = it.value.toInt()
                    "gengu" -> gengu = it.value.toInt()
                    "fuyuan" -> fuyuan = it.value.toInt()
                    "dingli" -> dingli = it.value.toInt()
                    "quanzhang" -> quanzhang = it.value.toInt()
                    "jianfa" -> jianfa = it.value.toInt()
                    "daofa" -> daofa = it.value.toInt()
                    "qimen" -> qimen = it.value.toInt()
                    "currentSkillName" -> currentSkillName = it.value
                    "exp" -> exp = it.value.toInt()
                    "female" -> female = it.value.toInt()
                    "leftpoint" -> leftPoint = it.value.toInt()
                    "grow_template" -> growTemplate = it.value
                    "level" -> level = it.value.toInt()
                    "talent" -> talent = it.value
                }
            }
            element.elements().forEach {
                other.xmls.add(it.asXML())
            }
        }
    }

    private fun saveArchive(archive: Archive?) {
        if (archive == null) return
        val doc = DocumentHelper.createDocument()
        val root = doc.addElement("gamesave")
        root.addAttribute("name", "save")
        root.addAttribute("newbieTask", "")
        archive.roleModel.roles.forEach {
            val element = root.addElement("Roles")
                .addAttribute("key", it.key)
                .addAttribute("animation", it.animation)
                .addAttribute("name", it.name)
                .addAttribute("head", it.head)
                .addAttribute("maxhp", it.maxhp.toString())
                .addAttribute("maxmp", it.maxmp.toString())
                .addAttribute("wuxing", it.wuxing.toString())
                .addAttribute("shenfa", it.shenfa.toString())
                .addAttribute("bili", it.bili.toString())
                .addAttribute("gengu", it.gengu.toString())
                .addAttribute("fuyuan", it.fuyuan.toString())
                .addAttribute("dingli", it.dingli.toString())
                .addAttribute("quanzhang", it.quanzhang.toString())
                .addAttribute("jianfa", it.jianfa.toString())
                .addAttribute("daofa", it.daofa.toString())
                .addAttribute("qimen", it.qimen.toString())
                .addAttribute("currentSkillName", it.currentSkillName)
                .addAttribute("exp", it.exp.toString())
                .addAttribute("female", it.female.toString())
                .addAttribute("leftpoint", it.leftPoint.toString())
                .addAttribute("grow_template", it.growTemplate)
                .addAttribute("level", it.level.toString())
                .addAttribute("talent", it.talent)
            it.other.xmls.forEach { x ->
                element.addText(x)
            }
        }
        archive.other.xmls.forEach {
            root.addText(it)
        }
        val file = File("$filesDir/saves/save4")
        if (file.exists()) {
            file.delete()
            file.createNewFile()
        }
//        val format = OutputFormat()
//        format.isSuppressDeclaration = true
        val writer = XMLWriter(file.outputStream())
        writer.isEscapeText = false
        writer.write(doc)
    }

}