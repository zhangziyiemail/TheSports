package com.example.github.thesports.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.github.thesports.R
import com.example.github.thesports.base.BaseFragment
import com.example.github.thesports.databinding.FragmentHomePageBinding
import com.example.github.thesports.utils.LogUtils

/**
 *   Created by Lee Zhang on 10/22/20 23:10
 **/
class HomeViewPageFragment : BaseFragment<FragmentHomePageBinding>() {
    private val mViewModel by lazy {
        ViewModelProvider(requireActivity(), HomeViewModeFactory(HomeListViewRepository())).get(
            HomeViewModel::class.java
        )
    }

    private val mAdapter by lazy {
        HomePageAdapter()
    }

    override fun getLayoutId(): Int = R.layout.fragment_home_page

    override fun actionsOnViewInflate() {
        mViewModel.fatchLeagueEventWhithId(arguments?.getInt(LEAGUE_EVENT) ?: 0)
        mViewModel.leagueEventLists.observe(this, Observer {
            mAdapter.update(it)
        })
    }

    override fun initFragment(view: View, savedInstanceState: Bundle?) {
        mBinding?.let { binding ->
            binding.adapter = mAdapter
        }
    }

    companion object {
        private const val LEAGUE_EVENT = "league_event"

        @JvmStatic
        fun newInstance(sectionNumber: Int): HomeViewPageFragment {
            return HomeViewPageFragment().apply {
                arguments = Bundle().apply {
                    putInt(LEAGUE_EVENT, sectionNumber)
                }
            }
        }
    }

}