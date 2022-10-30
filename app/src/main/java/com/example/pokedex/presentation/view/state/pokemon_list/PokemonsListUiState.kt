package com.example.pokedex.presentation.view.state.pokemon_list

import com.example.pokedex.domain.domain_object.PokemonDomain

data class PokemonsListUiState(
    val pokemons:List<PokemonDomain.Pokemon> = emptyList(),
    val isQuerying: Boolean = false,
    val error:String = ""
)