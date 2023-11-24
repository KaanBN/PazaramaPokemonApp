package com.example.pazaramapokemonapp.data.repository

import com.example.pazaramapokemonapp.data.dto.PokemonDetailDto
import com.example.pazaramapokemonapp.data.dto.PokemonSpecieDto
import com.example.pazaramapokemonapp.domain.model.Pokemon
import com.example.pazaramapokemonapp.domain.model.PokemonDetail
import com.example.pazaramapokemonapp.domain.model.PokemonSpecie
import com.example.pazaramapokemonapp.domain.repository.PokemonRepository
import com.example.pazaramapokemonapp.util.Resource
import kotlinx.coroutines.delay

const val NETWORK_DELAY = 1000L
class FakePokemonRepository : PokemonRepository {
    var isReturnNetworkError = false
    override suspend fun getPokemonList(): Resource<List<Pokemon>> {
        delay(NETWORK_DELAY)
        return if (isReturnNetworkError) {
            Resource.Error("Error", null)
        } else {
            Resource.Success(
                listOf(
                    Pokemon(name= "bulbasaur", url= "https://pokeapi.co/api/v2/pokemon/1/")
                )
            )
        }
    }

    override suspend fun getPokemonDetail(id: Int): Resource<PokemonDetail> {
        delay(NETWORK_DELAY)
        return if (isReturnNetworkError) {
            Resource.Error("Error", null)
        } else {
            Resource.Success(
                PokemonDetail(
                    name= "bulbasaur",
                    height= 7,
                    weight= 69,
                    types= listOf(
                        PokemonDetailDto.Type(
                            slot= 1,
                            type= PokemonDetailDto.Type.Type(
                                name= "grass",
                                url= "https://pokeapi.co/api/v2/type/12/"
                            )
                        ),
                        PokemonDetailDto.Type(
                            slot= 2,
                            type= PokemonDetailDto.Type.Type(
                                name= "poison",
                                url= "https://pokeapi.co/api/v2/type/4/"
                            )
                        ),
                    ),
                    stats= listOf(
                        PokemonDetailDto.Stat(
                            baseStat = 45,
                            effort = 0,
                            stat = PokemonDetailDto.Stat.Stat(
                                name = "hp",
                                url = "https://pokeapi.co/api/v2/stat/1/"
                            )
                        ),
                        PokemonDetailDto.Stat(
                            baseStat = 49,
                            effort = 0,
                            stat = PokemonDetailDto.Stat.Stat(
                                name = "attack",
                                url = "https://pokeapi.co/api/v2/stat/2/"
                            )
                        ),
                        PokemonDetailDto.Stat(
                            baseStat = 49,
                            effort = 0,
                            stat = PokemonDetailDto.Stat.Stat(
                                name = "defense",
                                url = "https://pokeapi.co/api/v2/stat/3/"
                            )
                        ),
                        PokemonDetailDto.Stat(
                            baseStat = 65,
                            effort = 1,
                            stat = PokemonDetailDto.Stat.Stat(
                                name = "special-attack",
                                url = "https://pokeapi.co/api/v2/stat/4/"
                            )
                        ),
                        PokemonDetailDto.Stat(
                            baseStat = 65,
                            effort = 0,
                            stat = PokemonDetailDto.Stat.Stat(
                                name = "special-defense",
                                url = "https://pokeapi.co/api/v2/stat/5/"
                            )
                        ),
                        PokemonDetailDto.Stat(
                            baseStat = 45,
                            effort = 0,
                            stat = PokemonDetailDto.Stat.Stat(
                                name = "special-defense",
                                url = "https://pokeapi.co/api/v2/stat/6/"
                            )
                        ),
                    ),
                    abilities= listOf(
                        PokemonDetailDto.Ability(
                            ability= PokemonDetailDto.Ability.Ability(
                                name= "overgrow",
                                url= "https://pokeapi.co/api/v2/ability/65/"
                            ),
                            isHidden= false,
                            slot= 1
                        ),
                        PokemonDetailDto.Ability(
                            ability= PokemonDetailDto.Ability.Ability(
                                name= "chlorophyll",
                                url= "https://pokeapi.co/api/v2/ability/34/"
                            ),
                            isHidden= true,
                            slot= 3
                        ),
                    )
                )
            )
        }
    }

    override suspend fun getPokemonSpecies(id: Int): Resource<PokemonSpecie> {
        delay(NETWORK_DELAY)
        return if (isReturnNetworkError) {
            Resource.Error("Error", null)
        } else {
            Resource.Success(
                PokemonSpecie(
                    color = PokemonSpecieDto.Color(
                        name = "green",
                        url = "https://pokeapi.co/api/v2/pokemon-color/5/"
                    ),
                    flavor_text_entries = listOf(
                        PokemonSpecieDto.FlavorTextEntry(
                            flavorText = "A strange seed was planted on its back at birth. The plant sprouts and grows with this POKéMON.",
                            language = PokemonSpecieDto.FlavorTextEntry.Language(
                                name = "en",
                                url = "https://pokeapi.co/api/v2/language/9/"
                            ),
                            version = PokemonSpecieDto.FlavorTextEntry.Version(
                                name = "red",
                                url = "https://pokeapi.co/api/v2/version/1/"
                            )
                        )
                    ),
                    name = "bulbasaur",
                )
            )
        }
    }

}