package com.example.pazaramapokemonapp.data.service

import com.example.pazaramapokemonapp.domain.model.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET

interface PokemonApi {
    @GET("pokemon")
    suspend fun getPokemonList(): Response<PokemonResponse>
}