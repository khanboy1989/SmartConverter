package com.techtest.smartconverter.di.modules

import com.techtest.smartconverter.repository.RatesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class RepositoryModule {

    @Provides
    @Singleton
    fun providesRatesRepository():RatesRepository{
        return RatesRepository()
    }

}