package com.example.pazaramapokemonapp.presentation

import app.cash.turbine.test
import com.example.pazaramapokemonapp.data.repository.FakePokemonRepository
import com.example.pazaramapokemonapp.domain.usecase.GetPokemonListUseCase
import com.example.pazaramapokemonapp.presentation.home.HomeViewModel
import com.example.pazaramapokemonapp.rules.MainDispatcherRule
import com.example.pazaramapokemonapp.util.dispatcher.DispatcherProvider
import com.example.pazaramapokemonapp.util.dispatcher.TestDefaultDispatcher
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: HomeViewModel
    private lateinit var pokemonRepository: FakePokemonRepository
    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        dispatcherProvider = TestDefaultDispatcher()
        pokemonRepository = FakePokemonRepository()
        viewModel = HomeViewModel(
            getPokemonListUseCase = GetPokemonListUseCase(pokemonRepository),
            dispatcherProvider = dispatcherProvider
        )
    }

    @Test
    fun whenViewModelInit_getPokemonsReturnSuccess_shouldReturnListOfPokemons() = runTest {
        viewModel.uiState.test {
            val uiState = awaitItem()
            assertThat(uiState.isLoading).isTrue()
            advanceUntilIdle()
            val uiState2 = awaitItem()
            assertThat(uiState2.isLoading).isFalse()
            assertThat(uiState2.pokemons).isNotEmpty()
            assertThat(uiState2.errorMessage).isNull()
        }
    }

    @Test
    fun whenViewModelInit_getPokemonsReturnFalse_shouldReturnListOfPokemons() = runTest {
        pokemonRepository.isReturnNetworkError = true
        viewModel.uiState.test {
            val uiState = awaitItem()
            assertThat(uiState.isLoading).isTrue()
            advanceUntilIdle()
            val uiState2 = awaitItem()
            assertThat(uiState2.isLoading).isFalse()
            assertThat(uiState2.errorMessage).isNotNull()
        }
    }
}