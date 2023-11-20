package com.example.pazaramapokemonapp.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pazaramapokemonapp.databinding.FragmentHomeBinding
import com.example.pazaramapokemonapp.domain.model.Pokemon
import com.example.pazaramapokemonapp.presentation.home.adapter.PokemonListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), PokemonListAdapter.Listener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var pokemonListAdapter: PokemonListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pokemonListAdapter = PokemonListAdapter(this)
        binding.recyclerView.adapter = pokemonListAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        addTextChangedListener()
        collectUiState()
        binding.errorView.btnRetry.setOnClickListener {
            viewModel.retry()
        }
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    binding.progressBar.isVisible = uiState.isLoading
                    uiState.errorMessage?.let {
                        binding.errorView.root.isVisible = true
                        binding.errorView.txtError.text = it
                    } ?: kotlin.run {
                        binding.errorView.root.isVisible = false
                    }
                    pokemonListAdapter.submitList(uiState.pokemons)
                }
            }
        }
    }

    private fun addTextChangedListener() {
        binding.searchEditText.addTextChangedListener {
            viewModel.setQuery(it.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(pokemon: Pokemon) {
        /*val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(movie.imdbID)
        findNavController().navigate(action)*/
        println("pokemon clicked")
    }
}


