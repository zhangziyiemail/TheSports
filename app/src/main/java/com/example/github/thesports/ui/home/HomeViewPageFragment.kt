package com.example.github.thesports.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.github.thesports.MainActivity
import com.example.github.thesports.R
import com.example.github.thesports.base.BaseFragment
import com.example.github.thesports.base.OnItemClickListener
import com.example.github.thesports.databinding.FragmentHomePageBinding
import com.example.github.thesports.entity.LeagueEvent
import com.example.github.thesports.utils.LogUtils

/**
 *   Created by Lee Zhang on 10/22/20 23:10
 **/
class HomeViewPageFragment : BaseFragment<FragmentHomePageBinding>() {

    private var state = "all"

    lateinit var mData: List<LeagueEvent>

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
            mData = it
        })
    }

    override fun initFragment(view: View, savedInstanceState: Bundle?) {
        mBinding?.let { binding ->
            binding.adapter = mAdapter
            binding.itemClick = OnItemClickListener { position, view ->
                var bundle = Bundle()
                mAdapter.getItemData(position)?.idEvent?.toLong()?.let {
                    bundle.putLong("event_id",it)
                }

                mNavController.navigate(
                    R.id.action_homeviewpagefragment_to_eventdetailfragment,
                    bundle
                )
            }
        }
        (activity as MainActivity).onViewClick ={
            when(it){
                R.id.menu_all->{
                    if(state != "all") mViewModel.fatchLeagueEventWhithId(arguments?.getInt(LEAGUE_EVENT) ?: 0)
                    state ="all"
                }
                R.id.menu_ended->{
                    if(state != "ended")mViewModel.fatchEndLeagueEventWhithId(arguments?.getInt(LEAGUE_EVENT) ?: 0,"Match Finished")
                    state ="ended"
                }
                R.id.menu_future->{
                    if(state != "future")mViewModel.fatchEndLeagueEventWhithId(arguments?.getInt(LEAGUE_EVENT) ?: 0,"Not Started")
                    state ="future"
                }
                R.id.menu_sort->LogUtils.error("HomeViewPageFragment sort")
                R.id.menu_search->LogUtils.error("HomeViewPageFragment search")
            }
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