package com.example.pazaramapokemonapp.domain.model

import com.example.pazaramapokemonapp.data.dto.PokemonSpecieDto

class PokemonSpecie (
    val flavor_text_entries: List<PokemonSpecieDto.FlavorTextEntry>,
    val color: PokemonSpecieDto.Color,
    val name: String
)