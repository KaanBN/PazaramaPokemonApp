package com.example.pazaramapokemonapp.presentation.detail

import com.example.pazaramapokemonapp.domain.model.PokemonDetail
import com.example.pazaramapokemonapp.domain.model.PokemonSpecie

data class PokemonDetailUiState(
    val isLoading: Boolean = false,
    val pokemonDetail: PokemonDetail? = null,
    val errorMessage: String? = null,
    val pokemonSpecie: PokemonSpecie? = null
)
