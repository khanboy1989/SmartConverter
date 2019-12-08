package com.techtest.smartconverter.presenters

import android.annotation.SuppressLint
import com.techtest.smartconverter.models.RateList
import com.techtest.smartconverter.models.RevolutApiService
import com.techtest.smartconverter.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ConverterPresenter : BasePresenter() {

    private var TAG = ConverterPresenter::class.java.simpleName

    @Inject
    lateinit var service: RevolutApiService


    fun refreshAmounts(base:String,amount:Float){
        getRates(base,amount)
    }

    @SuppressLint("CheckResult")
    private fun getRates(base: String,amount:Float) {
        service.getRates(base).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribeWith(object : DisposableSingleObserver<RateList>() {
                override fun onSuccess(t: RateList) {
                    print(t.toString())
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })
    }


    override fun onViewCreated() {
    }

    override fun onViewDestroyed() {

    }

    override fun onPause() {

    }
}