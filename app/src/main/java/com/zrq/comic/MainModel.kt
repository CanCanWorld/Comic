package com.zrq.comic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zrq.comic.bean.Chapter
import com.zrq.comic.bean.Search

class MainModel : ViewModel() {
    val searchListCache = ArrayList<Search.DataDTO>()

    val chapterListCache = ArrayList<Chapter.DataDTO.ChapterListDTO>()

    var comic: Search.DataDTO? = null

    var chapterId = ""

    //设置全屏
    var setScreen: () -> Unit = {}
}