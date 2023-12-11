package com.example.movieapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieapp.data.local.entity.MovieEntity

@Dao
interface MoviesDao {
    @Query("SELECT * FROM movies")
    fun getFavoritedMovies(): LiveData<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveMovies(movie: MovieEntity)

    @Query("DELETE FROM movies WHERE title = :movieTitle")
    suspend fun deleteMovies(movieTitle: String)

    @Query("SELECT EXISTS(SELECT * FROM movies WHERE title = :title)")
    fun isMovieFavorite(title: String): LiveData<Boolean>
}