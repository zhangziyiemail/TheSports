package com.example.github.thesports.base

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 *   Created by Lee Zhang on 10/21/20 19:39
 **/


@BindingAdapter("bind:recyclerscroll")
fun setMyScrollListener(rv:RecyclerView,listener: RecyclerView.OnScrollListener){
    rv.addOnScrollListener(listener)
}


