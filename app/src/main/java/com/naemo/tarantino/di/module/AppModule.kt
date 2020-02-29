package com.naemo.tarantino.di.module

import android.app.Application
import android.content.Context
import com.naemo.tarantino.network.Client
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {


    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun provideClient(): Client {
        return Client()
    }
}