package com.example.github.thesports.ui.select

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.github.thesports.R
import com.example.github.thesports.base.BaseRecyclerAdapter
import com.example.github.thesports.base.BaseViewHolder
import com.example.github.thesports.data.MyDatabaseUtils
import com.example.github.thesports.databinding.ItemSportBinding
import com.example.github.thesports.entity.League
import com.example.github.thesports.entity.Sport
import com.example.github.thesports.entity.SportWithLeagueList
import com.example.github.thesports.http.RetrofitManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import kotlinx.coroutines.*

/**
 *   Created by Lee Zhang on 10/20/20 12:49
 **/

class SelectListViewRepository {
    val counterContext = newSingleThreadContext("CounterContext")

    suspend fun getAllSports(): MutableList<Sport> = withContext(Dispatchers.IO) {
        RetrofitManager.apiService.getAllSports().sports
    }

    suspend fun getAllLeagues(): MutableList<League> = withContext(Dispatchers.IO) {
        RetrofitManager.apiService.getAllLeague().leagues
    }


}


class SelectPageAdapter : BaseRecyclerAdapter<SportWithLeagueList>(null) {
    override fun getLayoutId(viewType: Int): Int = R.layout.item_sport

    override fun setVariable(
        data: SportWithLeagueList,
        position: Int,
        holder: BaseViewHolder<ViewDataBinding>
    ) {

        val cgLeagues = (holder.binding as ItemSportBinding).cgLeagues
        for (league in data.leagueslist) {

            val chip = Chip(cgLeagues.context)
            chip.setChipDrawable(
                ChipDrawable.createFromAttributes(
                    cgLeagues.context,
                    null,
                    0,
                    R.style.Widget_MaterialComponents_Chip_Filter
                )
            )
            chip.text = league.strLeague
            chip.isChecked = league.follow
            chip.setOnClickListener(View.OnClickListener {
                if (!(it as Chip).isChecked) {
                    GlobalScope.launch {
                        MyDatabaseUtils.leagueDao.setfollow(league.idLeague, false)
                    }
                } else {
                    GlobalScope.launch {
                        MyDatabaseUtils.leagueDao.setfollow(league.idLeague, true)
                    }
                }
            })
            cgLeagues.addView(chip)
        }
        holder.binding.sportName = data.sprot.strSport
    }

    fun update(newData: List<SportWithLeagueList>?) {
        val result = DiffUtil.calculateDiff(NewsListCacheDiffCall(newData, getAdapterData()), true)
        if (mData == null) {
            mData = mutableListOf()
        }
        mData?.addAll(newData ?: mutableListOf())
        result.dispatchUpdatesTo(this)
    }
}

class NewsListCacheDiffCall(
    private val newList: List<SportWithLeagueList>?,
    private val oldList: List<SportWithLeagueList>?
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        if (newList.isNullOrEmpty() || oldList.isNullOrEmpty()) false
        else newList[newItemPosition].sprot.idSport == oldList[oldItemPosition].sprot.idSport

    override fun getOldListSize(): Int = oldList?.size ?: 0

    override fun getNewListSize(): Int = newList?.size ?: 0

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        if (newList.isNullOrEmpty() || oldList.isNullOrEmpty()) false
        else newList[newItemPosition] == oldList[oldItemPosition]

}



