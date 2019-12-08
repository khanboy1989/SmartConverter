package com.techtest.smartconverter.models

import io.reactivex.Single
import javax.inject.Inject

class RevolutApiService {

    @Inject
    lateinit var revolutApi:RevolutApi

    init {
    }

    fun getRates(base:String):Single<RateList>{
        return revolutApi.getRates(base)
    }

}