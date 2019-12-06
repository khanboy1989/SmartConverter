package com.techtest.smartconverter.presenters

import android.annotation.SuppressLint
import android.util.Log
import com.techtest.smartconverter.di.components.DaggerPresenterComponent
import com.techtest.smartconverter.models.RateList
import com.techtest.smartconverter.repository.RatesRepository
import com.techtest.smartconverter.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ConverterPresenter:BasePresenter() {

    private var TAG = ConverterPresenter::class.java.simpleName

    @Inject
    lateinit var repository:RatesRepository

    init {
        DaggerPresenterComponent.builder().build().inject(this)
    }

    @SuppressLint("CheckResult")
    fun getRates(base:String){
        Log.d(TAG,"presenter get rates")
        repository.getRates(base).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribeWith(object: DisposableSingleObserver<RateList>(){
                override fun onSuccess(t: RateList) {
                    Log.d(TAG,"onSuccess" + t.toString())
                }

                override fun onError(e: Throwable) {
                    Log.d(TAG,"onError" + e.localizedMessage)

                }

            })
    }
}