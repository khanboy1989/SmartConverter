package com.techtest.smartconverter.di.components

import com.techtest.smartconverter.di.modules.RepositoryModule
import com.techtest.smartconverter.presenters.ConverterPresenter
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [RepositoryModule::class])
interface PresenterComponent {
    fun inject(presenter:ConverterPresenter)
}