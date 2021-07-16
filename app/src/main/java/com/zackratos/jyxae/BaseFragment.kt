package com.zackratos.jyxae

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/5  5:05 PM
 * @Describe :
 */
open class BaseFragment(@LayoutRes contentLayoutId: Int = 0) : Fragment(contentLayoutId) {

    private var loaded = false

    var active = false

    override fun onResume() {
        super.onResume()
        if (!loaded) {
            lazyLoad()
        }
        loaded = true
        if (!isHidden) {
            onVisible()
        }
    }

    override fun onPause() {
        super.onPause()
        if (!isHidden) {
            onHint()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (isResumed) {
            when (hidden) {
                true -> onHint()
                else -> onVisible()
            }
        }
    }

    /**
     *  懒加载
     *  适用于 [androidx.viewpager2.widget.ViewPager2] 和 [androidx.viewpager.widget.ViewPager] 的 [androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT] 模式
     */
    protected open fun lazyLoad() {}

    /**
     *  当前 Fragment 可见
     *  适用于 [androidx.viewpager2.widget.ViewPager2] 和 [androidx.viewpager.widget.ViewPager] 的 [androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT] 模式
     *  包括当前 Fragment 所在的 Activity 可见，ViewPager 切换可见，在布局中调用 [androidx.fragment.app.FragmentTransaction.show] 可见
     */
    protected open fun onVisible() {
        active = true
    }

    /**
     *  当前 Fragment 不可见
     *  适用于 [androidx.viewpager2.widget.ViewPager2] 和 [androidx.viewpager.widget.ViewPager] 的 [androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT] 模式
     *  包括当前 Fragment 所在的 Activity 不可见，ViewPager 切换不可见，在布局中调用 [androidx.fragment.app.FragmentTransaction.hide] 不可见
     */
    protected open fun onHint() {
        active = false
    }

}