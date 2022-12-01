package com.zrq.comic.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.zrq.comic.R
import com.zrq.comic.adapter.SearchAdapter
import com.zrq.comic.bean.Search
import com.zrq.comic.databinding.FragmentHomeBinding
import com.zrq.comic.interfaces.OnItemClickListener
import com.zrq.comic.interfaces.OnItemLongClickListener
import com.zrq.comic.util.Constants.BASE_URL
import com.zrq.comic.util.Constants.SEARCH
import com.zrq.comic.util.Util.httpGet

class HomeFragment : BaseFragment<FragmentHomeBinding>(), OnItemClickListener, OnItemLongClickListener {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater, container, false)
    }

    private val list = ArrayList<Search.DataDTO>()
    private lateinit var adapter: SearchAdapter

    override fun initData() {
        list.clear()
        list.addAll(mainModel.searchListCache)
        adapter = SearchAdapter(requireContext(), list, this, this)
        mBinding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun initEvent() {
        mBinding.apply {

            refreshLayout.setOnRefreshListener {
                loadSearch()
            }

            refreshLayout.setOnLoadMoreListener {
                refreshLayout.finishLoadMore()
            }

            btnSearch.setOnClickListener {
                loadSearch()
            }

            etSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    btnSearch.callOnClick()
                    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(requireActivity().window.decorView.windowToken, 0)
                }
                false
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadSearch() {
        val keyword = mBinding.etSearch.text.toString()
        if (keyword == "") return
        val url = "$BASE_URL$SEARCH/$keyword"

        httpGet(url) { success, msg ->
            if (success) {
                try {

                    val search = Gson().fromJson(msg, Search::class.java)
                    if (search?.data != null) {
                        if (search.data.size == 0) {
                            Toast.makeText(requireContext(), "空", Toast.LENGTH_SHORT).show()
                            return@httpGet
                        }
                        list.clear()
                        list.addAll(search.data)
                        mainModel.searchListCache.clear()
                        mainModel.searchListCache.addAll(list)
                        Handler(Looper.getMainLooper()).post {
                            adapter.notifyDataSetChanged()
                            mBinding.refreshLayout.finishRefresh()
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "接口返回数据无效", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onItemClick(view: View, position: Int) {
        mainModel.comic = list[position]
        Navigation.findNavController(requireActivity(), R.id.fragment_container)
            .navigate(R.id.chapterFragment)
    }

    override fun onItemLongClick(view: View, position: Int) {

    }

    companion object {
        const val TAG = "HomeFragment"
    }
}
