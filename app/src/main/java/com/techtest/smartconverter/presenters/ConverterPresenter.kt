package com.techtest.smartconverter.presenters

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.techtest.smartconverter.di.components.DaggerApiComponent
import com.techtest.smartconverter.di.components.DaggerPresenterComponent
import com.techtest.smartconverter.models.Rate
import com.techtest.smartconverter.models.RateList
import com.techtest.smartconverter.models.RevolutApiService
import com.techtest.smartconverter.ui.base.BasePresenter
import com.techtest.smartconverter.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ConverterPresenter : BasePresenter() {

    private var TAG = ConverterPresenter::class.java.simpleName


    val loadError by lazy {MutableLiveData<Boolean>()}
    val loading by lazy {MutableLiveData<Boolean>()}
    val rates by lazy {MutableLiveData<List<Rate>>()}


    @Inject
    lateinit var service: RevolutApiService

    /***
     * initialize the dagger component and inject the dependecies
     */
    init {
        DaggerPresenterComponent.create().inject(this)
    }

    /***
     * Refresh amounts function is entry point for getting the rates
     *
     * @param base : currency requested by user(initially default currency is passed)
     * @param amount : amount (initially default amount is passed)
     */
    fun refreshAmounts(base:String,amount:Float){
        loadError.value = false
        loading.value = true
        rates.value = null
        getRates(base,amount)
    }

    /**
     * GetRates get rates function gets value for DEFAULT CURRENCY AND AMOUNT
     *
     * @param base:currency
     * @param amount: that is requested by user
     * */
    @SuppressLint("CheckResult")
    private fun getRates(base: String,amount:Float) {
        disposable.add(service.getRates(base)
            .delay(Constants.RATE_REFRESH_FREQUENCY, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .repeatUntil { !base.equals(Constants.DEFAULT_SYMBOL, ignoreCase = true) }
            .subscribe({
                val rates = ArrayList<Rate>()
                rates.add(Rate(it.base,Constants.DEFAULT_AMOUNT))
                rates.addAll(it.rates.map{Rate(it.key,it.value)})
                this.rates.value = rates
                loading.value = false
                loadError.value = false

            },{
                it.printStackTrace()
                loading.value = false
                loadError.value = true
            }))
    }


    override fun onViewCreated() {
    }

    override fun onViewDestroyed() {
        disposable.dispose()
    }

    override fun onPause() {
        disposable.dispose()
    }

    override fun onStop() {
        disposable.clear()
    }
}