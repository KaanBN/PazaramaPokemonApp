package com.example.pazaramapokemonapp.data.mapper

import com.example.pazaramapokemonapp.data.dto.PokemonSpecieDto
import com.example.pazaramapokemonapp.domain.model.PokemonSpecie

fun PokemonSpecieDto.toPokemonSpecie(): PokemonSpecie {
    return PokemonSpecie(
        flavor_text_entries = flavorTextEntries,
        color = color,
    )
}