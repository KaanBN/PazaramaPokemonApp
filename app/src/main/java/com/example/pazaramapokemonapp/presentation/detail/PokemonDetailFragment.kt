package com.example.pazaramapokemonapp.presentation.detail

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.pazaramapokemonapp.R
import com.example.pazaramapokemonapp.databinding.FragmentPokemonDetailBinding
import com.example.pazaramapokemonapp.presentation.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PokemonDetailFragment : Fragment() {

    private var _binding: FragmentPokemonDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private val args: PokemonDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val displayMetrics = resources.displayMetrics
        val width = displayMetrics.widthPixels
        binding.pokemonImage.layoutParams.width = (width * 0.5).toInt()
        binding.pokemonImage.layoutParams.height = (width * 0.5).toInt()

        val pokemonName = args.pokemonName
        binding.pokemonNameTV.text = pokemonName.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                java.util.Locale.getDefault()
            ) else it.toString()
        }
        Glide.with(requireContext())
            .load(args.pokemonImageUrl)
            .into(binding.pokemonImage)
        val threeDigitsPokemonNumber = String.format("%03d", args.pokemonId)
        binding.pokemonNumberTV.text = "#$threeDigitsPokemonNumber"

        binding.cardView.post {
            binding.changePokemonLayout.y = binding.cardView.y - binding.changePokemonLayout.height / 2 - binding.buttonPrevious.width - binding.buttonPrevious.width / 2
            val layoutParams = binding.scrollView.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = binding.changePokemonLayout.height / 2 - binding.buttonPrevious.width - binding.buttonPrevious.width / 2
            binding.scrollView.layoutParams = layoutParams

            val hpTitleWidth = binding.hpTitleTV.width
            val attackTitleWidth = binding.atkTitleTV.width
            val defenseTitleWidth = binding.defTitleTV.width
            val sAttackTitleWidth = binding.satkTitleTV.width
            val sDefenseTitleWidth = binding.sdefTitleTV.width
            val speedTitleWidth = binding.spdTitleTV.width

            val highestTitleWidth = maxOf(hpTitleWidth, attackTitleWidth, defenseTitleWidth, sAttackTitleWidth, sDefenseTitleWidth, speedTitleWidth)
            binding.hpTitleTV.layoutParams.width = highestTitleWidth
            binding.atkTitleTV.layoutParams.width = highestTitleWidth
            binding.defTitleTV.layoutParams.width = highestTitleWidth
            binding.satkTitleTV.layoutParams.width = highestTitleWidth
            binding.sdefTitleTV.layoutParams.width = highestTitleWidth
            binding.spdTitleTV.layoutParams.width = highestTitleWidth

            val hpValueWidth = binding.hpValTV.width
            val attackValueWidth = binding.atkValTV.width
            val defenseValueWidth = binding.defValTV.width
            val sAttackValueWidth = binding.satkValTV.width
            val sDefenseValueWidth = binding.sdefValTV.width
            val speedValueWidth = binding.spdValTV.width

            val highestValueWidth = maxOf(hpValueWidth, attackValueWidth, defenseValueWidth, sAttackValueWidth, sDefenseValueWidth, speedValueWidth)
            binding.hpValTV.layoutParams.width = highestValueWidth
        }
    }
}