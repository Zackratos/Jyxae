package com.zackratos.jyxae

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.zackratos.jyxae.ext.packageName

/**
 * @Author   : Zackratos
 * @Date     : 2021/7/17 1:34
 * @email    : 869649338@qq.com
 * @Describe :
 */
class HomeViewModel: ViewModel() {

    private val liveData: MutableLiveData<HomeModel> by lazy { MutableLiveData<HomeModel>() }

    fun observe(owner: LifecycleOwner, observer: Observer<HomeModel>) {
        liveData.observe(owner, observer)
    }

    fun postChangePackageName(pkg: String) {
        packageName = pkg
        liveData.value = HomeModel(pkg)
    }

}