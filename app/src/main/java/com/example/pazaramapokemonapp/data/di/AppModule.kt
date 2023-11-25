package com.example.pazaramapokemonapp.data.di

import com.example.pazaramapokemonapp.data.repository.PokemonRepositoryImpl
import com.example.pazaramapokemonapp.data.service.PokemonApi
import com.example.pazaramapokemonapp.domain.repository.PokemonRepository
import com.example.pazaramapokemonapp.util.constants.BASE_URL
import com.example.pazaramapokemonapp.util.dispatcher.DefaultDispatcher
import com.example.pazaramapokemonapp.util.dispatcher.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun providePokemonApi(
        okHttpClient: OkHttpClient
    ): PokemonApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApi::class.java)
    }

    @Provides
    @Singleton
    fun providePokemonRepository(
        pokemonApi: PokemonApi
    ): PokemonRepository {
        return PokemonRepositoryImpl(pokemonApi)
    }

    @Provides
    @Singleton
    fun provideDispatchers(): DispatcherProvider {
        return DefaultDispatcher()
    }
}