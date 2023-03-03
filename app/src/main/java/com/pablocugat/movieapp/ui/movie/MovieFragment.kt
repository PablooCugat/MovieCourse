package com.pablocugat.movieapp.ui.movie

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.pablocugat.movieapp.R
import com.pablocugat.movieapp.core.Resource
import com.pablocugat.movieapp.data.local.AppDatabase
import com.pablocugat.movieapp.data.local.LocalMovieDataSource
import com.pablocugat.movieapp.data.model.Movie
import com.pablocugat.movieapp.data.remote.RemoteMovieDataSource
import com.pablocugat.movieapp.databinding.FragmentMovieBinding
import com.pablocugat.movieapp.presentation.MovieViewModel
import com.pablocugat.movieapp.presentation.MovieViewModelFactory
import com.pablocugat.movieapp.repository.MovieRepositoryImpl
import com.pablocugat.movieapp.repository.RetrofitClient
import com.pablocugat.movieapp.ui.movie.adapters.MovieAdapter
import com.pablocugat.movieapp.ui.movie.adapters.concat.PopularConcatAdapter
import com.pablocugat.movieapp.ui.movie.adapters.concat.TopRatedConcatAdapter
import com.pablocugat.movieapp.ui.movie.adapters.concat.UpcomingConcatAdapter

class MovieFragment : Fragment(R.layout.fragment_movie), MovieAdapter.OnMovieClickListener {

    private lateinit var binding: FragmentMovieBinding
    private val viewModel by viewModels<MovieViewModel> {
        MovieViewModelFactory(
            MovieRepositoryImpl(
                RemoteMovieDataSource(RetrofitClient.webservice),
                LocalMovieDataSource(AppDatabase.getDatabase(requireContext()).movieDao())
            )
        )
    }

    private lateinit var concatAdapter: ConcatAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieBinding.bind(view)

        concatAdapter = ConcatAdapter()

        viewModel.fetchMainScreenMovies().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    concatAdapter.apply {
                        addAdapter(
                            0,
                            UpcomingConcatAdapter(
                                MovieAdapter(
                                    result.data.first.results,
                                    this@MovieFragment
                                )
                            )
                        )
                        addAdapter(
                            1,
                            TopRatedConcatAdapter(
                                MovieAdapter(
                                    result.data.second.results,
                                    this@MovieFragment
                                )
                            )
                        )
                        addAdapter(
                            2,
                            PopularConcatAdapter(
                                MovieAdapter(
                                    result.data.third.results,
                                    this@MovieFragment
                                )
                            )
                        )
                    }

                    binding.rvMovies.adapter = concatAdapter
                }
                is Resource.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d("Error", "${result.exception}")
                }
            }
        })
    }

    override fun OnMovieClick(movie: Movie) {
        val action = MovieFragmentDirections.actionMovieFragmentToMovieDetailFragment(
            movie.poster_path,
            movie.backdrop_path,
            movie.vote_average.toFloat(),
            movie.vote_count,
            movie.overview,
            movie.title,
            movie.original_language,
            movie.release_date,
        )
        findNavController().navigate(action)
    }
}
