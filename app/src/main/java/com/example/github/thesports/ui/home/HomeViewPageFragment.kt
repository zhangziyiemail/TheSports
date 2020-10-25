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
import com.example.github.thesports.utils.CalendarReminderUtils
import com.example.github.thesports.utils.LogUtils
import java.text.ParsePosition
import java.text.SimpleDateFormat

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
            LogUtils.error(it.toString())
            mAdapter.update(it)
            mData = it
        })
    }

    override fun initFragment(view: View, savedInstanceState: Bundle?) {
        mBinding?.let { binding ->
            binding.adapter = mAdapter
            binding.itemClick = OnItemClickListener { position, view ->

                val itemData = mAdapter.getItemData(position)
                if ("Match Finished".equals(itemData?.strStatus)){
                    var bundle = Bundle()
                    itemData?.idEvent?.toLong()?.let { bundle.putLong("event_id", it) }
                    mNavController.navigate(
                        R.id.action_homeviewpagefragment_to_eventdetailfragment,
                        bundle
                    )
                }else{
                    itemData?.let {
                        //todo 添加提醒功能
                        itemData->
                        var bundle = Bundle()
                        bundle.putString("event_click",itemData.strEvent)
//                        Firebase.analytics.logEvent("event_click",bundle);
//                         SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z").parse(itemData.strTimestamp)).time
//                        CalendarReminderUtils.addCalendarEvent(context,itemData.strEvent,itemData.strTime,time,0)
                    }
                }
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