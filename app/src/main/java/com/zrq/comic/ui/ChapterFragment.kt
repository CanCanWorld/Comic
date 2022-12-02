package com.zrq.comic.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.zrq.comic.R
import com.zrq.comic.adapter.ChapterAdapter
import com.zrq.comic.bean.Chapter
import com.zrq.comic.bean.Search
import com.zrq.comic.databinding.FragmentChapterBinding
import com.zrq.comic.interfaces.OnItemClickListener
import com.zrq.comic.util.Constants.BASE_URL
import com.zrq.comic.util.Constants.COMIC_CHAPTER
import com.zrq.comic.util.Util.httpGet

class ChapterFragment : BaseFragment<FragmentChapterBinding>(), OnItemClickListener {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChapterBinding {
        return FragmentChapterBinding.inflate(layoutInflater, container, false)
    }

    private var comicId = ""
    private val list = ArrayList<Chapter.DataDTO.ChapterListDTO>()
    private lateinit var adapter: ChapterAdapter
    private var comic: Search.DataDTO? = null
    private var upSort = true

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {
        comic = mainModel.comic
        comicId = comic?.comicId ?: ""
        adapter = ChapterAdapter(requireContext(), list, this)
        mBinding.apply {
            tvTitle.title = comic?.title
            rvChapter.adapter = adapter
            rvChapter.layoutManager = LinearLayoutManager(requireContext())
            Glide.with(this@ChapterFragment).load(comic?.cover).into(ivHead)
        }
        if (mainModel.chapterListCache.size != 0) {
            list.clear()
            list.addAll(mainModel.chapterListCache)
            adapter.notifyDataSetChanged()
        } else {
            loadChapter()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadChapter() {
        if (comicId == "") return
        val url = "$BASE_URL$COMIC_CHAPTER/$comicId"
        httpGet(url) { success, msg ->
            if (success) {
                val chapter = Gson().fromJson(msg, Chapter::class.java)
                val data = chapter?.data?.chapterList
                if (data != null) {
                    if (chapter.data.chapterList.size == 0) {
                        Toast.makeText(requireContext(), "空", Toast.LENGTH_SHORT).show()
                        return@httpGet
                    }
                    list.clear()
                    list.addAll(data)
                    Handler(Looper.getMainLooper()).post {
                        adapter.notifyDataSetChanged()
                    }
                }
            } else {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initEvent() {
        mBinding.apply {
            btnSort.setOnClickListener {
                if (upSort) {
                    upSort = false
                    ivSort.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24)
                    tvSort.text = "倒序"
                } else {
                    upSort = true
                    ivSort.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
                    tvSort.text = "正序"
                }
                list.reverse()
                adapter.notifyDataSetChanged()
            }

            rvChapter.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                @RequiresApi(Build.VERSION_CODES.P)
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 40) {
                        mainModel.setScreen()
                    }
                }
            })
        }
    }

    override fun onItemClick(view: View, position: Int) {
        mainModel.chapterId = list[position].chapterId
        mainModel.chapterListCache.clear()
        mainModel.chapterListCache.addAll(list)
        Navigation.findNavController(requireActivity(), R.id.fragment_container)
            .navigate(R.id.contentFragment)
    }
}
