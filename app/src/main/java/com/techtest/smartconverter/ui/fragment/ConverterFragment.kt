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
import com.techtest.smartconverter.ui.activity.MainActivity
import com.techtest.smartconverter.ui.presenters.ConverterPresenter
import com.techtest.smartconverter.ui.adapter.CurrencyListAdapter
import com.techtest.smartconverter.ui.adapter.OnAmountChangedListener
import com.techtest.smartconverter.ui.base.BaseFragment
import com.techtest.smartconverter.util.Constants
import kotlinx.android.synthetic.main.fragment_converter.view.*


class ConverterFragment : BaseFragment<ConverterPresenter>(), OnAmountChangedListener,ConvertView {

    private lateinit var rootView:View
    private lateinit var adapter:CurrencyListAdapter
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


    /***
     * Initialize the Recycler in first launch
     */
    private fun initRecycler(){
        adapter = CurrencyListAdapter(this)
        rootView.currencyRecyclerView.adapter = adapter
        rootView.currencyRecyclerView.addItemDecoration(DividerItemDecoration(rootView.currencyRecyclerView.context,DividerItemDecoration.VERTICAL))
    }

    //Init observers for LiveData from presenter
    private fun initObservers(){
        presenter.rates.observe(this,currencyRateObserver)
        presenter.loading.observe(this,loadingProgressObserver)
    }

    //Initialize the presenter of the fragment
    override fun instantiatePresenter(): ConverterPresenter {
        return ConverterPresenter(this)
    }
    //Initial method when fragment created get the rates with default values
    private fun getRates(base:String,amount:Float){
        presenter.refreshBaseAndAmount(base,amount)
    }
    //onDestroy call presenter to stop disposable
    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }
    //onStop
    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }
    //onPause
    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    /**
     *Getting called by adapter and updates the amount on presenter checks
     * if amount and base (currency) has changed
     */
    override fun onAmountChanged(symbol: String, amount: Float) {
        presenter.refreshBaseAndAmount(symbol,amount)
    }

    /**
     *Getting called by presenter in order to update
     * the amount and change the values of each row
     */
    override fun updateAmount(amount: Float) {
        adapter.updateAmount(amount)
    }

    //<--------- Observers -------------->
    private val currencyRateObserver = Observer<ArrayList<Rate>> {
        it?.let{
            Log.d(TAG,it.toString())
            rootView.currencyRecyclerView.visibility = View.VISIBLE
            adapter.refreshData(it)
        }
    }

    private val loadingProgressObserver = Observer<Boolean>{isLoading->
        if(isLoading){
            rootView.loadingView.visibility = View.VISIBLE
            rootView.currencyRecyclerView.visibility = View.GONE
        }else{
            rootView.loadingView.visibility = View.GONE
        }
    }

}
