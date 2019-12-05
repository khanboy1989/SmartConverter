package com.techtest.smartconverter.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.techtest.smartconverter.ui.fragment.ConverterFragment
import com.techtest.smartconverter.ui.fragment.RatesFragment

class TabBarFragmentAdapter(val context:Context,fm:FragmentManager ): FragmentPagerAdapter(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            RATES_FRAGMENT -> RatesFragment()
            CONVERTER_FRAGMENT -> ConverterFragment()
            else -> error("No fragment found")
        }

    }

    override fun getCount(): Int {
        return getFragmentSize().size
    }

    companion object{
        const val  RATES_FRAGMENT  =  0
        const val  CONVERTER_FRAGMENT = 1

        private var fragmentItemSize:MutableList<Int> = mutableListOf()

        private fun getFragmentSize():List<Int>{
            fragmentItemSize.add(RATES_FRAGMENT)
            fragmentItemSize.add(CONVERTER_FRAGMENT)
            return fragmentItemSize

        }
    }
}