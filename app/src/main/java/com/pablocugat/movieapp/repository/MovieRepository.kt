package com.pablocugat.movieapp.repository

import com.pablocugat.movieapp.data.model.MovieList

interface MovieRepository {
    suspend fun getUpcomingMovies():MovieList
    suspend fun getTopRatedMovies():MovieList
    suspend fun getPopularMovies():MovieList
}