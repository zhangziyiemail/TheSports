package com.example.github.thesports.ui.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.github.thesports.MainActivity
import com.example.github.thesports.R
import com.example.github.thesports.base.BaseFragment
import com.example.github.thesports.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    lateinit var toolbar: Toolbar
    var sportList = ArrayList<String>()


    override fun getLayoutId(): Int = R.layout.fragment_home

    private val mViewModel by lazy {
        ViewModelProvider(requireActivity(), HomeViewModeFactory(HomeListViewRepository())).get(
            HomeViewModel::class.java
        )
    }
    lateinit var mAdapter: HomePagerAdapter


    override fun actionsOnViewInflate() {
        mViewModel.fatchLeagueEventList()
        mViewModel.leagueWithEventList.observe(this, Observer {
            mAdapter = HomePagerAdapter(parentFragmentManager,it)
            view_pager.adapter = mAdapter
            view_pager.setOffscreenPageLimit(1)
        })
    }

    override fun initFragment(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).showToolBar(true)
        tl_leagues.setupWithViewPager(view_pager)

    }


}