package com.example.github.thesports.ui.select

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.github.thesports.ui.activity.MainActivity
import com.example.github.thesports.R
import com.example.github.thesports.base.BaseFragment
import com.example.github.thesports.databinding.FragmentSelectBinding
import kotlinx.android.synthetic.main.fragment_select.*
import kotlin.collections.ArrayList

/**
 *   Created by Lee Zhang on 10/19/20 18:16
 **/

class SelectFragment : BaseFragment<FragmentSelectBinding>() {

    var sportList = ArrayList<String>()

//    private val mAdapter by lazy {
//        SelectPageAdapter()
//    }

    private val mViewModel by lazy {
        ViewModelProvider(this, SelectViewModeFactory(SelectListViewRepository())).get(
            SelectViewModel::class.java
        )
    }

    private val mAdapter by lazy{
        SelectPageAdapter()
    }

    override fun getLayoutId(): Int = R.layout.fragment_select


    override fun actionsOnViewInflate() {
        mViewModel.fatchSproutAndLeague()
        (activity as MainActivity).showLoading()
        mViewModel.sportWithLeague.observe(this, Observer {
            mAdapter.update(it)
            it.forEach(){
                sportList.add(it.sprot.strSport)
            }
            (activity as MainActivity).dismissLoading()
        })

    }

    override fun initFragment(view: View, savedInstanceState: Bundle?) {
       (activity as MainActivity)?.showToolBar(false)
        mBinding?.let { binding ->
            binding.adapter = mAdapter
        }
        fab.setOnClickListener {
            mNavController.navigate(R.id.action_selectFragment_to_homeFragment)
            mAdapter.notifyDataSetChanged()
        }
    }
}