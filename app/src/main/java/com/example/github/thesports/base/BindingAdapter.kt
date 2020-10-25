package com.example.github.thesports.base

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.github.thesports.utils.LogUtils

/**
 *   Created by Lee Zhang on 10/21/20 19:39
 **/


@BindingAdapter("bind:listItemClick")
fun bindRecyclerItemClick(recyclerView: RecyclerView, listener: OnItemClickListener) {
    val adapter = recyclerView.adapter
    if (adapter == null || adapter !is BaseRecyclerAdapter<*>) return
    adapter.setOnItemListener(listener)
}

