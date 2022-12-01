package com.zrq.comic.ui

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.zrq.comic.R
import com.zrq.comic.databinding.FragmentPicBinding
import com.zrq.comic.view.Loading

class PicFragment(
    private val position: Int,
    private val url: String
) : BaseFragment<FragmentPicBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentPicBinding {
        return FragmentPicBinding.inflate(inflater, container, false)
    }

    override fun initData() {
        mBinding.apply {
            tvPage.text = position.toString()
            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.loading_animation)
            // 使用ImageView显示动画
            ivLoad.startAnimation(animation)
            Glide.with(requireActivity())
                .load(url)
                .into(object : ImageViewTarget<Drawable>(image) {

                    override fun onLoadStarted(placeholder: Drawable?) {
                        super.onLoadStarted(placeholder)
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        super.onResourceReady(resource, transition)
                    }

                    override fun setResource(resource: Drawable?) {
                        ivLoad.visibility = View.GONE
                        image.setImageDrawable(resource)
                    }

                })
        }
    }

    override fun initEvent() {
    }

    companion object {
        const val TAG = "PicFragment"

        fun newInstance(position: Int, url: String) = PicFragment(position, url)
    }

}
