package com.techtest.smartconverter.ui.base

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<out V:BaseView>(protected val view:V) {

    protected val disposable = CompositeDisposable()

    open fun onViewCreated(){}
    open fun onViewDestroyed(){}
    open fun onPause(){}
    open fun onStop(){}


}