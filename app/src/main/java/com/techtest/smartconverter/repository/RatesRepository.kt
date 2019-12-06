package com.techtest.smartconverter.repository

import android.annotation.SuppressLint
import android.util.Log
import com.techtest.smartconverter.di.components.DaggerRepositoryComponent
import com.techtest.smartconverter.models.RateList
import com.techtest.smartconverter.models.RevolutApiService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RatesRepository {

    private var TAG = RatesRepository::class.java.simpleName

    @Inject
    lateinit var revApiService:RevolutApiService

    init {
        DaggerRepositoryComponent.builder().build().inject(this)
    }

    fun getRates(base:String):Single<RateList>{
        return revApiService.getRates(base)
    }
}