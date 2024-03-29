package com.techtest.smartconverter.di.components

import com.techtest.smartconverter.di.modules.ApiModule
import com.techtest.smartconverter.models.RevolutApiService
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [ApiModule::class])
interface ApiComponent {
    //inject the RevolutApi class into RevolutApiService
    fun inject(service:RevolutApiService)
}