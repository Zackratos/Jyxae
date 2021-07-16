package com.zackratos.jyxae.archive

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zackratos.jyxae.BaseFragmentStateAdapter
import com.zackratos.jyxae.BlankFragment
import com.zackratos.jyxae.R
import com.zackratos.jyxae.ViewBindingFragment
import com.zackratos.jyxae.databinding.FragmentArchiveBinding
import com.zackratos.jyxae.ext.archiveTitle
import com.zackratos.jyxae.ext.homeFragment
import com.zackratos.jyxae.ext.requestExternalStorage
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX
import kotlin.math.absoluteValue

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/7  11:58 AM
 * @email    : zhangwenchao@soulapp.cn
 * @Describe :
 */
class ArchiveFragment: ViewBindingFragment<FragmentArchiveBinding>() {

    companion object {
        private const val FILE_PATH = "file_path"
        private const val FILE_NAME = "file_name"
        fun newInstance(filePath: String?, fileName: String?) =
                ArchiveFragment().apply {
                    arguments = Bundle().apply {
                        putString(FILE_PATH, filePath)
                        putString(FILE_NAME, fileName)
                    }
                }
    }

    private val viewModel: ArchiveViewModel by viewModels()

    private val adapter: PagerAdapter by lazy { PagerAdapter(this) }

    private val filePath: String? by lazy { arguments?.getString(FILE_PATH) }

    private val fileName: String? by lazy { arguments?.getString(FILE_NAME) }

    private val title by lazy { fileName?.archiveTitle }

    private val tabTitles by lazy { arrayOf("队友", "外功秘籍", "内功秘籍", "天赋书", "武器", "防具", "饰品", "消耗品", "特殊", "日志") }

    private val titleHeight by lazy { binding.toolbar.height }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UltimateBarX.with(this)
            .colorRes(R.color.primary_material_dark)
            .fitWindow(true)
            .applyStatusBar()
        binding.run {
            viewPager.offscreenPageLimit = this@ArchiveFragment.adapter.itemCount
            viewPager.adapter = this@ArchiveFragment.adapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()
            appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener {_, offset ->
                val alpha = 1 - (offset.absoluteValue.toFloat() / titleHeight.toFloat())
                homeFragment?.titleAlpha = alpha
            })
        }
        viewModel.observe(this, Observer {  })
        requestExternalStorage { viewModel.postLoadArchive(filePath) }
        lifecycle.addObserver(adapter)
    }

    override fun onVisible() {
        super.onVisible()
        homeFragment?.run {
            title = this@ArchiveFragment.title
            titleAlpha = 1f
        }
    }

    private class PagerAdapter(fragment: Fragment) : BaseFragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 10

        override fun newFragment(position: Int): Fragment {
            return when (position) {
                0 -> RoleFragment.newInstance()
                else -> BlankFragment.newInstance()
            }
        }

    }

}