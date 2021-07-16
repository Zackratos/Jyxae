package com.zackratos.jyxae.archive

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.zackratos.jyxae.BlankFragment
import com.zackratos.jyxae.R
import com.zackratos.jyxae.ViewBindingFragment
import com.zackratos.jyxae.databinding.FragmentRoleEditBinding
import com.zackratos.jyxae.ext.headUrl
import com.zackratos.jyxae.ext.onPressHolder
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/13  12:58 PM
 * @email    : zhangwenchao@soulapp.cn
 * @Describe :
 */
class RoleEditFragment: ViewBindingFragment<FragmentRoleEditBinding>() {

    companion object {

        private const val ROLE = "role"

        fun newInstance(role: Archive.Role?, saveBlock: ((Archive.Role?) -> Unit)? = null) =
                RoleEditFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ROLE, role)
                    }
                    this.saveBlock = saveBlock
                }
    }

    private val role: Archive.Role? by lazy { arguments?.getParcelable<Archive.Role>(ROLE) }

    private var saveBlock: ((Archive.Role?) -> Unit)? = null

    private var attributeFragment: AttributeFragment? = null

    private var equipFragment: BlankFragment? = null

    private var skillFragment: BlankFragment? = null

    private var talentFragment: BlankFragment? = null


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UltimateBarX.with(this).colorRes(R.color.primary_material_dark).fitWindow(true).applyStatusBar()
        initViewData()
        initViewEvent()
        binding.viewAttribute.performClick()
    }

    private fun initViewData() {
        binding.run {
            toolbar.title = role?.name
            ivHead.load(role?.headUrl) {
                error(R.drawable.xiaobing)
                transformations(CircleCropTransformation())
            }
            tvHp.text = role?.maxhp?.toString()
            tvMp.text = role?.maxmp?.toString()
            tvExp.text = role?.exp?.toString()
        }
    }

    private fun initViewEvent() {
        binding.run {
            root.setOnClickListener {}
            fab.setOnClickListener {
                attributeFragment?.saveAttr()
                role?.updateAttr(attributeFragment?.role)
                saveBlock?.invoke(role)
            }
            ivHpMinus.setOnClickListener {
                if (role == null) return@setOnClickListener
                if (role!!.maxhp <= 0) return@setOnClickListener
                role!!.maxhp--
                tvHp.text = role?.maxhp?.toString()
            }
            ivHpPlus.setOnClickListener {
                if (role == null) return@setOnClickListener
                role!!.maxhp++
                tvHp.text = role?.maxhp?.toString()
            }
            ivHpMinus.onPressHolder(lifecycleScope) {
                if (role == null) return@onPressHolder
                if (role!!.maxhp <= 0) return@onPressHolder
                role!!.maxhp--
                tvHp.text = role?.maxhp?.toString()
            }
            ivHpPlus.onPressHolder(lifecycleScope) {
                if (role == null) return@onPressHolder
                role!!.maxhp++
                tvHp.text = role?.maxhp?.toString()
            }
            ivMpPlus.setOnClickListener {
                if (role == null) return@setOnClickListener
                role!!.maxmp++
                tvMp.text = role?.maxmp?.toString()
            }
            ivMpMinus.setOnClickListener {
                if (role == null) return@setOnClickListener
                if (role!!.maxmp <= 0) return@setOnClickListener
                role!!.maxmp--
                tvMp.text = role?.maxmp?.toString()
            }
            ivMpPlus.onPressHolder(lifecycleScope) {
                if (role == null) return@onPressHolder
                role!!.maxmp++
                tvMp.text = role?.maxmp?.toString()
            }
            ivMpMinus.onPressHolder(lifecycleScope) {
                if (role == null) return@onPressHolder
                if (role!!.maxmp <= 0) return@onPressHolder
                role!!.maxmp--
                tvMp.text = role?.maxmp?.toString()
            }
            ivExpMinus.setOnClickListener {  }
            ivExpPlus.setOnClickListener {  }
            viewAttribute.setOnClickListener {
                selectTab(false)
                tvAttribute.isSelected = true
                showFragment(0)
            }
            viewEquip.setOnClickListener {
                selectTab(false)
                tvEquip.isSelected = true
                showFragment(1)
            }
            viewSkill.setOnClickListener {
                selectTab(false)
                tvSkill.isSelected = true
                showFragment(2)
            }
            viewTalent.setOnClickListener {
                selectTab(false)
                tvTalent.isSelected = true
                showFragment(3)
            }
        }
    }

    private fun selectTab(selected: Boolean) {
        binding.run {
            tvAttribute.isSelected = selected
            tvEquip.isSelected = selected
            tvSkill.isSelected = selected
            tvTalent.isSelected = selected
        }
    }

    private fun showFragment(position: Int) {
        val transaction = childFragmentManager.beginTransaction()
        when (position) {
            0 -> {
                if (attributeFragment == null) {
                    attributeFragment = AttributeFragment.newInstance(role)
                    transaction.add(R.id.flContainer, attributeFragment!!)
                }
                transaction.show(attributeFragment!!)
                        .reqHide(equipFragment)
                        .reqHide(skillFragment)
                        .reqHide(talentFragment)
                        .commit()
            }
            1 -> {
                if (equipFragment == null) {
                    equipFragment = BlankFragment.newInstance()
                    transaction.add(R.id.flContainer, equipFragment!!)
                }
                transaction.show(equipFragment!!)
                        .reqHide(attributeFragment)
                        .reqHide(skillFragment)
                        .reqHide(talentFragment)
                        .commit()
            }
            2 -> {
                if (skillFragment == null) {
                    skillFragment = BlankFragment.newInstance()
                    transaction.add(R.id.flContainer, skillFragment!!)
                }
                transaction.show(skillFragment!!)
                        .reqHide(attributeFragment)
                        .reqHide(equipFragment)
                        .reqHide(talentFragment)
                        .commit()
            }
            3 -> {
                if (talentFragment == null) {
                    talentFragment = BlankFragment.newInstance()
                    transaction.add(R.id.flContainer, talentFragment!!)
                }
                transaction.show(talentFragment!!)
                        .reqHide(attributeFragment)
                        .reqHide(equipFragment)
                        .reqHide(skillFragment)
                        .commit()
            }
        }
    }

    private fun FragmentTransaction.reqHide(fragment: Fragment?): FragmentTransaction {
        if (fragment != null && fragment.isAdded) {
            hide(fragment)
        }
        return this
    }


}