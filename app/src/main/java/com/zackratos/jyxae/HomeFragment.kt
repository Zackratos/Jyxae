package com.zackratos.jyxae

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.zackratos.jyxae.databinding.FragmentHomeBinding
import com.zackratos.jyxae.drawer.DrawerFragment
import com.zackratos.ultimatebarx.ultimatebarx.statusBarHeight

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/6  11:22 AM
 * @email    : zhangwenchao@soulapp.cn
 * @Describe :
 */
class HomeFragment: ViewBindingFragment<FragmentHomeBinding>() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val params = binding.toolbar.layoutParams as FrameLayout.LayoutParams
        params.setMargins(0, statusBarHeight, 0, 0)
        binding.toolbar.layoutParams = params
        binding.toolbar.setNavigationOnClickListener { binding.drawerLayout.openDrawer(Gravity.LEFT) }
        childFragmentManager.beginTransaction()
            .add(R.id.flDrawer, DrawerFragment.newInstance(::replace))
            .commit()

//        val toggle = ActionBarDrawerToggle(requireActivity(), binding.drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer)
    }

    var title: String? = null
        set(value) {
            field = value
            binding.toolbar.title = value
        }

    var titleAlpha: Float = 1f
        set(value) {
            field = value
            binding.toolbar.alpha = value
            if (value > 0) {
                if (binding.toolbar.visibility != View.VISIBLE) {
                    binding.toolbar.visibility = View.VISIBLE
                }
            } else {
                binding.toolbar.visibility = View.INVISIBLE
            }
        }

    private fun replace(fragment: Fragment) {
        binding.drawerLayout.closeDrawer(Gravity.LEFT)
        childFragmentManager.beginTransaction()
            .replace(R.id.flContainer, fragment)
            .commit()
    }

    fun onBackPressed(): Boolean {
        if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            return true
        }
        return false
    }

}