package com.example.pazaramapokemonapp.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pazaramapokemonapp.domain.model.PokemonDetail
import com.example.pazaramapokemonapp.domain.usecase.GetPokemonDetailUseCase
import com.example.pazaramapokemonapp.domain.usecase.GetPokemonSpecieUseCase
import com.example.pazaramapokemonapp.util.NavArgs
import com.example.pazaramapokemonapp.util.Resource
import com.example.pazaramapokemonapp.util.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
    private val getPokemonSpecieUseCase: GetPokemonSpecieUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private val pokemonIdState = MutableStateFlow<Int?>(null)
    private val _uiState = MutableStateFlow(PokemonDetailUiState())
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.update { it.copy(isLoading = false, errorMessage = throwable.message) }
    }

    init {
        val pokemonId = savedStateHandle.get<Int>(NavArgs.pokemonId)
        pokemonIdState.update { pokemonId }
        pokemonId?.let {
            getPokemonDetail(it)
            getPokemonStory(it)
        }
    }

    private fun getPokemonDetail(pokemonId: Int) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(dispatcherProvider.IO + exceptionHandler) {
            when (val resource = getPokemonDetailUseCase(pokemonId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            pokemonDetail = resource.data
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = resource.errorMessage ?: "An unexpected error occurred"
                        )
                    }
                }
            }
        }
    }

    private fun getPokemonStory(pokemonId: Int) {
        viewModelScope.launch(dispatcherProvider.IO + exceptionHandler) {
            when (val resource = getPokemonSpecieUseCase(pokemonId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            pokemonSpecie = resource.data
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = resource.errorMessage ?: "An unexpected error occurred"
                        )
                    }
                }
            }
        }
    }

    fun retry() {
        pokemonIdState.value?.let {
            getPokemonDetail(it)
            getPokemonStory(it)
        }
    }
}
