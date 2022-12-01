package com.zrq.comic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zrq.comic.bean.Search

class MainModel : ViewModel() {
    val searchListCache = ArrayList<Search.DataDTO>()
    var comic : Search.DataDTO? = null
    var chapterId = ""
}