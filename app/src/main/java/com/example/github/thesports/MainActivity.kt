package com.example.github.thesports

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.github.thesports.base.BaseActivity
import com.example.github.thesports.databinding.ActivityMainBinding
import com.example.github.thesports.ui.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.yesButton

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private var availableCount = 0
    val hasLogin = MutableLiveData<Boolean>()

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

    fun toolBarTitle(title: String){
        toolbar.title = title
    }

    fun showToolBar(boolean: Boolean){
        if (!boolean)
            toolbar.visibility = View.GONE
        else
            toolbar.visibility = View.VISIBLE
    }


}