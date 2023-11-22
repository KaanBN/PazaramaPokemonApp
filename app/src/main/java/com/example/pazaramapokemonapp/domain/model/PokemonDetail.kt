package com.example.pazaramapokemonapp.domain.model

import com.example.pazaramapokemonapp.data.dto.PokemonDetailDto

class PokemonDetail(
    val name: String,
    val url: String,
    val height: Int,
    val weight: Int,
    val stats: List<PokemonDetailDto.Stat.Stat>,
    val types: List<PokemonDetailDto.Type.Type>,
    val sprites: PokemonDetailDto.Sprites,
    val abilities: List<PokemonDetailDto.Ability.Ability>
)