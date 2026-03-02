package com.example.architechturestartercode.presentation.favmovies.view_model

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.architechturestartercode.data.movie.MoviesRepository
import com.example.architechturestartercode.data.movie.model.Movie
import com.example.architechturestartercode.data.movie.model.MovieResponse
import com.example.architechturestartercode.presentation.allmovies.view_model.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavViewModel(
                   private val moviesRepository: MoviesRepository
) : ViewModel() {

    val uiState: StateFlow<UiState> = moviesRepository.getAllFavMovies()
        .map { movies ->
            UiState.Success(movies) as UiState
        }
        .catch { e ->
            emit(UiState.Error(e.message ?: "Could not load favorites"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    fun deleteFavMovie(movie: Movie) {
        viewModelScope.launch {
            moviesRepository.deleteMovieFromFav(movie)
        }
    }
}

class FavViewModelFactory(
    private val repository: MoviesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

