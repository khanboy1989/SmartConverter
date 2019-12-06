package com.techtest.smartconverter.models

import com.techtest.smartconverter.di.components.DaggerApiComponent
import io.reactivex.Single
import javax.inject.Inject

class RevolutApiService {

    @Inject
    lateinit var revolutApi:RevolutApi

    init{
        DaggerApiComponent.builder().build().inject(this)
    }

    fun getRates(base:String):Single<RateList>{
        return revolutApi.getRates(base)
    }

}