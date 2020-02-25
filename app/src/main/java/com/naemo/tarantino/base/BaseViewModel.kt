package com.naemo.tarantino.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel

open class BaseViewModel<N>(application: Application) : AndroidViewModel(application) {
    private var navigator: N? = null

    fun getNavigator(): N?{
        return navigator
    }

    open fun setNavigator(navigator: N) {
        this.navigator = navigator
    }
}