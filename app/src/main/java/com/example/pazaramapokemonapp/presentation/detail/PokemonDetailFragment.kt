package com.example.pazaramapokemonapp.presentation.detail

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.pazaramapokemonapp.R
import com.example.pazaramapokemonapp.databinding.FragmentPokemonDetailBinding
import com.example.pazaramapokemonapp.domain.model.PokemonDetail
import com.example.pazaramapokemonapp.domain.model.PokemonSpecie
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PokemonDetailFragment : Fragment() {

    private var _binding: FragmentPokemonDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PokemonDetailViewModel by viewModels()

    private val args: PokemonDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val originalNavBarColor = requireActivity().window.navigationBarColor
        super.onViewCreated(view, savedInstanceState)
        collectUiState()
        arrangeDetails()
        binding.errorView.btnRetry.setOnClickListener {
            viewModel.retry()
        }
        binding.backButton.setOnClickListener {
            requireActivity().window.statusBarColor = resources.getColor(R.color.primary)
            requireActivity().window.navigationBarColor = originalNavBarColor
            requireActivity().onBackPressed()
        }
    }

    private fun arrangeDetails(){
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
            binding.atkValTV.layoutParams.width = highestValueWidth
            binding.defValTV.layoutParams.width = highestValueWidth
            binding.satkValTV.layoutParams.width = highestValueWidth
            binding.sdefValTV.layoutParams.width = highestValueWidth
            binding.spdValTV.layoutParams.width = highestValueWidth

        }
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    binding.progressBar.isVisible = uiState.isLoading
                    binding.scrollView.isInvisible = uiState.isLoading
                    uiState.errorMessage?.let {
                        binding.errorView.root.isVisible = true
                        binding.errorView.txtError.text = it
                    }
                    uiState.pokemonDetail?.let {
                        binding.errorView.root.isVisible = false
                        bindPokemonDetail(it)
                    }
                    uiState.pokemonSpecie?.let {
                        binding.errorView.root.isVisible = false
                        bindPokemonStory(it)
                    }
                }
            }
        }
    }

    private fun bindPokemonStory(pokemonSpecie: PokemonSpecie) {
        val englishFlavorTextEntries = pokemonSpecie.flavor_text_entries.filter { it.language.name == "en" }
        val englishFlavorTextEntry = englishFlavorTextEntries[0]
        binding.pokemonStoryTV.text = englishFlavorTextEntry.flavorText.replace("\n", " ")
    }

    private fun bindPokemonDetail(pokemonDetail: PokemonDetail) {
        val floatWeight = pokemonDetail.weight.toFloat() / 10
        val floatHeight = pokemonDetail.height.toFloat() / 10

        binding.weightTV.text = "$floatWeight kg"
        binding.heightTV.text = "$floatHeight m"

        binding.hpValTV.text = pokemonDetail.stats[0].baseStat.toString()
        binding.atkValTV.text = pokemonDetail.stats[1].baseStat.toString()
        binding.defValTV.text = pokemonDetail.stats[2].baseStat.toString()
        binding.satkValTV.text = pokemonDetail.stats[3].baseStat.toString()
        binding.sdefValTV.text = pokemonDetail.stats[4].baseStat.toString()
        binding.spdValTV.text = pokemonDetail.stats[5].baseStat.toString()

        binding.hpProgressBar.progress = pokemonDetail.stats[0].baseStat
        binding.atkProgressBar.progress = pokemonDetail.stats[1].baseStat
        binding.defProgressBar.progress = pokemonDetail.stats[2].baseStat
        binding.satkProgressBar.progress = pokemonDetail.stats[3].baseStat
        binding.sdefProgressBar.progress = pokemonDetail.stats[4].baseStat
        binding.spdProgressBar.progress = pokemonDetail.stats[5].baseStat

        val pokemonTypes = pokemonDetail.types
        val color = pokemonTypes[0].type.name
        val wireframeItems = listOf(
            binding.hpProgressBar,
            binding.atkProgressBar,
            binding.defProgressBar,
            binding.satkProgressBar,
            binding.sdefProgressBar,
            binding.spdProgressBar,
        )

        val wireframeTvItems = listOf(
            binding.aboutTitlteTV,
            binding.baseStatsTitleTV,
            binding.hpTitleTV,
            binding.atkTitleTV,
            binding.defTitleTV,
            binding.satkTitleTV,
            binding.sdefTitleTV,
            binding.spdTitleTV,
        )

        binding.outterLayout.setBackgroundColor(resources.getColor(resources.getIdentifier(color, "color", requireContext().packageName)))

        wireframeTvItems.forEach {
            it.setTextColor(resources.getColor(resources.getIdentifier(color, "color", requireContext().packageName)))
        }

        wireframeItems.forEach {
            it.progressTintList = resources.getColorStateList(resources.getIdentifier(color, "color", requireContext().packageName))
            it.progressBackgroundTintList = resources.getColorStateList(resources.getIdentifier(color, "color", requireContext().packageName))
        }

        requireActivity().window.statusBarColor = resources.getColor(resources.getIdentifier(color, "color", requireContext().packageName))

        requireActivity().window.navigationBarColor = resources.getColor(resources.getIdentifier(color, "color", requireContext().packageName))

        val movesLayout = binding.movesLayout
        binding.movesLayout.removeAllViews()
        val abilities = pokemonDetail.abilities

        for (element in abilities) {
            val abilityName = element.ability.name
            val abilityTv = TextView(requireContext())
            abilityTv.text = abilityName.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    java.util.Locale.getDefault()
                ) else it.toString()
            }

            abilityTv.setTextColor(resources.getColor(R.color.dark_gray))
            abilityTv.textAlignment = View.TEXT_ALIGNMENT_CENTER
            abilityTv.textSize = 10f
            movesLayout.addView(abilityTv)
        }

        val types = pokemonDetail.types
        binding.typesLayout.removeAllViews()

        for (element in types) {
            val typeName = element.type.name
            val typeTv = TextView(requireContext())
            typeTv.text = typeName.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    java.util.Locale.getDefault()
                ) else it.toString()
            }

            val color = resources.getColor(resources.getIdentifier(typeName, "color", requireContext().packageName))
            typeTv.textAlignment = View.TEXT_ALIGNMENT_CENTER
            typeTv.textSize = 10f
            typeTv.setBackgroundResource(R.drawable.rounded_corner_background)
            typeTv.setTextColor(resources.getColor(R.color.white))
            typeTv.typeface = ResourcesCompat.getFont(requireContext(), R.font.poppins_bold)
            typeTv.backgroundTintList = resources.getColorStateList(resources.getIdentifier(typeName, "color", requireContext().packageName))
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(10, 0, 10, 0)
            typeTv.layoutParams = params
            val padding = 5
            typeTv.setPadding(20,padding,20,padding)

            binding.typesLayout.addView(typeTv)
        }
    }
}