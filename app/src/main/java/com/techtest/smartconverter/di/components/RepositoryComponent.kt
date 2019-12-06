package com.techtest.smartconverter.di.components

import com.techtest.smartconverter.di.modules.ApiModule
import com.techtest.smartconverter.repository.RatesRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class])
interface RepositoryComponent {

    fun inject(repo:RatesRepository)

}