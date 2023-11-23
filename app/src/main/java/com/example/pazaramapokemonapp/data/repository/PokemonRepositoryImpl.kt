package com.example.pazaramapokemonapp.data.repository

import com.example.pazaramapokemonapp.data.mapper.toPokemonDetail
import com.example.pazaramapokemonapp.data.mapper.toPokemonSpecie
import com.example.pazaramapokemonapp.data.service.PokemonApi
import com.example.pazaramapokemonapp.domain.model.Pokemon
import com.example.pazaramapokemonapp.domain.model.PokemonDetail
import com.example.pazaramapokemonapp.domain.model.PokemonSpecie
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

    override suspend fun getPokemonDetail(pokemonId: Int): Resource<PokemonDetail> {
        return try {
            val response = pokemonApi.getPokemonDetail(pokemonId)
            if (response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    Resource.Success(resultResponse.toPokemonDetail())
                } ?: Resource.Error("An unknown error occurred", null)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun getPokemonSpecies(pokemonId: Int): Resource<PokemonSpecie> {
        return try {
            val response = pokemonApi.getPokemonSpecies(pokemonId)
            if (response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    Resource.Success(resultResponse.toPokemonSpecie())
                } ?: Resource.Error("An unknown error occurred", null)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }


}
