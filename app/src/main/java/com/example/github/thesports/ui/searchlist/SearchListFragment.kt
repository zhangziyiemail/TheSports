package com.example.github.thesports.ui.searchlist

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.github.thesports.R
import com.example.github.thesports.base.BaseFragment
import com.example.github.thesports.base.OnItemClickListener
import com.example.github.thesports.data.MyDatabaseUtils
import com.example.github.thesports.databinding.FragmentSearchListBinding
import com.example.github.thesports.entity.EventDetail
import com.example.github.thesports.entity.LeagueEvent
import com.example.github.thesports.ui.activity.MainActivity
import com.example.github.thesports.ui.home.HomePageAdapter
import com.example.github.thesports.utils.LogUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchListFragment : BaseFragment<FragmentSearchListBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_search_list
    var mData = arrayListOf<LeagueEvent>()
    private val mViewModel by lazy {
        ViewModelProvider(requireActivity(), SearchListFactory(SearchListRepository())).get(
            SearchListModel::class.java
        )
    }

    private val mAdapter by lazy {
        HomePageAdapter()
    }

    override fun actionsOnViewInflate() {
        (activity as MainActivity).showLoading()
        arguments?.getString("str_event").let {
            if (it != null) {
                mViewModel.fatchSearchEvent(it)
            }
        }
        mViewModel.searchEventLists.observe(this, Observer {
            mData.addAll(it)
            mAdapter.update(it)
            LogUtils.error(it.toString())
            (activity as MainActivity).dismissLoading()
        })
    }

    override fun initFragment(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).showToolBar(false)
        mBinding?.let { binding ->
            binding.adapter = mAdapter
            binding.itemClick = OnItemClickListener { position, view ->
                val itemData = mAdapter.getItemData(position)
                GlobalScope.launch {
                    if (itemData != null) {
                        MyDatabaseUtils.leagueEventDao.insertLeague(itemData)
                    }
                }
                val bundle = Bundle()
                if (itemData != null) {
                    bundle.putString("event_id",itemData.idEvent)
                }
                mNavController.navigate(R.id.action_selectfragment_to_eventdetailfragment,bundle)
            }
        }
    }
}


