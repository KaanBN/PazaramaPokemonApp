package com.example.pazaramapokemonapp.data.repository

import com.example.pazaramapokemonapp.data.service.PokemonApi
import com.example.pazaramapokemonapp.domain.model.Pokemon
import com.example.pazaramapokemonapp.domain.repository.PokemonRepository
import com.example.pazaramapokemonapp.util.Resource
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val pokemonApi: PokemonApi
) : PokemonRepository {
    override suspend fun getPokemonList(): Resource<List<Pokemon>> {
        return try {
            val response = pokemonApi.getPokemonList()
            if (response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    Resource.Success(resultResponse.results)
                } ?: Resource.Error("An unknown error occurred", null)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }


}
