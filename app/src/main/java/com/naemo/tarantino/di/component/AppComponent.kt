package com.naemo.tarantino.di.component

import android.app.Application
import com.naemo.tarantino.Tarantino
import com.naemo.tarantino.di.builder.ActivityBuilder
import com.naemo.tarantino.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(AndroidSupportInjectionModule::class), (AppModule::class), (ActivityBuilder::class)])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder


        fun build(): AppComponent
    }

    fun inject(tarantino: Tarantino)
}