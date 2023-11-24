package com.example.pazaramapokemonapp.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.pazaramapokemonapp.data.repository.FakePokemonRepository
import com.example.pazaramapokemonapp.domain.usecase.GetPokemonDetailUseCase
import com.example.pazaramapokemonapp.domain.usecase.GetPokemonSpecieUseCase
import com.example.pazaramapokemonapp.presentation.detail.PokemonDetailViewModel
import com.example.pazaramapokemonapp.rules.MainDispatcherRule
import com.example.pazaramapokemonapp.util.NavArgs
import com.google.common.truth.Truth.assertThat
import com.example.pazaramapokemonapp.util.dispatcher.DispatcherProvider
import com.example.pazaramapokemonapp.util.dispatcher.TestDefaultDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: PokemonDetailViewModel
    private lateinit var pokemonRepository: FakePokemonRepository
    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        pokemonRepository = FakePokemonRepository()
        dispatcherProvider = TestDefaultDispatcher()
        val savedStateHandle = SavedStateHandle(
            mapOf(
                NavArgs.pokemonId to 1
            )
        )
        viewModel = PokemonDetailViewModel(
            getPokemonDetailUseCase = GetPokemonDetailUseCase(pokemonRepository),
            getPokemonSpecieUseCase = GetPokemonSpecieUseCase(pokemonRepository),
            savedStateHandle = savedStateHandle,
            dispatcherProvider = dispatcherProvider
        )
    }

    @Test
    fun initial_state_should_be_loading() {
        val uiState = viewModel.uiState.value
        assertTrue(uiState.isLoading)
        assertEquals(null, uiState.errorMessage)
        assertEquals(null, uiState.pokemonDetail)
        assertEquals(null, uiState.pokemonSpecie)
    }

    @Test
    fun whenViewModelInit_getMoviesDetailReturnFalse_updateState() = runTest {
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

    /*@Test
    fun whenViewModelInit_getMoviesDetailReturnSuccess_updateState() = runTest {
        viewModel.uiState.test {
            val uiState = awaitItem()
            assertThat(uiState.isLoading).isTrue()
            advanceUntilIdle()
            val uiState2 = awaitItem()
            assertThat(uiState2.isLoading).isFalse()
            assertThat(uiState2.pokemonDetail).isNotNull()
            assertThat(uiState2.errorMessage).isNull()
            assertThat(uiState2.pokemonSpecie).isNull()

        }
    }*/

}