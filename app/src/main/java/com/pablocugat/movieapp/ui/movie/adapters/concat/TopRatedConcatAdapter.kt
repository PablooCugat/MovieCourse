package com.pablocugat.movieapp.ui.movie.adapters.concat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pablocugat.movieapp.core.BaseConcatHolder
import com.pablocugat.movieapp.databinding.TopRatedMoviesRowBinding
import com.pablocugat.movieapp.ui.movie.adapters.MovieAdapter

class TopRatedConcatAdapter (private val moviesAdapter: MovieAdapter):
    RecyclerView.Adapter<BaseConcatHolder<*>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseConcatHolder<*> {
        val itemBinding = TopRatedMoviesRowBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ConcatViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = 1


    override fun onBindViewHolder(holder: BaseConcatHolder<*>, position: Int) {
        when(holder){
            is ConcatViewHolder-> holder.bind(moviesAdapter)
        }
    }

    private inner class ConcatViewHolder(val binding: TopRatedMoviesRowBinding):
        BaseConcatHolder<MovieAdapter>(binding.root){
        override fun bind(adapter: MovieAdapter) {
            binding.rvTopRatedMovies.adapter= adapter
        }
    }
}