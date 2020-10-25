package com.example.github.thesports.ui.eventdetail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.github.thesports.MainActivity
import com.example.github.thesports.R
import com.example.github.thesports.base.BaseFragment
import com.example.github.thesports.databinding.FragmentEventDetailBinding
import com.example.github.thesports.entity.EventDetail
import com.example.github.thesports.ui.home.HomeListViewRepository
import com.example.github.thesports.ui.home.HomeViewModeFactory
import com.example.github.thesports.ui.home.HomeViewModel
import com.example.github.thesports.utils.LogUtils

/**
 *   Created by Lee Zhang on 10/24/20 16:24

 **/
class EventDetailFragment : BaseFragment<FragmentEventDetailBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_event_detail

    private val EVENT_ID = "event_id"

    private val mViewModel by lazy {
        ViewModelProvider(this, EventDetaiModeFactory(EventDetailViewRepository())).get(
            EventDetailViewModel::class.java
        )
    }

    override fun actionsOnViewInflate() {
        arguments?.getLong(EVENT_ID)?.let { mViewModel.fatchEventDetail(it) }

    }

    override fun initFragment(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).showToolBar(false)

        mViewModel.eventDetail.observe(this, Observer {
            mBinding?.let { binding ->
                binding.toolbarTitletext = it.strEvent

                binding.homeFormation = it.strHomeFormation
                binding.awayFormation = it.strAwayFormation

                binding.homeGoalDetails = it.strHomeGoalDetails
                binding.awayGoalDetails = it.strAwayGoalDetails

                binding.homeLineupDefense = it.strHomeLineupDefense
                binding.awayLineupDefense = it.strAwayLineupDefense

                binding.homeRedCards = it.strHomeRedCards
                binding.awayRedCards = it.strAwayRedCards

                binding.homeYellowCard = it.strHomeYellowCards
                binding.awayYellowCard = it.strAwayYellowCards

                binding.homeShots = it.intHomeShots
                binding.awayShots = it.intAwayShots

                binding.homeScore = it.intHomeScore
                binding.awayScore = it.intAwayScore

                binding.homeLineupForward = it.strHomeLineupForward
                binding.awayLineupForward = it.strAwayLineupForward

                binding.homeLineupGoalkeeper = it.strHomeLineupGoalkeeper
                binding.awayLineupGoalkeeper = it.strAwayLineupGoalkeeper

                binding.homeLineupMidfield = it.strHomeLineupMidfield
                binding.awayLineupMidfield = it.strAwayLineupMidfield

                binding.homeLineupSubstitutes = it.strHomeLineupSubstitutes
                binding.awayLineupSubstitutes = it.strAwayLineupSubstitutes
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()

    }


}