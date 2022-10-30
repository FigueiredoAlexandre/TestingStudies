package com.example.pokedex.domain.repository

import com.example.pokedex.domain.domain_object.PokemonDomain
import com.example.pokedex.domain.query_object.PokeQuery
import kotlinx.coroutines.flow.Flow

interface ListOfPokemonsRepository {

    suspend fun getPokemons(
        query: PokeQuery
    ): Flow<List<PokemonDomain.Pokemon>>
}