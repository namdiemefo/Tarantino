package com.naemo.tarantino.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.naemo.tarantino.R
import com.naemo.tarantino.api.model.Movie
import kotlinx.android.synthetic.main.movie_card.view.*
import javax.inject.Inject

class MovieAdapter(context: Context, private var movieList: ArrayList<Movie>, private var itemClickListener: ItemClickListener) : RecyclerView.Adapter<MovieAdapter.MoviesViewHolder>() {

    var context: Context? = null
        @Inject set

    init {
        this.context = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_card, parent, false)
        return MoviesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {

        val mList = movieList[position]
        val originalTitle = mList.originalTitle
        val thumbNail = "https://image.tmdb.org/t/p/w500" + mList.posterPath
        val releaseDate = mList.releaseDate
        val overView = mList.overview
        val id = mList.id
        val rating = mList.voteAverage

        holder.title.text = originalTitle
         Glide.with(context!!).load(thumbNail).into(holder.thumbNail)
        holder.releaseDate.text = releaseDate

        holder.movieFrame.setOnClickListener { itemClickListener.onItemClicked(id, originalTitle, thumbNail, releaseDate, overView, rating) }
    }

    class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.title
        val thumbNail: ImageView = itemView.thumbnail
        val releaseDate: TextView = itemView.release_date
        val movieFrame: CardView = itemView.movie_frame
    }

    interface ItemClickListener {
        fun onItemClicked(id: Int, originalTitle: String, posterPath: String, releaseDate: String, overView: String, rating: Double)
    }

}