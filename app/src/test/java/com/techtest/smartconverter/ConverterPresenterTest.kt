package com.techtest.smartconverter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.techtest.smartconverter.di.ApiModuleTest
import com.techtest.smartconverter.di.components.DaggerPresenterComponent
import com.techtest.smartconverter.models.Rate
import com.techtest.smartconverter.models.RateList
import com.techtest.smartconverter.models.RevolutApiService
import com.techtest.smartconverter.presenters.ConverterPresenter
import com.techtest.smartconverter.util.Constants
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class ConverterPresenterTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var revolutApiService:RevolutApiService

    lateinit var testScheduler: TestScheduler

    var presenter = ConverterPresenter()

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        DaggerPresenterComponent.builder()
            .apiModule(ApiModuleTest(revolutApiService)).build().inject(presenter)
    }


    @Before
    fun setupRxSchedulers(){
        val immidiate = object: Scheduler(){
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() },true)
            }
        }

        testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

        RxJavaPlugins.setInitNewThreadSchedulerHandler{ t: Callable<Scheduler> -> testScheduler  }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { t: Callable<Scheduler> ->  testScheduler}
    }


    @Test
    fun getRatesSuccess() {

        val rateMapped = mutableMapOf<String,Float>()

        rateMapped["AUD"] = 1.2F
        rateMapped["GBP"] = 0.9F

        val ratesList = RateList("EUR", "09/12/2018", rateMapped)

        val testSingle = Single.just(ratesList)

        Mockito.`when`(revolutApiService.getRates(Constants.DEFAULT_SYMBOL))
            .thenReturn(testSingle)


        presenter.refreshAmounts(Constants.DEFAULT_SYMBOL,1.0F)

        testScheduler.advanceTimeBy(1,TimeUnit.SECONDS)


        Assert.assertEquals(false,presenter.loadError.value)
        Assert.assertEquals(false,presenter.loading.value)

    }
}