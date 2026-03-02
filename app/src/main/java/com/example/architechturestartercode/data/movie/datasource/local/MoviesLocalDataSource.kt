package com.example.architechturestartercode.data.movie.datasource.local

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.architechturestartercode.data.db.AppDatabase
import com.example.architechturestartercode.data.movie.model.Movie
import kotlinx.coroutines.flow.Flow

class MoviesLocalDataSource(context: Context) {
    private val moviesDao: MoviesDao = AppDatabase.getInstance(context).moviesDao()

    suspend fun insertMovie(movie: Movie) {
        moviesDao.insertMovies(movie)
    }

    suspend fun deleteMovie(movie: Movie) {
        moviesDao.deleteMovies(movie)
    }

    fun getAllMovies(): Flow<List<Movie>> {
        return moviesDao.getAllMovies()
    }
}

