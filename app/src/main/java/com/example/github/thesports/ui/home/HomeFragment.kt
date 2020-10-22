package com.example.github.thesports.ui.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.example.github.thesports.MainActivity
import com.example.github.thesports.R
import com.example.github.thesports.base.BaseFragment
import com.example.github.thesports.base.safeLaunch
import com.example.github.thesports.data.MyDatabaseUtils
import com.example.github.thesports.databinding.FragmentHomeBinding
import com.example.github.thesports.entity.MyLeagues
import com.example.github.thesports.utils.LogUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    lateinit var toolbar: Toolbar
    lateinit var myLeagues: List<MyLeagues>
    var mMap = HashMap<String, String>()
    var mSet = mutableSetOf<String>()

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun actionsOnViewInflate() {
        GlobalScope.safeLaunch{
            block ={
                myLeagues = MyDatabaseUtils.myLeaguesDao.getMyLeagues()
                for (item in myLeagues) {
                    if (!mMap.containsKey(item.strSport)){
                        mMap.put(item.strSport,item.strLeague)
                    }else{
                        mMap.put(item.strSport,mMap.get(item.strSport)!!+","+item.strLeague)
                    }
                }
            }
        }


//        for (item in mMap.keys){
//            tl_sports.addTab(tl_sports.newTab().setText(item),false)
//        }
    }

    override fun initFragment(view: View, savedInstanceState: Bundle?) {
//        toolbar = (activity as MainActivity)?.toolbar

        (activity as MainActivity).showToolBar(true)

    }


}