package com.zrq.comic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zrq.comic.bean.Chapter
import com.zrq.comic.databinding.ItemChapterBinding
import com.zrq.comic.interfaces.OnItemClickListener

class ChapterAdapter(
    private val context: Context,
    private val list: List<Chapter.DataDTO.ChapterListDTO>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<VH<ItemChapterBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemChapterBinding> {
        val mBinding = ItemChapterBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(mBinding)
    }

    override fun onBindViewHolder(holder: VH<ItemChapterBinding>, position: Int) {
        val data = list[position]
        holder.binding.apply {
            tvNumber.text = position.toString()
            tvTitle.text = data.title
            root.setOnClickListener {
                onItemClickListener.onItemClick(it, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}