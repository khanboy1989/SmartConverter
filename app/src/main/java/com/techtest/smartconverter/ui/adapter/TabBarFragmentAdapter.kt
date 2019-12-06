package com.techtest.smartconverter.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.techtest.smartconverter.R
import com.techtest.smartconverter.ui.fragment.ConverterFragment
import com.techtest.smartconverter.ui.fragment.RatesFragment

class TabBarFragmentAdapter(val context:Context,fm:FragmentManager ): FragmentPagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            RATES_FRAGMENT -> RatesFragment()
            CONVERTER_FRAGMENT -> ConverterFragment()
            else -> error("No fragment found")
        }

    }
    override fun getCount(): Int {
        return 2
    }

    companion object{
        const val  RATES_FRAGMENT  =  0
        const val  CONVERTER_FRAGMENT = 1
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            RATES_FRAGMENT -> context.getString(R.string.title_rates)
            CONVERTER_FRAGMENT -> context.getString(R.string.title_converter)
            else -> ""
        }
    }
}