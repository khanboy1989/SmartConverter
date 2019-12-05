package com.techtest.smartconverter.ui.base

abstract class BasePresenter {
    
    open fun onViewCreated(){}
    open fun onViewDestroyed(){}
    open fun onPause(){}


}