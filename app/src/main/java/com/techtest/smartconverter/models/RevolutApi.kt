package com.techtest.smartconverter.models

import com.techtest.smartconverter.util.Feeds
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RevolutApi {

    @GET(Feeds.LATEST_EUR)
    fun getRates(@Query("base") base:String): Single<RateList>

}