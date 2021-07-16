package com.zackratos.jyxae

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.zackratos.jyxae.databinding.FramelayoutBinding

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/13  8:02 PM
 * @email    : zhangwenchao@soulapp.cn
 * @Describe :
 */
class MainFragment: ViewBindingFragment<FramelayoutBinding>() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val homeFragment by lazy { HomeFragment.newInstance() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction()
                .add(R.id.frameLayout, homeFragment)
                .commit()
    }

    fun add(fragment: Fragment) {
        childFragmentManager.beginTransaction()
                .addToBackStack(null)
                .add(R.id.frameLayout, fragment)
                .commit()
    }

    fun onBackPressed(): Boolean {
//        childFragmentManager.popBackStack()
        val pop = childFragmentManager.popBackStackImmediate()
        if (pop) {
            return true
        }
        return homeFragment.onBackPressed()
//        return childFragmentManager.popBackStackImmediate()
    }

}