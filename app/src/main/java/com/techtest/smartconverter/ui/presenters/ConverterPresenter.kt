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
    val rates by lazy { MutableLiveData<ArrayList<Rate>>() }

    var base: String = ""
    private var isLoadedDefaultBase = false

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
     * If previous and current base is equal just update the amount calculation
     * If base is not changed we simply update the rates depending on new amount
     */
    fun refreshBaseAndAmount(base: String, amount: Float) {
        //if there is an letter upper or lower case ignore and check if the values are equal
        if(base.equals(this.base,ignoreCase = true)){
            convertView.updateAmount(amount)
        }else {
            this.base = base.toUpperCase()
            loadError.value = false
            loading.value = !isLoadedDefaultBase
            this.rates.value = null
            getRates(this.base)
        }
    }

    /**
     * get rates function gets value for DEFAULT CURRENCY AND AMOUNT (initially)
     * when base(currency) has changed the new currency request is being made
     * @param base:currency
     * */
    private fun getRates(base: String) {
        disposable.add(service.getRates(base)
            .delay(Constants.RATE_REFRESH_FREQUENCY, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .repeatUntil { !base.equals(this.base, ignoreCase = true) }
            .subscribe({
                val rates = ArrayList<Rate>()
                rates.add(Rate(it.base, Constants.DEFAULT_AMOUNT))
                rates.addAll(it.rates.map { Rate(it.key, it.value) })
                this.rates.value = rates
                loading.value = false
                loadError.value = false
                isLoadedDefaultBase = true
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
        disposable.clear()
    }

    /**
     *onPause
     * dispose the disposable
     */
    override fun onPause() {
        disposable.clear()
    }

    /**
     *onStop dispose the disposables so that application won't make any request
     */
    override fun onStop() {
        disposable.clear()
    }

    /**
     * OnResume update the list once more with default values
     */
    override fun onResume() {
        base = ""
        isLoadedDefaultBase = false
        refreshBaseAndAmount(Constants.DEFAULT_SYMBOL,Constants.DEFAULT_AMOUNT)
    }
}