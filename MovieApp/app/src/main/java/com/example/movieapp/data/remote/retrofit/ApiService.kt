package com.example.movieapp.data.remote.retrofit

import com.example.movieapp.data.remote.response.DetailMoviesResponse
import com.example.movieapp.data.remote.response.MoviesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/top_rated?language=en-US")
    fun getMovies(): Call<MoviesResponse>

    @GET("movie/{id}?language=en-US")
    fun getDetailMovie(
        @Path("id") id: Long
    ) : Call<DetailMoviesResponse>

    @GET("search/movie?language=en-US")
    fun searchMovie(
        @Query("query") query: String
    ) : Call<MoviesResponse>
}