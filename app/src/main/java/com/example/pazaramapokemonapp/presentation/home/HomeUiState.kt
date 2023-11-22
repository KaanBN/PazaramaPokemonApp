package com.example.pazaramapokemonapp.presentation.home

import com.example.pazaramapokemonapp.domain.model.Pokemon

data class HomeUiState(
    val pokemons: List<Pokemon> = emptyList(),
    val isLoading: Boolean = true,
    val searchQuery: String = "Batman",
    val searchLimit: Int = 20,
    val searchOffset: Int = 0,
    val errorMessage: String? = null,
    val filter: String? = null
)