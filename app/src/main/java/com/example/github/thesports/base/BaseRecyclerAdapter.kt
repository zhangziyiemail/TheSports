package com.example.github.thesports.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 *   Created by Lee Zhang on 10/20/20 15:18
 **/
abstract class BaseRecyclerAdapter<T>(var mData: MutableList<T>?):
    RecyclerView.Adapter<BaseViewHolder<ViewDataBinding>>(){

    protected var mSelectionPosition = -1
    private var itemListener: OnItemClickListener? = null
    private var itemLongClickListener : OnItemLongClickListener? =null

    fun setOnItemListener(listener: OnItemClickListener?){
        this.itemListener = listener
    }

    fun setOnItemLongListener(listener: OnItemLongClickListener?) {
        this.itemLongClickListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewDataBinding> {
        return BaseViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), getLayoutId(viewType), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ViewDataBinding>, position: Int) {
        val data :T = getItemData(position) ?: return
        setVariable(data, position, holder)
        holder.binding.executePendingBindings()// when data change , system will be refresh next frame. Using executePendingBindings() will be immediately
        holder.binding.root.setOnClickListener {
                v -> itemListener?.onItemClick(position, v)}
        holder.binding.root.setOnLongClickListener { v ->
            itemLongClickListener?.onItemLongClick(position, v)
            false
        }
    }
    override fun getItemCount(): Int = mData?.size ?: 0

    fun getAdapterData(): MutableList<T>? = mData

    fun updateSelectedPosition(position: Int) {
        this.mSelectionPosition = position
        notifyDataSetChanged()
    }

    /**
     *  item layout
     */
    abstract fun getLayoutId(viewType: Int): Int

    /**
     *   data[position]
     */
    fun getItemData(position: Int): T? = mData!![position]

    abstract fun setVariable(data: T, position: Int, holder: BaseViewHolder<ViewDataBinding>)


    fun removeItem(position: Int) {
        if (position in 0 until itemCount)
            mData?.let {
                it.removeAt(position)
                notifyItemRemoved(position)
                if (position != itemCount) {
                    notifyItemRangeChanged(position, itemCount - position)
                }
            }
    }
}