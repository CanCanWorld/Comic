package com.zrq.comic.view

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.zrq.comic.R

class Loading(
    context: Context, theme: Int, string: String
) : Dialog(context, theme) {
    var tvLoading: TextView
    var ivLoading: ImageView

    constructor(context: Context) : this(context, R.style.loading_dialog, "玩命加载中...")
    constructor(context: Context, string: String) : this(context, R.style.loading_dialog, string) {}

    init {
        setCanceledOnTouchOutside(true)//点击其他区域时   true  关闭弹窗  false  不关闭弹窗
        setOnCancelListener { dismiss() }
        setContentView(R.layout.loading_dialog)
        tvLoading = findViewById(R.id.tv_loading_tx)
        tvLoading.text = string
        ivLoading = findViewById(R.id.iv_loading)
        // 加载动画
        val hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
            context, R.anim.loading_animation
        )
        // 使用ImageView显示动画
        ivLoading.startAnimation(hyperspaceJumpAnimation)

        window!!.attributes.gravity = Gravity.CENTER//居中显示
        window!!.attributes.dimAmount = 0.5f//背景透明度  取值范围 0 ~ 1
    }

    override fun dismiss() {
        super.dismiss()
    }

}