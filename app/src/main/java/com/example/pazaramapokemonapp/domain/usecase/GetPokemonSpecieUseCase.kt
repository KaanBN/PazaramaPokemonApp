package com.example.pazaramapokemonapp.domain.usecase

import com.example.pazaramapokemonapp.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonSpecieUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(pokemonId: Int) = pokemonRepository.getPokemonSpecies(pokemonId)
}