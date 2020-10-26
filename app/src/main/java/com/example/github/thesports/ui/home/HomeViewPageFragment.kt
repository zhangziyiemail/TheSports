package com.example.github.thesports.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.github.thesports.ui.activity.MainActivity
import com.example.github.thesports.R
import com.example.github.thesports.base.BaseFragment
import com.example.github.thesports.base.OnItemClickListener
import com.example.github.thesports.databinding.FragmentHomePageBinding
import com.example.github.thesports.entity.LeagueEvent
import com.example.github.thesports.utils.LogUtils
import java.text.SimpleDateFormat

/**
 *   Created by Lee Zhang on 10/22/20 23:10
 **/
class HomeViewPageFragment : BaseFragment<FragmentHomePageBinding>() {

    private var state = "all"

    lateinit var mData: List<LeagueEvent>

    var leagueEvent: Int = 0

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
        (activity as MainActivity).showLoading()

        mViewModel.leagueEventLists.observe(this, Observer {
            mAdapter.update(it)
            mData = it
            (activity as MainActivity).dismissLoading()
        })
    }

    override fun initFragment(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey("event_id") }?.apply {
            leagueEvent = getInt("event_id")-1
            mViewModel.fatchLeagueEventWhithId(leagueEvent)
        }
        mBinding?.let { binding ->
            binding.adapter = mAdapter
            binding.itemClick = OnItemClickListener { position, view ->
                val itemData = mAdapter.getItemData(position)
                if ("Match Finished".equals(itemData?.strStatus)) {
                    var bundle = Bundle()
                    itemData?.idEvent?.toLong()?.let { bundle.putLong("event_id", it) }
                    mNavController.navigate(
                        R.id.action_homeviewpagefragment_to_eventdetailfragment,
                        bundle
                    )
                } else {
                    itemData?.let {
                        //todo 添加提醒功能
                            itemData ->
                        var bundle = Bundle()
                        bundle.putString("event_click", itemData.strEvent)

                        val parse =
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(itemData.dateEvent + " " + itemData.strTime)

                        (activity as MainActivity).firebaseAnalytics
                            .setUserProperty("noti_event", itemData.strEvent)

//                        CalendarReminderUtils.addCalendarEvent(
//                            context,
//                            itemData.strEvent,
//                            itemData.strTime,
//                            parse.time,
//                            0
//                        )
                    }
                }
            }

            binding.refersh = SwipeRefreshLayout.OnRefreshListener {
                LogUtils.error(".OnRefreshListener "+leagueEvent)
                mViewModel.fatchLeagueEventWhithIdFromInternet(leagueEvent)
                binding.srlEventlist.isRefreshing = false
            }
        }
        (activity as MainActivity).onViewClick = {
            when (it) {
                R.id.menu_all -> {
                    if (state != "all") mViewModel.fatchLeagueEventWhithId(
                        leagueEvent
                    )
                    state = "all"
                }
                R.id.menu_ended -> {
                    if (state != "ended") mViewModel.fatchEndLeagueEventWhithId(
                        leagueEvent, "Match Finished"
                    )
                    state = "ended"
                }
                R.id.menu_future -> {
                    if (state != "future") mViewModel.fatchEndLeagueEventWhithId(
                        leagueEvent, "Not Started"
                    )
                    state = "future"
                }
                R.id.menu_sort -> mViewModel.fatchSortData()

                R.id.menu_sport -> mNavController.navigate(R.id.action_homeFragment_to_selectFragment)

            }
        }
    }


}