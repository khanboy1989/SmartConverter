package com.techtest.smartconverter.di

import com.techtest.smartconverter.di.modules.ApiModule
import com.techtest.smartconverter.models.RevolutApiService

class ApiModuleTest(val mockService:RevolutApiService):ApiModule() {

    override fun provideRevolutApiService(): RevolutApiService {
        return mockService
    }
}