package com.zackratos.jyxae.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zackratos.jyxae.HomeViewModel
import com.zackratos.jyxae.ViewBindingFragment
import com.zackratos.jyxae.archive.ArchiveFragment
import com.zackratos.jyxae.databinding.FragmentDrawerBinding
import com.zackratos.jyxae.databinding.ItemDrawerArchiveBinding
import com.zackratos.jyxae.ext.archiveTitle
import com.zackratos.jyxae.ext.homeFragment
import com.zackratos.jyxae.ext.requestExternalStorage
import com.zackratos.jyxae.global.GlobalFragment
import com.zackratos.jyxae.setting.SettingFragment
import java.io.File

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/6  1:59 PM
 * @Describe :
 */
class DrawerFragment: ViewBindingFragment<FragmentDrawerBinding>() {

    companion object {
        fun newInstance(replaceBlock: ((Fragment) -> Unit)?) =
                DrawerFragment().apply {
                    this.replaceBlock = replaceBlock
                }
    }

    private val viewModel: DrawerViewModel by viewModels()

    private val homeViewModel: HomeViewModel? by lazy { homeFragment?.let { ViewModelProvider(it).get(HomeViewModel::class.java) } }

    private val files by lazy { mutableListOf<File>() }

    private val adapter: Adapter by lazy { Adapter(files, replaceBlock, ::settingSelectVisibility) }

    private var replaceBlock: ((Fragment) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            rvArchive.run {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = this@DrawerFragment.adapter
            }
            flSetting.setOnClickListener {
                if (adapter.lastSelectPosition == -1) return@setOnClickListener
                replaceBlock?.invoke(SettingFragment.newInstance())
                if (adapter.lastSelectPosition >= 0) {
                    adapter.selects[adapter.lastSelectPosition] = false
                    adapter.notifyItemChanged(adapter.lastSelectPosition)
                }
                settingSelectVisibility(View.VISIBLE)
                adapter.lastSelectPosition = -1
            }
        }
        viewModel.observe(this, Observer {
            files.clear()
            files.addAll(it)
            adapter.notifyDataSetChanged()
            adapter.selects.clear()
            it.forEach { _ -> adapter.selects.add(false) }
            if (files.isNotEmpty() && files[0].name == "moddata.xml") {
                adapter.selectArchive(0)
            }
        })
        homeViewModel?.observe(this, Observer {
            requestExternalStorage { viewModel.postLoadArchiveFile() }
        })
        requestExternalStorage { viewModel.postLoadArchiveFile() }
    }

    private fun settingSelectVisibility(visibility: Int) {
        binding.viewSettingSelect.visibility = visibility
    }

    private class Adapter(private val files: MutableList<File>,
                          val replaceBlock: ((Fragment) -> Unit)? = null,
                          val settingVisibilityBlock: ((Int) -> Unit)? = null): RecyclerView.Adapter<ViewHolder>() {

        val selects: MutableList<Boolean> = mutableListOf()
        var lastSelectPosition = -2

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemDrawerArchiveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val holder = ViewHolder(binding)
            holder.itemView.setOnClickListener {
                val position = holder.adapterPosition
                selectArchive(position)
            }
            return holder
        }

        override fun getItemCount(): Int = files.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.run {
                binding.tvTitle.text = when (position) {
                    0 -> "全局存档"
                    else -> files[position].name.archiveTitle
                }
                binding.viewSelect.visibility = if (selects[position]) View.VISIBLE else View.GONE
            }
        }

        fun selectArchive(position: Int) {
            if (position == lastSelectPosition) return
            val fragment = when (position) {
                0 -> GlobalFragment.newInstance()
                else -> {
                    val file = files[position]
                    ArchiveFragment.newInstance(file.path, file.name)
                }
            }
            replaceBlock?.invoke(fragment)
            if (lastSelectPosition >= 0) {
                selects[lastSelectPosition] = false
                notifyItemChanged(lastSelectPosition)
            }
            selects[position] = true
            notifyItemChanged(position)
            lastSelectPosition = position
            settingVisibilityBlock?.invoke(View.GONE)
        }
    }

    private class ViewHolder(val binding: ItemDrawerArchiveBinding) : RecyclerView.ViewHolder(binding.root)

}