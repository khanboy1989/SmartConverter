package com.techtest.smartconverter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.techtest.smartconverter.di.ApiModuleTest
import com.techtest.smartconverter.di.components.DaggerPresenterComponent
import com.techtest.smartconverter.models.Rate
import com.techtest.smartconverter.models.RateList
import com.techtest.smartconverter.models.RevolutApiService
import com.techtest.smartconverter.ui.fragment.ConvertView
import com.techtest.smartconverter.ui.presenters.ConverterPresenter
import com.techtest.smartconverter.util.Constants
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
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
import java.util.concurrent.TimeUnit

//Testings of ConverterPresenter
@RunWith(MockitoJUnitRunner::class)
class ConverterPresenterTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var revolutApiService:RevolutApiService

    lateinit var testScheduler: TestScheduler

    lateinit  var presenter:ConverterPresenter

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        presenter = ConverterPresenter(ConvertViewTest())
        DaggerPresenterComponent.builder()
            .apiModule(ApiModuleTest(revolutApiService)).build().inject(presenter)
    }


    @Before
    fun setupRxSchedulers(){
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


        presenter.refreshBaseAndAmount(Constants.DEFAULT_SYMBOL,1.0F)

        testScheduler.advanceTimeBy(1,TimeUnit.SECONDS)


        Assert.assertEquals(false,presenter.loadError.value)
        Assert.assertEquals(false,presenter.loading.value)
        Assert.assertEquals(3,presenter.rates.value?.size)
        Assert.assertEquals("EUR",presenter.rates.value?.get(0)?.symbol)
    }

    @Test
    fun getRatesFailure(){
        val testSingle = Single.error<RateList>(Throwable())
        Mockito.`when`(revolutApiService.getRates(Constants.DEFAULT_SYMBOL))
            .thenReturn(testSingle)
        presenter.refreshBaseAndAmount(Constants.DEFAULT_SYMBOL,1.0F)
        testScheduler.advanceTimeBy(1,TimeUnit.SECONDS)
        Assert.assertEquals(true,presenter.loadError.value)

    }

    /**
     * Scenario of testing when Base changed
     * Testing how refreshAmountAndBase function behavior
     */
    @Test
    fun testWhenBaseIsSameAndAmountChanged(){
        presenter.base = "EUR"

        val rateMapped = mutableMapOf<String,Float>()

        rateMapped["AUD"] = 1.2F
        rateMapped["GBP"] = 0.9F

        val ratesList = RateList("AUD", "09/12/2018", rateMapped)

        val testSingle = Single.just(ratesList)

        Mockito.`when`(revolutApiService.getRates("AUD"))
            .thenReturn(testSingle)


        presenter.refreshBaseAndAmount("AUD",100.0F)

        testScheduler.advanceTimeBy(1,TimeUnit.SECONDS)

        Assert.assertEquals(false,presenter.loadError.value)
        Assert.assertEquals(false,presenter.loading.value)
        Assert.assertEquals(3,presenter.rates.value?.size)
        Assert.assertEquals("AUD",presenter.rates.value?.get(0)?.symbol)
    }

    /**
     * Scenario of testing when Base is same and amount changed
     * Testing how refreshAmountAndBase function behavior
     */
    @Test
    fun testWhenBaseChanged(){
        presenter.base = "EUR"
        presenter.refreshBaseAndAmount("AUD",100.0F)

    }

    //ConvertViewTest
    inner class ConvertViewTest: ConvertView{
        override fun updateAmount(amount: Float) {
            Assert.assertEquals(100.0F,amount)
        }
    }
}