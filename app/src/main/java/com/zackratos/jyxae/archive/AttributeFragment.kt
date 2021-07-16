package com.zackratos.jyxae.archive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zackratos.jyxae.ViewBindingFragment
import com.zackratos.jyxae.databinding.ItemArchiveAttributeBinding
import com.zackratos.jyxae.databinding.RecyclerviewBinding
import com.zackratos.jyxae.ext.onPressHolder
import kotlinx.coroutines.CoroutineScope

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/15  1:11 PM
 * @email    : zhangwenchao@soulapp.cn
 * @Describe :
 */
class AttributeFragment: ViewBindingFragment<RecyclerviewBinding>() {

    companion object {
        private const val ROLE = "role"
        fun newInstance(role: Archive.Role?) =
            AttributeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ROLE, role)
                }
            }
    }

    val role: Archive.Role? by lazy { arguments?.getParcelable<Archive.Role>(ROLE) }

    private val attributes by lazy {
        mutableListOf(
            Attribute("拳掌", role?.quanzhang ?: 0),
            Attribute("剑法", role?.jianfa ?: 0),
            Attribute("刀法", role?.daofa ?: 0),
            Attribute("奇门", role?.qimen ?: 0),
            Attribute("臂力", role?.bili ?: 0),
            Attribute("身法", role?.shenfa ?: 0),
            Attribute("悟性", role?.wuxing ?: 0),
            Attribute("福缘", role?.fuyuan ?: 0),
            Attribute("根骨", role?.gengu ?: 0),
            Attribute("定力", role?.dingli ?: 0),
            Attribute("未分配", role?.leftPoint ?: 0)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = Adapter(lifecycleScope, attributes)
        }
    }

    fun saveAttr() {
        role?.quanzhang = attributes[0].value
        role?.jianfa = attributes[1].value
        role?.daofa = attributes[2].value
        role?.qimen = attributes[3].value
        role?.bili = attributes[4].value
        role?.shenfa = attributes[5].value
        role?.wuxing = attributes[6].value
        role?.fuyuan = attributes[7].value
        role?.gengu = attributes[8].value
        role?.dingli = attributes[9].value
        role?.leftPoint = attributes[10].value
    }


    private class Adapter(val scope: CoroutineScope, val attributes: List<Attribute>): RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemArchiveAttributeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val holder = ViewHolder(binding)
            holder.binding.run {
                ivMinus.setOnClickListener {
                    val attribute = attributes[holder.adapterPosition]
                    holder.minus(attribute)
                }
                ivPlus.setOnClickListener {
                    val attribute = attributes[holder.adapterPosition]
                    holder.plus(attribute)
                }
                ivMinus.onPressHolder(scope) {
                    val attribute = attributes[holder.adapterPosition]
                    holder.minus(attribute)
                }
                ivPlus.onPressHolder(scope) {
                    val attribute = attributes[holder.adapterPosition]
                    holder.plus(attribute)
                }
            }
            return holder
        }

        override fun getItemCount(): Int = attributes.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val attribute = attributes[position]
            holder.binding.run {
                tvTitle.text = attribute.title
                tvValue.text = attribute.value.toString()

            }
        }
    }

    private class ViewHolder(val binding: ItemArchiveAttributeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun minus(attribute: Attribute) {
            if (attribute.value <= 0) return
            attribute.value--
            binding.tvValue.text = attribute.value.toString()
        }

        fun plus(attribute: Attribute) {
            attribute.value++
            binding.tvValue.text = attribute.value.toString()
        }

    }

    private data class Attribute(var title: String, var value: Int)

}