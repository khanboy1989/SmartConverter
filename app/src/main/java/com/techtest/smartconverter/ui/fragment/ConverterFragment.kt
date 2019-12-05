package com.techtest.smartconverter.ui.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.techtest.smartconverter.R
import com.techtest.smartconverter.presenters.ConverterPresenter
import com.techtest.smartconverter.ui.base.BaseFragment


class ConverterFragment : BaseFragment<ConverterPresenter>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_converter, container, false)
    }

    override fun instantiatePresenter(): ConverterPresenter {
        return ConverterPresenter()
    }


}
