package com.zackratos.jyxae.global

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zackratos.jyxae.Fab
import com.zackratos.jyxae.ViewBindingFragment
import com.zackratos.jyxae.databinding.ItemGlobalTitleBinding
import com.zackratos.jyxae.databinding.RecyclerviewBinding
import com.zackratos.jyxae.ext.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/6  7:26 PM
 * @email    : zhangwenchao@soulapp.cn
 * @Describe :
 */
class TitleFragment: ViewBindingFragment<RecyclerviewBinding>(), Fab {

    companion object {

        private const val TITLE = "title"

        fun newInstance(titleModel: Global.TitleModel? = null) =
            TitleFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(TITLE, titleModel)
                }
            }
    }

    private val globalViewModel: GlobalViewModel by lazy { ViewModelProvider(requireParentFragment()).get(GlobalViewModel::class.java) }

    private val viewModel: TitleViewModel by viewModels()

    private val adapter by lazy { Adapter() }

    private var archiveTitle: Global.TitleModel? = null

    private var allTitle: Global.TitleModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@TitleFragment.adapter
        }
        globalViewModel.observe(this, Observer {
            archiveTitle = it?.titleModel
            addTitleData()
        })
        viewModel.observe(this, Observer {
            allTitle = it
            addTitleData()

        })
        viewModel.postReadTitle()
    }

    override fun onFabClick() {
        val dialog = ProgressDialog.show(requireContext(), null, "正在保存")
        globalViewModel.save(allTitle) {
            dialog.dismiss()
            toast("保存成功，重启游戏生效")
        }
    }

    private fun addTitleData() {
        if (archiveTitle == null || allTitle == null) return
        lifecycleScope.launch {
            withContext(Dispatchers.Default) {
                for (t1 in allTitle?.titles!!) {
                    for (t2 in archiveTitle?.titles!!) {
                        if (t1.name == t2.name) {
                            t1.complete = true
                            break
                        }
                    }
                }
            }
            adapter.run {
                this.titleModel = allTitle
                notifyDataSetChanged()
            }
        }
    }

    private class Adapter(var titleModel: Global.TitleModel? = null): RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemGlobalTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val holder = ViewHolder(binding)
            holder.itemView.setOnClickListener {
                val position = holder.adapterPosition
                val title =  titleModel?.titles?.get(position)
                title?.let { it.complete = !it.complete }
                notifyItemChanged(position)
            }
            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val title =  titleModel?.titles?.get(position)
            holder.binding.run {
                tvTitle.run {
                    text = title?.name
                    isSelected = title?.complete ?: false
                }
                tvDesc.text = title?.desc
            }
        }

        override fun getItemCount(): Int = titleModel?.titles?.size ?: 0
    }

    private class ViewHolder(val binding: ItemGlobalTitleBinding) : RecyclerView.ViewHolder(binding.root) {

    }

}