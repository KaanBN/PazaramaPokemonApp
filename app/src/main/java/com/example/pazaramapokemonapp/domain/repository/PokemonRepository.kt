package com.example.pazaramapokemonapp.domain.repository

import com.example.pazaramapokemonapp.domain.model.Pokemon
import com.example.pazaramapokemonapp.util.Resource

interface PokemonRepository {
    suspend fun getPokemonList(): Resource<List<Pokemon>>
}