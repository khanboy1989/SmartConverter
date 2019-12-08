package com.techtest.smartconverter.ui.fragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.techtest.smartconverter.R
import com.techtest.smartconverter.presenters.ConverterPresenter
import com.techtest.smartconverter.ui.base.BaseFragment
import com.techtest.smartconverter.util.Constants


class ConverterFragment : BaseFragment<ConverterPresenter>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        getRates(Constants.DEFAULT_SYMBOL,Constants.DEFAULT_AMOUNT)
        return inflater.inflate(R.layout.fragment_converter, container, false)
    }

    override fun instantiatePresenter(): ConverterPresenter {
        return ConverterPresenter()
    }

    private fun getRates(base:String,amount:Float){
        presenter.refreshAmounts(base,amount)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }
}
