package com.techtest.smartconverter.ui.base

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter {

    protected val disposable = CompositeDisposable()

    open fun onViewCreated(){}
    open fun onViewDestroyed(){}
    open fun onPause(){}
    open fun onStop(){}


}