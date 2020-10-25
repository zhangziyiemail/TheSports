package com.example.github.thesports.ui.home

import android.content.Context
import android.text.TextUtils
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager.widget.PagerAdapter
import com.example.github.thesports.R
import com.example.github.thesports.base.BaseRecyclerAdapter
import com.example.github.thesports.base.BaseViewHolder
import com.example.github.thesports.base.DynamicFragmentStatePagerAdapter
import com.example.github.thesports.databinding.ItemHomePageBinding

import com.example.github.thesports.entity.*
import com.example.github.thesports.http.RetrofitManager
import com.example.github.thesports.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *   Created by Lee Zhang on 10/22/20 17:23
 **/
class HomeListViewRepository{


    suspend fun getNextEventData(id:Long): List<LeagueEvent> = withContext(Dispatchers.IO) {
        RetrofitManager.apiService.geteventsnextleague(id).events
    }

    suspend fun getLastEventData(id:Long): List<LeagueEvent> = withContext(Dispatchers.IO) {
        RetrofitManager.apiService.geteventspastleague(id).events
    }
}


class HomePagerAdapter(fm: FragmentManager,var data :List<LeagueWithEvent>): DynamicFragmentStatePagerAdapter<LeagueWithEvent>(fm){

    var mDatas=data

    override fun getItem(position: Int): Fragment = HomeViewPageFragment.newInstance(position)

    override fun getItemData(position: Int): LeagueWithEvent? {
        return if(position<mDatas.size){
            mDatas[position]
        }else{
            null
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mDatas[position].league.strLeague
    }

    override fun getCount(): Int = mDatas.size

    override fun dataEquals(oldData: LeagueWithEvent?, newData: LeagueWithEvent?): Boolean =
        TextUtils.equals(oldData?.league?.strLeague, newData?.league?.strLeague)

    override fun getDataPosition(data: LeagueWithEvent?): Int = mDatas.indexOf(data)

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


    fun update(leagueEvent: List<LeagueEvent>?) {
        LogUtils.error("HomeViewPageFragment updata")
        val result = DiffUtil.calculateDiff(NewsListCacheDiffCall(leagueEvent, getAdapterData()), true)
        if (mData == null) {
            mData = mutableListOf()
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


