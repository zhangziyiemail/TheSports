package com.example.github.thesports.ui.home

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.github.thesports.R
import com.example.github.thesports.base.BaseFragment
import com.example.github.thesports.base.OnItemClickListener
import com.example.github.thesports.databinding.FragmentHomeBinding
import com.example.github.thesports.entity.LeagueWithEvent
import com.example.github.thesports.ui.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private var state = "all"
    override fun getLayoutId(): Int = R.layout.fragment_home
    lateinit var mData: List<LeagueWithEvent>
    private lateinit var mAdapter: CollectionAdapter
    var searchData = arrayListOf<String>()


    private val mViewModel by lazy {
        ViewModelProvider(this, HomeViewModeFactory(HomeListViewRepository())).get(
            HomeViewModel::class.java
        )
    }

    private val mSearchAdapter by lazy {
        SearchaAdapter()
    }

    override fun actionsOnViewInflate() {
        (activity as MainActivity).showLoading()
        mViewModel.fatchLeagueEventList()
        registerViewmodel()
        mViewModel.likeSearchList.observe(this, Observer {
            searchData.clear()
            searchData.addAll(it)
            mSearchAdapter.update(it)
        })
    }

    override fun initFragment(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).showToolBar(true)
        mBinding?.let { binding ->
            binding.adapter = mSearchAdapter
            binding.itemClick = OnItemClickListener { position, view ->
                val tv_search = view.findViewById<TextView>(R.id.tv_title)
                val bundle = Bundle()
                bundle.putString("str_event", tv_search.text.toString())
                mNavController.navigate(
                    R.id.action_homeviewpagefragment_to_searchlistfragment,
                    bundle
                )

            }
        }
        (activity as MainActivity).onSearch = {
            listView.visibility = View.VISIBLE
            if (it != null) {
                mViewModel.fatchLikeSearchView(it)
            }
        }

        (activity as MainActivity).onSearchClose = {
            listView.visibility = View.GONE
        }

//        (activity as MainActivity).onViewClick = {
//            when (it) {
//                R.id.menu_all -> {
//                    if (state != "all") mViewModel.fatchLeagueEventWhithId(
//                        view_pager.currentItem
//                    )
//                    state = "all"
//                }
//                R.id.menu_ended -> {
//                    if (state != "ended") {
//                        LogUtils.error("fatchEndLeagueEventWhithId "+  view_pager.currentItem)
//                        mViewModel.fatchEndLeagueEventWhithId(
//                            view_pager.currentItem, "Match Finished"
//                        )
//                    }
//                    state = "ended"
//                }
//                R.id.menu_future -> {
//                    if (state != "future") {
//
//                        mViewModel.fatchNoStartLeagueEventWhithId(
//                            view_pager.currentItem, "Not Started"
//                        )
//                    }
//
//                    state = "future"
//                }
//                R.id.menu_sort -> mViewModel.fatchSortData()
//
//                R.id.menu_sport -> mNavController.navigate(R.id.action_homeFragment_to_selectFragment)
//
//            }
//        }
    }

    override fun onPause() {
//        view_pager.setAdapter(null);
        super.onPause()
    }

    override fun onResume() {
//        registerViewmodel()
        super.onResume()
    }

    fun registerViewmodel() {
        mViewModel.leagueWithEventList.observe(this, Observer {
            mData = it
            val collectionPagerAdapter = CollectionPagerAdapter(childFragmentManager, it)
            view_pager.adapter = collectionPagerAdapter
            view_pager.offscreenPageLimit = 1
            tl_leagues.setupWithViewPager(view_pager)
            (view_pager as RecyclerView).recycledViewPool.setMaxRecycledViews(
                R.layout.item_home_page,
                0
            )
//            mAdapter = CollectionAdapter(this, it)
//            view_pager.adapter = mAdapter
//
//            TabLayoutMediator(tl_leagues, view_pager) { tab, position ->
//                tab.text = mData[position].league.strLeague
//            }.attach()
        }
        )
    }


}