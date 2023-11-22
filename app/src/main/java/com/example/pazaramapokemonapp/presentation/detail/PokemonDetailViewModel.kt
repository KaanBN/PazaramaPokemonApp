package com.example.pazaramapokemonapp.presentation.detail

import androidx.lifecycle.ViewModel
import com.example.pazaramapokemonapp.domain.usecase.GetPokemonListUseCase
import com.example.pazaramapokemonapp.util.dispatcher.DispatcherProvider
import javax.inject.Inject

class PokemonDetailViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

}
