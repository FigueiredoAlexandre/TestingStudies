package com.example.pokedex.domain.domain_object

sealed class PokemonDomain {
    data class Pokemon(
        val name:String,
        val url: String
    ):PokemonDomain()
    object Loader: PokemonDomain()
}