package com.techtest.smartconverter.di.modules

import com.techtest.smartconverter.models.RevolutApi
import com.techtest.smartconverter.models.RevolutApiService
import com.techtest.smartconverter.util.Feeds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
open class ApiModule {

    @Provides
    @Singleton
    fun provideRevolutApi():RevolutApi{
        return Retrofit.Builder()
            .baseUrl(Feeds.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(RevolutApi::class.java)
    }


    @Provides
    @Singleton
    open fun provideRevolutApiService():RevolutApiService{
        return RevolutApiService()
    }
}