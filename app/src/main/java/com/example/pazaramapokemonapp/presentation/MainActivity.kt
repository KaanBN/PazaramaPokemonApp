package com.example.pazaramapokemonapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pazaramapokemonapp.databinding.ActivityMainBinding
import com.example.pazaramapokemonapp.domain.repository.PokemonRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var repository: PokemonRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
