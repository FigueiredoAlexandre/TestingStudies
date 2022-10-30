package com.example.pokedex.domain.mapper

import com.example.pokedex.data.remote.response.PokemonItemResponse
import com.example.pokedex.data.remote.response.PokemonResponse
import com.example.pokedex.domain.domain_object.PokemonDomain

class PokemonResponseToPokemon: Mapper<PokemonItemResponse, PokemonDomain.Pokemon> {


    override fun convertTo(input: PokemonItemResponse): PokemonDomain.Pokemon {
        return PokemonDomain.Pokemon(
            name = input.name.orEmpty(),
            url = input.url.orEmpty()
        )
    }
}