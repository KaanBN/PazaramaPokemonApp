package com.example.pazaramapokemonapp.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pazaramapokemonapp.databinding.PokedexItemLayoutBinding
import com.example.pazaramapokemonapp.domain.model.Pokemon
import com.example.pazaramapokemonapp.presentation.home.HomeUiState

class PokemonListAdapter(
    private val listener: Listener
) : ListAdapter<Pokemon, PokemonListAdapter.PokemonListViewHolder>(MovieDiffUtil()) {

    interface Listener {
        fun onItemClick(pokemonId: Int, pokemonName: String, pokemonImageUrl: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonListViewHolder {
        val binding = PokedexItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PokemonListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonListViewHolder, position: Int) {
        val itemView = holder.itemView
        itemView.post {
            val layoutParams = itemView.layoutParams
            layoutParams.height = itemView.width
            itemView.layoutParams = layoutParams
        }

        val pokemon = getItem(position)
        holder.bindItems(pokemon)
    }

    inner class PokemonListViewHolder(private val binding: PokedexItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItems(pokemon: Pokemon) {
            val pokemonId = pokemon.url.split("/".toRegex()).dropLast(1).last()
            val threeDigitPokemonId = String.format("%03d", pokemonId.toInt())

            binding.root.setOnClickListener {
                listener.onItemClick(
                    pokemonId.toInt(),
                    pokemon.name,
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$pokemonId.png"
                )
            }

            binding.pokemonNumber.text = "#$threeDigitPokemonId"
            binding.pokemonName.text = pokemon.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    java.util.Locale.getDefault()
                ) else it.toString()
            }

            Glide.with(binding.root.context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$pokemonId.png")
                .placeholder(com.example.pazaramapokemonapp.R.drawable.balbazar)
                .into(binding.pokemonImage)        }
    }
}

private class MovieDiffUtil : DiffUtil.ItemCallback<Pokemon>() {
    override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}
