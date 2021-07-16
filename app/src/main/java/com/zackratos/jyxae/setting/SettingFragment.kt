package com.zackratos.jyxae.setting

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.zackratos.jyxae.HomeViewModel
import com.zackratos.jyxae.R
import com.zackratos.jyxae.ViewBindingFragment
import com.zackratos.jyxae.databinding.FragmentSettingBinding
import com.zackratos.jyxae.ext.homeFragment
import com.zackratos.jyxae.ext.packageName
import com.zackratos.jyxae.ext.toast
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX

/**
 * @Author   : Zackratos
 * @Date     : 2021/7/15 0:38
 * @email    : 869649338@qq.com
 * @Describe :
 */
class SettingFragment: ViewBindingFragment<FragmentSettingBinding>() {

    companion object {
        fun newInstance() = SettingFragment()
    }

    private val homeViewModel: HomeViewModel? by lazy { homeFragment?.let { ViewModelProvider(it).get(HomeViewModel::class.java) } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UltimateBarX.with(this).colorRes(R.color.primary_material_dark).fitWindow(true).applyStatusBar()
        binding.run {
            packageName?.let {
                etPackage.setText(it)
                etPackage.setSelection(it.length)
            }
            fab.setOnClickListener {
//                packageName = etPackage.text.toString()
                homeViewModel?.postChangePackageName(etPackage.text.toString())
                toast("保存成功")
            }
        }
    }

    override fun onVisible() {
        super.onVisible()
        homeFragment?.run {
            title = "设置"
            titleAlpha = 1f
        }
    }

}