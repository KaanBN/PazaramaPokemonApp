package com.example.pazaramapokemonapp.domain.repository

import com.example.pazaramapokemonapp.domain.model.Pokemon
import com.example.pazaramapokemonapp.domain.model.PokemonDetail
import com.example.pazaramapokemonapp.domain.model.PokemonSpecie
import com.example.pazaramapokemonapp.util.Resource

interface PokemonRepository {
    suspend fun getPokemonList(): Resource<List<Pokemon>>

    suspend fun getPokemonDetail(id: Int): Resource<PokemonDetail>

    suspend fun getPokemonSpecies(id: Int): Resource<PokemonSpecie>
}