package com.example.pazaramapokemonapp.data.mapper

import com.example.pazaramapokemonapp.data.dto.PokemonDetailDto
import com.example.pazaramapokemonapp.domain.model.PokemonDetail

fun PokemonDetailDto.toPokemonDetail(): PokemonDetail {
    return PokemonDetail(
        name = name,
        height = height,
        weight = weight,
        stats = stats,
        types = types,
        sprites = sprites,
        abilities = abilities
    )
}