package com.zrq.comic.ui

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ImageViewTarget
import com.zrq.comic.R
import com.zrq.comic.databinding.FragmentPicBinding

class PicFragment(
    private val position: Int,
    private val url: String
) : BaseFragment<FragmentPicBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentPicBinding {
        return FragmentPicBinding.inflate(inflater, container, false)
    }

    private lateinit var loadAnimation: Animation

    @SuppressLint("SetTextI18n")
    override fun initData() {
        loadAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.loading_animation)
        mBinding.apply {
            tvPage.text = (position + 1).toString()
            // 使用ImageView显示动画
            ivLoad.startAnimation(loadAnimation)
            loadPic()
        }
    }

    override fun initEvent() {
        mBinding.apply {
            ivRefresh.setOnClickListener {
                loadPic()
            }
        }
    }

    private fun loadPic() {
        mBinding.apply {
            Glide.with(requireActivity())
                .load(url)
                .into(object : ImageViewTarget<Drawable>(image) {
                    override fun onLoadStarted(placeholder: Drawable?) {
                        super.onLoadStarted(placeholder)
                        ivLoad.visibility = View.VISIBLE
                        ivLoad.startAnimation(loadAnimation)
                        Log.d(TAG, "onLoadStarted: ")
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        ivRefresh.visibility = View.VISIBLE
                    }

                    override fun setResource(resource: Drawable?) {
                        ivRefresh.visibility = View.GONE
                        ivLoad.clearAnimation()
                        ivLoad.visibility = View.GONE
                        Log.d(TAG, "setResource: ")
                        image.setImageDrawable(resource)
                    }

                })
        }
    }

    override fun onResume() {
        super.onResume()
        mainModel.setScreen()

    }

    companion object {
        const val TAG = "PicFragment"

        fun newInstance(position: Int, url: String) = PicFragment(position, url)
    }

}
