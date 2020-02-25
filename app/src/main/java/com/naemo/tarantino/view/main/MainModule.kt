package com.naemo.tarantino.view.main

import android.app.Application
import com.naemo.tarantino.R
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @Provides
    fun providesMainViewModel(application: Application): MainViewModel {
        return MainViewModel(application)
    }

    @Provides
    fun layoutId(): Int {
        return R.layout.activity_main
    }
}