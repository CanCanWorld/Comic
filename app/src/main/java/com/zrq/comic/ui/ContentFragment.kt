package com.zrq.comic.ui

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.zrq.comic.adapter.PicAdapter
import com.zrq.comic.bean.Content
import com.zrq.comic.databinding.FragmentContentBinding
import com.zrq.comic.util.Constants.BASE_URL
import com.zrq.comic.util.Constants.COMIC_CONTENT
import com.zrq.comic.util.Util.httpGet

class ContentFragment : BaseFragment<FragmentContentBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentContentBinding {
        return FragmentContentBinding.inflate(inflater, container, false)
    }

    private var chapterId = ""
    private val list = ArrayList<String>()
    private lateinit var adapter: PicAdapter

    override fun initData() {
        chapterId = mainModel.chapterId
        adapter = PicAdapter(requireActivity(), list)
        mBinding.apply {
            viewPager.adapter = adapter
            viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            viewPager.offscreenPageLimit = 4
        }
        loadContent()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadContent() {
        val url = "$BASE_URL$COMIC_CONTENT/$chapterId"
        httpGet(url) { success, msg ->
            if (success) {
                val content = Gson().fromJson(msg, Content::class.java)
                if (content?.data != null) {
                    if (content.data.size == 0) {
                        Toast.makeText(requireContext(), "空", Toast.LENGTH_SHORT).show()
                        return@httpGet
                    }
                    list.clear()
                    list.addAll(content.data)
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

    companion object {
        const val TAG = "ContentFragment"
    }
}
