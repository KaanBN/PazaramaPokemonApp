package com.example.pazaramapokemonapp.domain.model

import com.example.pazaramapokemonapp.data.dto.PokemonDetailDto

class PokemonDetail(
    val name: String,
    val height: Int,
    val weight: Int,
    val stats: List<PokemonDetailDto.Stat>,
    val types: List<PokemonDetailDto.Type>,
    val sprites: PokemonDetailDto.Sprites,
    val abilities: List<PokemonDetailDto.Ability>
)