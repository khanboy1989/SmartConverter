package com.techtest.smartconverter.ui.presenters

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.techtest.smartconverter.di.components.DaggerPresenterComponent
import com.techtest.smartconverter.models.Rate
import com.techtest.smartconverter.models.RevolutApiService
import com.techtest.smartconverter.ui.base.BasePresenter
import com.techtest.smartconverter.ui.fragment.ConvertView
import com.techtest.smartconverter.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ConverterPresenter(private val convertView: ConvertView) : BasePresenter<ConvertView>(convertView) {

    private var TAG = ConverterPresenter::class.java.simpleName

    //<----------Observable variables ------------>
    val loadError by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }
    val rates by lazy { MutableLiveData<List<Rate>>() }

    private var base: String = ""
    private var amount: Float = 1.0F

    //inject the RevolutApiService from Presenter Component
    @Inject
    lateinit var service: RevolutApiService

    /***
     * initialize the dagger component and inject the dependencies
     */
    init {
        DaggerPresenterComponent.create().inject(this)
    }

    /***
     * Refresh amounts function is entry point for getting the rates
     * @param base : currency requested by user(initially default currency is passed)
     * @param amount : amount (initially default amount is passed)
     */
    fun refreshBaseAndAmount(base: String, amount: Float) {
        this.amount = amount
        when (this.base == base) {
            true -> {
                this.amount = amount
                convertView.updateAmount(this.amount)
            }
            false -> {
                this.base = base.toUpperCase()
                rates.value = null
                loadError.value = false
                loading.value = true
                this.base = base
                this.amount = amount
                getRates(base, amount)
            }
        }


    }

    /**
     * GetRates get rates function gets value for DEFAULT CURRENCY AND AMOUNT
     * @param base:currency
     * @param amount: that is requested by user
     * */
    @SuppressLint("CheckResult")
    private fun getRates(base: String, amount: Float) {
        disposable.add(service.getRates(base)
            .delay(Constants.RATE_REFRESH_FREQUENCY, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .repeatUntil { !base.equals(Constants.DEFAULT_SYMBOL, ignoreCase = true) }
            .subscribe({
                val rates = ArrayList<Rate>()
                rates.add(Rate(it.base, Constants.DEFAULT_AMOUNT))
                rates.addAll(it.rates.map { Rate(it.key, it.value) })
                this.rates.value = rates
                loading.value = false
                loadError.value = false

            }, {
                it.printStackTrace()
                loading.value = false
                loadError.value = true
            })
        )
    }


    /**
     * When binded activity is generated this method is called
     */
    override fun onViewCreated() {
    }

    /**
     * When view is destroyed dispose all retrofit
     * tasks so that application will no longer send request to server
     * at the background
     */
    override fun onViewDestroyed() {
        disposable.dispose()
    }

    /**
     *onPause
     * dispose the disposable
     */
    override fun onPause() {
        disposable.dispose()
    }

    override fun onStop() {
        disposable.clear()
    }
}