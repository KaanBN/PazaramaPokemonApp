package com.example.pazaramapokemonapp.domain.model

import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)


data class PokemonResponse(
    @SerializedName("results") val results: List<Pokemon>
)