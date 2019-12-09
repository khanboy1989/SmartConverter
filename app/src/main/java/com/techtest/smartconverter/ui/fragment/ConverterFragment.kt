package com.techtest.smartconverter.ui.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.techtest.smartconverter.R
import com.techtest.smartconverter.models.Rate
import com.techtest.smartconverter.presenters.ConverterPresenter
import com.techtest.smartconverter.ui.adapter.CurrencyListAdapter
import com.techtest.smartconverter.ui.base.BaseFragment
import com.techtest.smartconverter.util.Constants
import kotlinx.android.synthetic.main.fragment_converter.view.*


class ConverterFragment : BaseFragment<ConverterPresenter>() {

    private lateinit var rootView:View
    private  val adapter = CurrencyListAdapter()
    private val TAG = ConverterFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        getRates(Constants.DEFAULT_SYMBOL,Constants.DEFAULT_AMOUNT)
        rootView =  inflater.inflate(R.layout.fragment_converter, container, false)
        initRecycler()
        initObservers()
        return rootView
    }


    private fun initRecycler(){
        rootView.currencyRecyclerView.adapter = adapter
        rootView.currencyRecyclerView.addItemDecoration(DividerItemDecoration(rootView.currencyRecyclerView.context,DividerItemDecoration.VERTICAL))
    }

    private fun initObservers(){
        presenter.rates.observe(this,currencyRateObserver)
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

    //<--------- Observers -------------->
    private val currencyRateObserver = Observer<List<Rate>> {
        it?.let{
            Log.d(TAG,it.toString())
            adapter.refreshData(it)
        }

    }
}
