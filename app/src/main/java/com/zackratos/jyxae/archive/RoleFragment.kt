package com.zackratos.jyxae.archive

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zackratos.jyxae.R
import com.zackratos.jyxae.ViewBindingFragment
import com.zackratos.jyxae.databinding.ItemArchiveRoleBinding
import com.zackratos.jyxae.databinding.RecyclerviewBinding
import com.zackratos.jyxae.ext.*

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/9  3:36 PM
 * @email    : zhangwenchao@soulapp.cn
 * @Describe :
 */
class RoleFragment: ViewBindingFragment<RecyclerviewBinding>() {

    companion object {
        fun newInstance() = RoleFragment()
    }

    private val adapter: Adapter by lazy { Adapter(this, ::saveRole) }

    private val archiveViewModel: ArchiveViewModel by lazy { ViewModelProvider(requireParentFragment()).get(ArchiveViewModel::class.java) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            recyclerView.run {
                layoutManager = GridLayoutManager(requireContext(), 4)
                adapter = this@RoleFragment.adapter
            }
        }
        archiveViewModel.observe(this, Observer {
            adapter.roleModel = it?.roleModel
            adapter.notifyDataSetChanged()
        })

    }

    private fun saveRole(role: Archive.Role?) {
        val dialog = ProgressDialog.show(requireContext(), null, "正在保存")
        archiveViewModel.save(role) {
            dialog.dismiss()
            toast("保存成功，重新读档生效")
        }
    }

    private class Adapter(private val fragment: Fragment, val saveRole: (Archive.Role?) -> Unit): RecyclerView.Adapter<ViewHolder>() {

        var roleModel: Archive.RoleModel? = null

        private val itemSize by lazy { (screenWidth - 8 * 8.dp) / 4 }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemArchiveRoleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val params = binding.ivHead.layoutParams as LinearLayout.LayoutParams
            params.height = itemSize
            binding.ivHead.layoutParams = params
            val holder = ViewHolder(binding)
            holder.itemView.setOnClickListener {
                val position = holder.adapterPosition
                val role = roleModel?.roles?.get(position)
                fragment.mainFragment?.add(RoleEditFragment.newInstance(role, saveRole))
            }
            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val role = roleModel?.roles?.get(position)
            holder.binding.ivHead.load(role?.headUrl) {
                error(R.drawable.xiaobing)
            }
            holder.binding.tvName.text = role?.name

        }

        override fun getItemCount(): Int = roleModel?.roles?.size ?: 0
    }

    private class ViewHolder(val binding: ItemArchiveRoleBinding) : RecyclerView.ViewHolder(binding.root)

}