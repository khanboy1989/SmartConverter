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


    private fun initRecycler(){
        adapter = CurrencyListAdapter(this)
        rootView.currencyRecyclerView.adapter = adapter
        rootView.currencyRecyclerView.addItemDecoration(DividerItemDecoration(rootView.currencyRecyclerView.context,DividerItemDecoration.VERTICAL))
    }

    private fun initObservers(){
        presenter.rates.observe(this,currencyRateObserver)
        presenter.loading.observe(this,loadingProgressObserver)
    }

    override fun instantiatePresenter(): ConverterPresenter {
        return ConverterPresenter(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }

    private fun getRates(base:String,amount:Float){
        presenter.refreshBaseAndAmount(base,amount)
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
    private val currencyRateObserver = Observer<List<Rate>> {
        it?.let{
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


    companion object{
        const val AMOUNT_KEY = "amount_key"
        const val CURRENCY_KEY = "currency_key"
    }
}
