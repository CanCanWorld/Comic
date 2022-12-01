package com.zrq.comic.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zrq.comic.ui.PicFragment

class PicAdapter(
    private val fragmentActivity: FragmentActivity,
    private val list: ArrayList<String>,
): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return PicFragment.newInstance(position ,list[position])
    }
}