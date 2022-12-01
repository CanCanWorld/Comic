package com.zrq.comic.ui

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
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

    override fun initData() {
        comic = mainModel.comic
        comicId = comic?.comicId ?: ""
        adapter = ChapterAdapter(requireContext(), list, this)
        loadChapter()
        mBinding.apply {
            tvTitle.title = comic?.title
            rvChapter.adapter = adapter
            rvChapter.layoutManager = LinearLayoutManager(requireContext())
            Glide.with(this@ChapterFragment).load(comic?.cover).into(ivHead)
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

    override fun initEvent() {
    }

    override fun onItemClick(view: View, position: Int) {
        mainModel.chapterId = list[position].chapterId
        Navigation.findNavController(requireActivity(), R.id.fragment_container)
            .navigate(R.id.contentFragment)
    }
}
