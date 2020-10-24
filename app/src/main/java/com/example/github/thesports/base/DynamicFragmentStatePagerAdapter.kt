package com.example.github.thesports.base

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.PagerAdapter

/**
 *   Created by Lee Zhang on 10/23/20 01:01
 **/
abstract class DynamicFragmentStatePagerAdapter<T>(val mFragmentManager: FragmentManager):
    PagerAdapter(){

    private val TAG = "FragmentStatePagerAdapt"
    private val DEBUG = false
    private var mCurTransaction: FragmentTransaction? = null
    private val mSavedState = ArrayList<Fragment.SavedState?>()
    private var mItemInfos = ArrayList<ItemInfo<T>?>()
    protected var mCurrentPrimaryItem: Fragment? = null

    /**
     * Return the Fragment associated with a specified position.
     */
    abstract fun getItem(position: Int): Fragment

    protected fun getCachedItem(position: Int): Fragment? = if (mItemInfos.size > position) mItemInfos[position]?.fragment else null

    //根据位置获取数据需要加数组越界判断，外部数据移除后，调用notifyDataSetChanged
    //ViewPager通过getItemPosition来判断老数据位置是否更新的同时会通过老的postion来获取
    abstract fun getItemData(position: Int): T?

    abstract fun dataEquals(oldData: T?, newData: T?): Boolean

    abstract fun getDataPosition(data: T?): Int

    class ItemInfo<D>(var fragment: Fragment, var data: D?, var position: Int)


    override fun startUpdate(container: ViewGroup) {
        if (container.id == View.NO_ID) {
            throw IllegalStateException("ViewPager with adapter " + this
                    + " requires a view id")
        }
    }
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // If we already have this item instantiated, there is nothing
        // to do.  This can happen when we are restoring the entire pager
        // from its saved state, where the fragment manager has already
        // taken care of restoring the fragments we previously had instantiated.
        if (mItemInfos.size > position) {
            val ii = mItemInfos[position]
            ii?.let {
                //由于先调用notifyDataSetChanged，ViewPager的ItemInfo.postion发生改变后，可能会优先调用instantiateItem添加新的页面
                // 所以要做位置判断，如果不正确则更新数组后再返回
                if (it.position == position) {
                    if(!it.fragment.isAdded){
                        if (mCurTransaction == null) {
                            mCurTransaction = mFragmentManager.beginTransaction()
                        }
                        mCurTransaction?.add(container.id, it.fragment)
                    }
                    return it
                } else {
                    checkDataUpdate()
                    return instantiateItem(container,position)

                }
            }
        }

        val fragment = getItem(position)
        if (DEBUG) Log.v(TAG, "Adding item #$position: f=$fragment")
        if (mSavedState.size > position) {
            val fss = mSavedState[position]
            if (fss != null) {
                fragment.setInitialSavedState(fss)
            }
        }
        while (mItemInfos.size <= position) {
            mItemInfos.add(null)
        }
        fragment.setMenuVisibility(false)
        fragment.userVisibleHint = false
        val iiNew = ItemInfo(fragment, getItemData(position), position)
        mItemInfos[position] = iiNew
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction()
        }
        mCurTransaction?.add(container.id, fragment)

        return iiNew
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val ii = `object` as ItemInfo<T>
        if (DEBUG)
            Log.v(TAG, "Removing item #" + position + ": f=" + `object`
                    + " v=" + ii.fragment.view)
        while (mSavedState.size <= position) {
            mSavedState.add(null)
        }
        mSavedState[position] = if (ii.fragment.isAdded)
            mFragmentManager.saveFragmentInstanceState(ii.fragment)
        else
            null
        mItemInfos[position] = null
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction()
        }
        mCurTransaction?.remove(ii.fragment)
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        val ii = `object` as? ItemInfo<T>
        val fragment = ii?.fragment
        if (fragment != mCurrentPrimaryItem) {
            mCurrentPrimaryItem?.apply {
                setMenuVisibility(false)
                userVisibleHint = false
            }
            fragment?.apply {
                setMenuVisibility(true)
                userVisibleHint = true
            }
            mCurrentPrimaryItem = fragment
        }
    }

    override fun finishUpdate(container: ViewGroup) {
        mCurTransaction?.apply {
            commitNowAllowingStateLoss()
        }
        mCurTransaction = null
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        val fragment = (`object` as ItemInfo<T>).fragment
        return fragment.view === view
    }

    override fun getItemPosition(`object`: Any): Int {
        val itemInfo: ItemInfo<T> = `object` as ItemInfo<T>
        val oldPosition = mItemInfos.indexOf(itemInfo)
        if (oldPosition >= 0) {
            val oldData: T? = itemInfo.data
            val newData: T? = getItemData(oldPosition)
            return if (dataEquals(oldData, newData)) {
                POSITION_UNCHANGED
            } else {
                var oldDataNewPosition = getDataPosition(oldData)
                if (oldDataNewPosition < 0) {
                    oldDataNewPosition =POSITION_NONE
                }
                itemInfo.apply {
                    position = oldDataNewPosition
                }
                oldDataNewPosition
            }
        }
        return POSITION_UNCHANGED
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
        //更新缓存数组
        checkDataUpdate()
    }
    /**
     * 更新缓存数组。使位置和大小对应
     */
    private fun checkDataUpdate() {
        val pendingItemInfos = ArrayList<ItemInfo<T>?>(mItemInfos.size)
        //添加空数据
        for (i in 0 until mItemInfos.size) {
            pendingItemInfos.add(null)
        }
        for (value in mItemInfos) {
            value?.apply {
                if (position >= 0) {
                    //个数小于等于postion，需要继续添加空数据
                    while (pendingItemInfos.size <= position) {
                        pendingItemInfos.add(null)
                    }
                    //放到对应的位置
                    pendingItemInfos[position] = value
                }
            }
        }

        mItemInfos = pendingItemInfos
    }

    override fun saveState(): Parcelable? {
        var state: Bundle? = null
        if (mSavedState.size > 0) {
            state = Bundle()
            val fss = arrayOfNulls<Fragment.SavedState>(mSavedState.size)
            mSavedState.toArray(fss)
            state.putParcelableArray("states", fss)
        }
        for (i in mItemInfos.indices) {
            val f = mItemInfos[i]?.fragment
            if (f != null && f.isAdded) {
                if (state == null) {
                    state = Bundle()
                }
                val key = "f$i"
                mFragmentManager.putFragment(state, key, f)
            }
        }
        return state
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
        if (state != null) {
            val bundle = state as Bundle?
            bundle!!.classLoader = loader
            val fss = bundle.getParcelableArray("states")
            mSavedState.clear()
            mItemInfos.clear()
            if (fss != null) {
                for (i in fss.indices) {
                    mSavedState.add(fss[i] as Fragment.SavedState)
                }
            }
            val keys = bundle.keySet()
            for (key in keys) {
                if (key.startsWith("f")) {
                    val index = Integer.parseInt(key.substring(1))
                    val f = mFragmentManager.getFragment(bundle, key)
                    if (f != null) {
                        while (mItemInfos.size <= index) {
                            mItemInfos.add(null)
                        }
                        f.setMenuVisibility(false)
                        val iiNew = ItemInfo(f, getItemData(index), index)
                        mItemInfos[index] = iiNew
                    } else {
                        Log.w(TAG, "Bad fragment at key $key")
                    }
                }
            }
        }
    }

    protected fun getCurrentPrimaryItem() = mCurrentPrimaryItem

    protected fun getFragmentByPosition(position: Int): Fragment? {
        if (position < 0 || position >= mItemInfos.size) return null
        return mItemInfos[position]?.fragment
    }

}
