package com.zrq.comic.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zrq.comic.bean.Search
import com.zrq.comic.databinding.ItemSearchBinding
import com.zrq.comic.interfaces.OnItemClickListener
import com.zrq.comic.interfaces.OnItemLongClickListener

class SearchAdapter(
    private val context: Context,
    private var list: ArrayList<Search.DataDTO>,
    private val onItemClickListener: OnItemClickListener,
    private val onItemLongClickListener: OnItemLongClickListener,
) : RecyclerView.Adapter<VH<ItemSearchBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemSearchBinding> {
        val mBinding = ItemSearchBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(mBinding)
    }

    override fun onBindViewHolder(holder: VH<ItemSearchBinding>, position: Int) {
        val data = list[position]
        holder.binding.apply {
            tvTitle.text = data.title
            tvAuthor.text = data.author
            tvDesc.text = data.descs
            tvTag.text = data.comicType
            Glide.with(context).load(data.cover).into(ivAlbum)
            root.setOnClickListener {
                onItemClickListener.onItemClick(it, position)
            }
            root.setOnLongClickListener {
                onItemLongClickListener.onItemLongClick(it, position)
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}