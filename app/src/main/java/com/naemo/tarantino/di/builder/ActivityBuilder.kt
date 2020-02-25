package com.naemo.tarantino.di.builder

import com.naemo.tarantino.view.main.MainActivity
import com.naemo.tarantino.view.main.MainModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun bindMainActivity(): MainActivity

}