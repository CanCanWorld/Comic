package com.zrq.comic.ui

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.zrq.comic.databinding.FragmentPicBinding
import com.zrq.comic.view.Loading

class PicFragment(
    private val position: Int,
    private val url: String
) : BaseFragment<FragmentPicBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentPicBinding {
        return FragmentPicBinding.inflate(inflater, container, false)
    }

    private val loading by lazy { Loading(requireContext()) }

    override fun initData() {
        mBinding.apply {
            tvPage.text = position.toString()
            Glide.with(requireActivity())
                .load(url)
                .into(object : ImageViewTarget<Drawable>(image) {

                    override fun onLoadStarted(placeholder: Drawable?) {
                        super.onLoadStarted(placeholder)
                        loading.show()
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        super.onResourceReady(resource, transition)
                    }

                    override fun setResource(resource: Drawable?) {
                        image.setImageDrawable(resource)
                        loading.dismiss()
                    }

                })
        }
    }

    override fun initEvent() {
    }

    companion object {
        fun newInstance(position: Int, url: String) = PicFragment(position, url)
    }

}
