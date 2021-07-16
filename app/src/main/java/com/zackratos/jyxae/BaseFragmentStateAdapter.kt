package com.zackratos.jyxae

import android.util.SparseArray
import androidx.core.util.set
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/9  6:14 PM
 * @email    : zhangwenchao@soulapp.cn
 * @Describe :
 */
abstract class BaseFragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment), LifecycleObserver {

    protected val fragments: SparseArray<Fragment> by lazy { SparseArray<Fragment>() }

    override fun createFragment(position: Int): Fragment {
        val fragment = fragments[position]
        if (fragment != null) {
            return fragment
        }
        return newFragment(position).apply {
            fragments[position] = this
        }
    }

    fun findFragment(position: Int) = createFragment(position)

    abstract fun newFragment(position: Int): Fragment

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner) {
        fragments.clear()
    }
}