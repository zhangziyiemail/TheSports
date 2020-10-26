package com.example.github.thesports.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.github.thesports.R
import com.example.github.thesports.base.BaseFragment
import com.example.github.thesports.base.OnItemClickListener
import com.example.github.thesports.databinding.FragmentHomeBinding
import com.example.github.thesports.entity.LeagueWithEvent
import com.example.github.thesports.ui.activity.MainActivity
import com.example.github.thesports.utils.LogUtils
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_home
    lateinit var mData: List<LeagueWithEvent>
    private lateinit var mAdapter: CollectionAdapter
    var searchData = arrayListOf<String>()

    private val mViewModel by lazy {
        ViewModelProvider(requireActivity(), HomeViewModeFactory(HomeListViewRepository())).get(
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
                val bundle = Bundle()
                bundle.putString("str_event",searchData[position])
                mNavController.navigate(R.id.action_homeviewpagefragment_to_searchlistfragment,bundle)

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
    }

    override fun onPause() {
        view_pager.setAdapter(null);
        super.onPause()
    }

    override fun onResume() {
        registerViewmodel()
        super.onResume()
    }

    fun registerViewmodel() {
        mViewModel.leagueWithEventList.observe(this, Observer {
            mData = it
            mAdapter = CollectionAdapter(this, it)
            view_pager.adapter = mAdapter
            TabLayoutMediator(tl_leagues, view_pager) { tab, position ->
                tab.text = mData[position].league.strLeague
            }.attach()
        })
    }


}