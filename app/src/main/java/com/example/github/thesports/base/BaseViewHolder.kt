package com.example.github.thesports.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 *   Created by Lee Zhang on 10/20/20 15:41
 **/
open class BaseViewHolder<VB : ViewDataBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)