package com.zackratos.jyxae.global

import androidx.lifecycle.*
import com.zackratos.jyxae.ext.context
import com.zackratos.jyxae.tools.MoshiManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/6  10:39 PM
 * @Describe :
 */
class TitleViewModel: ViewModel() {

    private val liveData: MutableLiveData<Global.TitleModel> by lazy { MutableLiveData<Global.TitleModel>() }

    fun observe(owner: LifecycleOwner, observer: Observer<Global.TitleModel>) {
        liveData.observe(owner, observer)
    }

    fun postReadTitle() {
        viewModelScope.launch {
            val titleMode = withContext(Dispatchers.IO) {
                readGlobalTitle()
            }
            liveData.value = titleMode
        }
    }


    private fun readGlobalTitle(): Global.TitleModel? {
        val json = context.assets.open("title.json").bufferedReader().readText()
        val adapter = Global_TitleModelJsonAdapter(MoshiManager.instance.moshi)
        return adapter.fromJson(json)
    }

}