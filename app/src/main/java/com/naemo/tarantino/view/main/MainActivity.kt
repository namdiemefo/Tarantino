package com.naemo.tarantino.view.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.naemo.tarantino.BR
import com.naemo.tarantino.BuildConfig
import com.naemo.tarantino.R
import com.naemo.tarantino.api.model.Response
import com.naemo.tarantino.base.BaseActivity
import com.naemo.tarantino.databinding.ActivityMainBinding
import com.naemo.tarantino.network.Client
import com.naemo.tarantino.view.adapter.MovieAdapter
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import java.lang.Exception
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), MainNavigator, MovieAdapter.ItemClickListener {
    override fun onItemClicked(id: Int, originalTitle: String, posterPath: String, releaseDate: String, overView: String, rating: Double) {

    }

    var mainViewModel: MainViewModel? = null
        @Inject set

    var mLayoutId = R.layout.activity_main
        @Inject set

    var mBinder: ActivityMainBinding? = null
    var pd: ProgressBar? = null

    override fun getBindingVariable(): Int {
       return BR.viewModel
    }

    override fun getViewModel(): MainViewModel? {
        return mainViewModel
    }

    override fun getLayoutId(): Int {
        return mLayoutId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doBinding()
        initViews()
    }

    private fun doBinding() {
        mBinder = getViewDataBinding()
        mBinder?.viewModel = mainViewModel
        mBinder?.navigator = this
        mBinder?.viewModel?.setNavigator(this)
    }

    private fun initViews() {
        fetchMovies()
        swipe_refresh?.setColorSchemeResources(android.R.color.holo_orange_dark)
        swipe_refresh?.setOnRefreshListener {
            fetchMovies()
        }
    }

    private fun fetchMovies() {
        pd = ProgressBar(this, null, android.R.attr.progressBarStyleLarge)
        pd?.visibility = View.VISIBLE
        loadMovies()
    }

    private fun loadMovies() {
        try {
            if (BuildConfig.MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(applicationContext, "No Api Key", Toast.LENGTH_SHORT).show()
                pd?.visibility = View.GONE
                return
            }

            val client = Client()
            val movieResponseCall: Call<Response> = client.getApi().getTopRatedMovies(BuildConfig.MOVIE_DB_API_TOKEN)
            movieResponseCall.enqueue(object : Callback<Response> {
                override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                    val movies = response.body()?.result
                    if (movies == null) {
                        pd?.visibility = View.GONE
                        Toast.makeText(applicationContext, "No movies today", Toast.LENGTH_SHORT).show()
                    }

                    val adapter = movies?.let { MovieAdapter(applicationContext, it, this@MainActivity)}
                    recycler_view?.smoothScrollToPosition(0)
                    recycler_view.layoutManager = GridLayoutManager(this@MainActivity, 2)
                    recycler_view.adapter = adapter
                    if (swipe_refresh.isRefreshing) {
                        swipe_refresh.isRefreshing = false
                    }
                    pd?.visibility = View.GONE
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    Log.v("Error", t.message!!)
                    Toast.makeText(this@MainActivity, "Error fetching data", Toast.LENGTH_SHORT).show()
                    if (swipe_refresh.isRefreshing) {
                        swipe_refresh.isRefreshing = false
                    }
                    pd?.visibility = View.GONE
                }
            })
        } catch (e: Exception) {
            Log.d("Error:", e.message!!)
            Toast.makeText(this@MainActivity, e.toString(), Toast.LENGTH_SHORT).show()
            if (swipe_refresh.isRefreshing) {
                swipe_refresh.isRefreshing = false
            }
            pd?.visibility = View.GONE
        }
    }
}
