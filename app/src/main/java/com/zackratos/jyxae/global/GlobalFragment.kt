package com.zackratos.jyxae.global

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zackratos.jyxae.BaseFragmentStateAdapter
import com.zackratos.jyxae.BlankFragment
import com.zackratos.jyxae.Fab
import com.zackratos.jyxae.ViewBindingFragment
import com.zackratos.jyxae.databinding.FragmentGlobalBinding
import com.zackratos.jyxae.ext.homeFragment
import com.zackratos.jyxae.ext.requestExternalStorage
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX
import com.zackratos.ultimatebarx.ultimatebarx.statusBarHeight
import kotlinx.android.synthetic.main.fragment_global.*
import kotlin.math.absoluteValue

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/5  5:15 PM
 * @Describe :
 */
class GlobalFragment : ViewBindingFragment<FragmentGlobalBinding>() {

    companion object {

        private const val TITLE = "title"

        fun newInstance() = GlobalFragment()
    }

    private val viewModel: GlobalViewModel by viewModels()

    private val pagerAdapter by lazy { PagerAdapter(this) }

    private val tabTitles by lazy { arrayOf("称号", "剧情", "属性", "内功解锁", "外功解锁") }

    private var titleModel: Global.TitleModel? = null

    private var originTranslationY = 0f

    private val scrollHeight by lazy { binding.viewScroll.height }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        titleModel = arguments?.getParcelable(TITLE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UltimateBarX.with(this).transparent().applyStatusBar()
//        binding.tabs.addStatusBarTopPadding()
        binding.tabs.setPadding(0, statusBarHeight, 0, 0)
//        UltimateBarX.with(this).fitWindow(true).applyNavigationBar()
        originTranslationY = binding.flCover.translationY
        binding.viewPager.offscreenPageLimit = pagerAdapter.itemCount
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            binding.flCover.translationY = originTranslationY - verticalOffset
            val alpha = verticalOffset.absoluteValue.toFloat() / scrollHeight.toFloat()
            viewCover.alpha = alpha
            homeFragment?.titleAlpha = 1 - alpha
        })

        viewModel.observe(this, Observer {

        })

        binding.fab.setOnClickListener {
            val fragment = pagerAdapter.findFragment(binding.viewPager.currentItem)
            if (fragment is Fab) {
                fragment.onFabClick()
            }
        }

        requestExternalStorage { viewModel.postLoadGlobalArchive2() }
        lifecycle.addObserver(pagerAdapter)

    }

    override fun onVisible() {
        super.onVisible()
        homeFragment?.run {
            title = "全局存档"
            titleAlpha = 1f
        }
    }


    private class PagerAdapter(fragment: Fragment) : BaseFragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 5

        override fun newFragment(position: Int): Fragment {
            return when (position) {
                0 -> TitleFragment.newInstance()
                else -> BlankFragment.newInstance()
            }
        }
    }

}