package com.example.github.thesports.widget

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.github.thesports.R

/**
 *   Created by Lee Zhang on 10/25/20 13:38
 **/
class CustomDialog protected constructor(context: Context, theme: Int, string: String) :
    Dialog(context, theme) {

    var tvLoadingTx: TextView
    var ivLoading: ImageView

    constructor(context: Context) : this(context, R.style.loading_dialog, "loading...") {

    }

    constructor(context: Context, string: String) : this(context, R.style.loading_dialog, string) {}

    init {
        setCanceledOnTouchOutside(true)
        setOnCancelListener { dismiss() }
        setContentView(R.layout.loading_dialog)
        tvLoadingTx = findViewById(R.id.tv_loading_tx)
        tvLoadingTx.text = string
        ivLoading = findViewById(R.id.iv_loading)
        val hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
            context, R.anim.loading_animation
        )
        ivLoading.startAnimation(hyperspaceJumpAnimation)
        window!!.attributes.gravity = Gravity.CENTER
        window!!.attributes.dimAmount = 0.5f
    }


    override fun dismiss() {
        super.dismiss()
    }


}
