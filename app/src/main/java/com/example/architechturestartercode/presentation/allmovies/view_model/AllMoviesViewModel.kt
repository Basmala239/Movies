package com.example.architechturestartercode.presentation.allmovies.view_model

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.architechturestartercode.data.movie.MoviesRepository
import com.example.architechturestartercode.data.movie.model.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState {
    object Loading : UiState()
    data class Success(val movies: List<Movie>) : UiState()
    data class Error(val message: String) : UiState()
}
class AllMoviesViewModel(
    app: Application
) : AndroidViewModel(app) {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<String>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val moviesRepository = MoviesRepository(app)

    init {
        getAllMovies()
    }

    fun getAllMovies() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val movies = moviesRepository.getAllMovies()
                _uiState.value = UiState.Success(movies)
            } catch (ex: Exception) {
                _uiState.value = UiState.Error(ex.message ?: "Unknown Error")
            }
        }
    }

    fun addToFav(movie: Movie) {
        viewModelScope.launch {
            moviesRepository.insertMovieToFav(movie)
            _eventFlow.emit("Added ${movie.title} to favorites")
        }
    }
}