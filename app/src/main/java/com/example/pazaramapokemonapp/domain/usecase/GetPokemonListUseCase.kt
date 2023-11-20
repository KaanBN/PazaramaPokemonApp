package com.example.pazaramapokemonapp.domain.usecase

import com.example.pazaramapokemonapp.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke() = pokemonRepository.getPokemonList()
}