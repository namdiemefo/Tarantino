package com.naemo.tarantino.network

import android.content.Context
import android.util.Log
import com.naemo.tarantino.Tarantino
import com.naemo.tarantino.api.model.Response
import com.naemo.tarantino.util.NetworkUtil
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.File
import java.util.concurrent.TimeUnit
import java.io.IOException
import javax.inject.Inject
import okhttp3.CacheControl
import okhttp3.logging.HttpLoggingInterceptor





class Client {

    val TAG = "Client"
    private var BASE_URL = "http://api.themoviedb.org/3/"
    private var service: Service
    private var HEADER_CACHE_CONTROL = "Cache-Control"
    private var HEADER_PRAGMA = "Pragma"
    private var cacheSize: Long = 15 * 1024 * 1024
    var tara: Tarantino? = null

    var context: Context? = null
        @Inject set



    init {
        this.context = context



        val okHttpClient = OkHttpClient.Builder()
            .cache(cache())
            .addInterceptor(httpLoggingInterceptor()) //if network is on or off
            .addNetworkInterceptor(networkInterceptor()) //if network is on
            .addInterceptor(offlineInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        service = retrofit.create(Service::class.java)

    }

    fun getApi(): Service {
        return service
    }

    private fun cache(): Cache {
        return Cache(File(tara?.cacheDir, "whatever"), cacheSize)
    }

   private fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d(TAG, "log: http log: $message")
            }
        })
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return httpLoggingInterceptor
    }


    private fun networkInterceptor(): Interceptor {
        return object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                Log.d(TAG, "network interceptor: called.")

                val response = chain.proceed(chain.request())

                val cacheControl = CacheControl.Builder()
                    .maxAge(5, TimeUnit.MINUTES)
                    .build()

                return response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build()
            }
        }
    }

    private fun offlineInterceptor(): Interceptor {
        return object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                Log.d(TAG, "offline interceptor: called.")
                var request = chain.request()

                if (!NetworkUtil().isNetworkConnected()) {
                    val cacheControl = CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build()

                    request = request.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .cacheControl(cacheControl)
                        .build()
                }
                return chain.proceed(request)
            }
        }
    }
}

interface Service {

    @GET("discover/movie")
    fun getTopRatedMovies(@Query("api_key") apiKey: String): Call<Response>
}