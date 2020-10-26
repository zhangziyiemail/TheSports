package com.example.github.thesports.ui.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.github.thesports.R
import com.example.github.thesports.base.BaseActivity
import com.example.github.thesports.base.OnItemClickListener
import com.example.github.thesports.data.MyDatabaseUtils
import com.example.github.thesports.databinding.ActivityMainBinding
import com.example.github.thesports.ui.home.HomeFragment
import com.example.github.thesports.ui.home.HomeListViewRepository
import com.example.github.thesports.ui.home.HomeViewModeFactory
import com.example.github.thesports.ui.home.HomeViewModel
import com.example.github.thesports.ui.searchlist.SearchListFragment
import com.example.github.thesports.utils.LogUtils
import com.example.github.thesports.widget.CustomDialog
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : BaseActivity<ActivityMainBinding>() {
    lateinit var onViewClick:((Int)->Unit)
    lateinit var onSearch:((String)->Unit)
    lateinit var onSearchClose:(()->Unit)
    private var availableCount = 0
    lateinit var customDialog :CustomDialog
    lateinit var firebaseAnalytics: FirebaseAnalytics



    private val manager: ConnectivityManager by lazy {
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val request: NetworkRequest by lazy {
        NetworkRequest.Builder().build()
    }

    private val netStateCallback: ConnectivityManager.NetworkCallback by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                availableCount++
                checkState()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                availableCount--
                checkState()
            }
        }
    }

    private fun checkState() {
        mBinding.netAvailable = availableCount > 0
    }


    override fun onDestroy() {
        super.onDestroy()
        manager.unregisterNetworkCallback(netStateCallback)
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initActivity(savedInstanceState: Bundle?) {
        manager.registerNetworkCallback(request, netStateCallback)
        setSupportActionBar(toolbar)
        val options: FirebaseOptions = FirebaseOptions.Builder()
            .setApplicationId(getApplication().getPackageName())
            .setApiKey("AIzaSyCbzWSFMisHmP7J4bjVul-KycoHuy6-P3g")
            .build()
        firebaseAnalytics = Firebase.analytics
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true);
//        FirebaseApp.initializeApp(this,options);
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        customDialog = CustomDialog(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        toolbar.setOnMenuItemClickListener{
            if (::onViewClick.isInitialized)onViewClick.invoke(it.itemId) else LogUtils.error("onOptionsItemSelected")
            false
        }
        val menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_view, menu);
        val searchItem = menu?.findItem(R.id.menu_search)
        val searchView: SearchView? = searchItem?.actionView as SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (::onSearch.isInitialized) newText?.let { onSearch.invoke(it) } else LogUtils.error("onQueryTextChange")
                return true

            }
        })

        searchView?.setOnCloseListener {
            if (::onViewClick.isInitialized)onSearchClose.invoke() else LogUtils.error("onSearchClose")
            false
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.first()
            .childFragmentManager.fragments.last().let {
                if (it is HomeFragment) {
                    startActivity(
                        Intent(Intent.ACTION_MAIN)
                            .apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                addCategory(Intent.CATEGORY_HOME)
                            })
                    return
                }
            }

        super.onBackPressed()
    }


    fun toolBarTitle(title: String) {
        toolbar.title = title
    }

    fun showToolBar(boolean: Boolean) {
        if (!boolean)
            toolbar.visibility = View.GONE
        else
            toolbar.visibility = View.VISIBLE
    }

    fun showLoading(){
        customDialog.show()
    }

    fun dismissLoading(){
        customDialog.dismiss()
    }


    fun toolbarTitleClick(sportList: ArrayList<String>) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.sports)
            .setItems(
                sportList.toTypedArray(),
                DialogInterface.OnClickListener { dialog, which ->
//                    rv_sports.scrollToPosition(which)
                })
            .setCancelable(true)

        builder.create().show()
    }
}


