package com.example.pazaramapokemonapp.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pazaramapokemonapp.domain.model.Pokemon
import com.example.pazaramapokemonapp.domain.usecase.GetPokemonListUseCase
import com.example.pazaramapokemonapp.util.Resource
import com.example.pazaramapokemonapp.util.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    private var searchJob: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.update { it.copy(isLoading = false, errorMessage = throwable.message) }
    }

    private val allPokemons = mutableListOf<Pokemon>()

    init {
//        getPokemons()
        fakeGetPokemons()
    }

    private fun getPokemons() {
        viewModelScope.launch(dispatcherProvider.IO + exceptionHandler) {
            _uiState.update { it.copy(isLoading = true) }
            when (val response = getPokemonListUseCase()) {
                is Resource.Success -> {
                    response.data?.let {
                        allPokemons.clear()
                        allPokemons.addAll(it)
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                pokemons = allPokemons,
                                errorMessage = null
                            )
                        }
                    } ?: kotlin.run {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "No Pokemon found"
                            )
                        }
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = response.errorMessage ?: "An unexpected error occurred"
                        )
                    }
                }
            }
        }
    }

    private fun fakeGetPokemons(){
        viewModelScope.launch(dispatcherProvider.IO + exceptionHandler) {
            _uiState.update { it.copy(isLoading = true) }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    pokemons = listOf(
                        Pokemon(
                            "Bulbasaur",
                            "https://pokeapi.co/api/v2/pokemon/1/"
                        ),
                    ),
                    errorMessage = null
                )
            }
        }
    }


    fun retry() {
        _uiState.update { it.copy(errorMessage = null) }
        getPokemons()
    }

    fun setQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch(dispatcherProvider.IO + exceptionHandler) {
            delay(300)
            if (query.isNotEmpty()) {
                if (_uiState.value.filter == "Name") {
                    filterByName(query)
                } else {
                    filterByNumber(query)
                }
            } else {
                _uiState.update { it.copy(pokemons = allPokemons) }
            }
        }
    }

    fun setFilter(filter: String) {
        Log.d("HomeViewModel", "setFilter: $filter")
        _uiState.update { it.copy(filter = filter) }
    }

    fun sortByName() {
        _uiState.update { it.copy(pokemons = allPokemons.sortedBy { it.name }) }
    }

    fun sortByNumber() {
        _uiState.update { it.copy(pokemons = allPokemons.sortedBy {
            it.url.substring(34, it.url.length - 1).toInt()
        }) }
    }

    fun filterByName(query: String) {
        _uiState.update { it.copy(pokemons = allPokemons.filter { it.name.contains(query) }) }
    }

    fun filterByNumber(query: String) {
        _uiState.update { it.copy(pokemons = allPokemons.filter {
            it.url.substring(34, it.url.length - 1).toInt().toString().contains(query)
        }) }
    }
}