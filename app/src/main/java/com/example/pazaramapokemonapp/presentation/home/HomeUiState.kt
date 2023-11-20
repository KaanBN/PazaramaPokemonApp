package com.example.pazaramapokemonapp.presentation.home

import com.example.pazaramapokemonapp.domain.model.Pokemon

data class HomeUiState(
    val pokemons: List<Pokemon> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "Batman",
    val errorMessage: String? = null
)