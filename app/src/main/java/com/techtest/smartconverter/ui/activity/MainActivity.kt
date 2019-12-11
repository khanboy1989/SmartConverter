package com.techtest.smartconverter.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.techtest.smartconverter.R
import com.techtest.smartconverter.ui.adapter.TabBarFragmentAdapter
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    private lateinit var tabsAdapter:TabBarFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeTabs()
    }
    //initialize the tabbar fragment
    private fun initializeTabs(){
        tabsAdapter = TabBarFragmentAdapter(this,supportFragmentManager)
        mainViewPager.adapter = tabsAdapter
        tabLayout.setupWithViewPager(mainViewPager)
        mainViewPager.currentItem = 1
    }

}
