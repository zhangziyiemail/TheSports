package com.example.github.thesports.ui.home

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.github.thesports.R
import com.example.github.thesports.base.BaseRecyclerAdapter
import com.example.github.thesports.base.BaseViewHolder
import com.example.github.thesports.data.MyDatabaseUtils
import com.example.github.thesports.databinding.ItemHomePageBinding
import com.example.github.thesports.databinding.ItemSearchviewBinding

import com.example.github.thesports.entity.*
import com.example.github.thesports.http.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *   Created by Lee Zhang on 10/22/20 17:23
 **/
class HomeListViewRepository{

    suspend fun getEventData(strEvent: String): List<String> = withContext(Dispatchers.IO) {
        MyDatabaseUtils.leagueEventDao.getLikeEvent(strEvent)
    }

    suspend fun getNextEventData(id:Long): List<LeagueEvent> = withContext(Dispatchers.IO) {
        RetrofitManager.apiService.geteventsnextleague(id).events
    }

    suspend fun getLastEventData(id:Long): List<LeagueEvent> = withContext(Dispatchers.IO) {
        RetrofitManager.apiService.geteventspastleague(id).events
    }
}

class CollectionAdapter(fragment: HomeFragment, var data:List<LeagueWithEvent>) : FragmentStateAdapter(fragment){

    override fun getItemCount(): Int = data.size

    override fun createFragment(position: Int): Fragment {
        val homeViewPageFragment = HomeViewPageFragment()
        homeViewPageFragment.arguments = Bundle().apply {
            putInt("event_id", position + 1)
        }
        return homeViewPageFragment
    }
}

class HomePageAdapter : BaseRecyclerAdapter<LeagueEvent>(null){
    override fun getLayoutId(viewType: Int): Int  = R.layout.item_home_page

    override fun setVariable(
        data: LeagueEvent,
        position: Int,
        holder: BaseViewHolder<ViewDataBinding>
    ) {

        (holder.binding as ItemHomePageBinding).awayTeam = data.strAwayTeam
        holder.binding.homeTeam = data.strHomeTeam
        if (data.noti)holder.binding.ivIconNoti.setImageResource(R.mipmap.icon_bell)
        holder.binding.finised = if ("Match Finished".equals(data.strStatus)) true else false
        holder.binding.awayScore = data.intAwayScore
        holder.binding.homeScore = data.intHomeScore
        holder.binding.time =  data.strTime
        holder.binding.date =  data.dateEvent

    }


    fun update(leagueEvent: List<LeagueEvent>) {

        val result = DiffUtil.calculateDiff(NewsListCacheDiffCall(leagueEvent, getAdapterData()), true)
        if (mData == null) {
            mData = mutableListOf()
        }else{
            mData?.clear()
        }

        mData?.addAll(leagueEvent ?: mutableListOf())
        result.dispatchUpdatesTo(this)
    }

}

class NewsListCacheDiffCall(
    private val newList : List<LeagueEvent>?,
    private val oldList : List<LeagueEvent>? ):DiffUtil.Callback()
{
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        if (newList.isNullOrEmpty() || oldList.isNullOrEmpty()) false
        else newList[newItemPosition].idEvent == oldList[oldItemPosition].idEvent

    override fun getOldListSize(): Int = oldList?.size ?: 0

    override fun getNewListSize(): Int = newList?.size ?: 0

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        if (newList.isNullOrEmpty() || oldList.isNullOrEmpty()) false
        else newList[newItemPosition] == oldList[oldItemPosition]

}

class SearchaAdapter : BaseRecyclerAdapter<String>(null) {

    override fun getLayoutId(viewType: Int): Int = R.layout.item_searchview

    override fun setVariable(data: String, position: Int, holder: BaseViewHolder<ViewDataBinding>) {

        (holder.binding as ItemSearchviewBinding).search = data
    }

    fun update(strEvent: List<String>?) {
        val result = DiffUtil.calculateDiff(SearchaCacheDiffCall(strEvent, getAdapterData()), true)
        if (mData == null) {
            mData = mutableListOf()
        } else {
            mData?.clear()
        }
        mData?.addAll(strEvent ?: mutableListOf())
        result.dispatchUpdatesTo(this)
    }
}


class SearchaCacheDiffCall(
    private val newList: List<String>?,
    private val oldList: List<String>?
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        if (newList.isNullOrEmpty() || oldList.isNullOrEmpty()) false
        else newList[newItemPosition] == oldList[oldItemPosition]

    override fun getOldListSize(): Int = oldList?.size ?: 0

    override fun getNewListSize(): Int = newList?.size ?: 0

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        if (newList.isNullOrEmpty() || oldList.isNullOrEmpty()) false
        else newList[newItemPosition] == oldList[oldItemPosition]

}



