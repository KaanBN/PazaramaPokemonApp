package com.example.pazaramapokemonapp.presentation.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pazaramapokemonapp.R
import com.example.pazaramapokemonapp.databinding.FragmentHomeBinding
import com.example.pazaramapokemonapp.domain.model.Pokemon
import com.example.pazaramapokemonapp.presentation.home.adapter.PokemonListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
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
        requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.primary)
        pokemonListAdapter = PokemonListAdapter(this)
        binding.recyclerView.adapter = pokemonListAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        addTextChangedListener()
        collectUiState()
        binding.errorView.btnRetry.setOnClickListener {
            viewModel.retry()
        }

        binding.filterImageButton.post {
            val layoutParams = binding.filterImageButton.layoutParams
            layoutParams.width = binding.filterImageButton.height
            binding.filterImageButton.layoutParams = layoutParams
        }

        binding.filterImageButton.setOnClickListener {
            showAlertDialog()
        }

        binding.searchEditText.text?.let {
            if (it.isNotEmpty()) {
                binding.clearQueryImageButton.visibility = View.VISIBLE
            }
        }

        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.clearQueryImageButton.visibility = View.VISIBLE
            } else {
                binding.clearQueryImageButton.visibility = View.GONE
            }
        }

        binding.clearQueryImageButton.setOnClickListener {
            binding.searchEditText.clearFocus()
            binding.searchEditText.text.clear()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.searchEditText.text?.let {
            if (it.isNotEmpty()) {
                binding.clearQueryImageButton.visibility = View.VISIBLE
            }
        }
    }

    private fun showAlertDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.custom_alert_dialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val buttonLocation = IntArray(2)
        binding.filterImageButton.getLocationOnScreen(buttonLocation)
        val y = buttonLocation[1]
        val height = binding.filterImageButton.height
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setGravity(android.view.Gravity.TOP or android.view.Gravity.END)
        window?.attributes?.y = y + height / 2
        dialog.show()

        if (viewModel.uiState.value.filter == "Name") {
            dialog.findViewById<RadioButton>(R.id.radio_name).isChecked = true
        } else {
            dialog.findViewById<RadioButton>(R.id.radio_number).isChecked = true
        }

        dialog.findViewById<RadioButton>(R.id.radio_name).setOnClickListener {
            dialog.dismiss()
            binding.root.post{
                viewModel.sortByName()
                viewModel.setFilter("Name")
            }
            binding.filterImageButton.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.name))
        }

        dialog.findViewById<RadioButton>(R.id.radio_number).setOnClickListener {
            dialog.dismiss()
            binding.root.post{
                viewModel.sortByNumber()
                viewModel.setFilter("Number")
            }
            binding.filterImageButton.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.number))
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
                    binding.filterImageButton.isEnabled = uiState.pokemons.isNotEmpty()
                    binding.filterImageButton.setImageDrawable(ContextCompat.getDrawable(requireContext(), if (uiState.filter == "Name") R.drawable.name else R.drawable.number))
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

    override fun onItemClick(pokemonId: Int, pokemonName: String, pokemonImageUrl: String) {
//        val availablePokemonsIdList = viewModel.uiState.value.pokemons.map { it.url.split("/".toRegex()).dropLast(1).last().toInt() }
        val availablePokemonsIdArray = viewModel.uiState.value.pokemons.map { it.url.split("/".toRegex()).dropLast(1).last().toInt() }.toIntArray()
        val action = HomeFragmentDirections.actionHomeFragmentToPokemonDetailFragment(pokemonId, pokemonName, pokemonImageUrl, availablePokemonsIdArray)
        findNavController().navigate(action)
    }
}


