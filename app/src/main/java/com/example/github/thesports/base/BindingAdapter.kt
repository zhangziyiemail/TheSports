package com.example.github.thesports.base

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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



@BindingAdapter("bind:refreshlistener")
fun bindRefreshfLisener(swipeRefreshLayout: SwipeRefreshLayout, listener: SwipeRefreshLayout.OnRefreshListener) {
    return swipeRefreshLayout.setOnRefreshListener(listener)
}



