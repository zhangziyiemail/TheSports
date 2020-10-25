package com.example.github.thesports.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 *   Created by Lee Zhang on 10/19/20 18:16
 **/
abstract class BaseFragment<VB : ViewDataBinding> : Fragment(), CoroutineScope by MainScope() {
    protected  var mBinding: VB? = null
    protected lateinit var mNavController : NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true

        if (mBinding == null){
            mBinding = DataBindingUtil.inflate(inflater,getLayoutId(),container,false)
            actionsOnViewInflate()
        }

        mNavController = NavHostFragment.findNavController(this)

        return if (mBinding != null) {
            mBinding!!.root.apply { (parent as? ViewGroup)?.removeView(this) }
        } else super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment(view, savedInstanceState)
        mBinding?.lifecycleOwner = this

    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
        mBinding?.unbind()
    }
    abstract fun getLayoutId():Int
    abstract fun actionsOnViewInflate()
    abstract fun initFragment(view: View, savedInstanceState: Bundle?)

    fun <T : ViewModel> getViewModel(clazz: Class<T>): T =
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(clazz)

    fun <T : ViewModel> getSharedViewModel(clazz: Class<T>): T =
        ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(clazz)
}