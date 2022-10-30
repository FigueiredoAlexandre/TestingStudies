package com.example.pokedex.data.repository

import com.example.pokedex.data.remote.api.PokemonApi
import com.example.pokedex.data.remote.response.PokemonItemResponse
import com.example.pokedex.domain.domain_object.PokemonDomain
import com.example.pokedex.domain.mapper.Mapper
import com.example.pokedex.domain.mapper.PokemonResponseToPokemon
import com.example.pokedex.domain.query_object.PokeQuery
import com.example.pokedex.domain.repository.ListOfPokemonsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ListOfPokemonsRepositoryImp(
    val api: PokemonApi,
    val mapper: Mapper<PokemonItemResponse, PokemonDomain.Pokemon>
) : ListOfPokemonsRepository {


    override suspend fun getPokemons(query: PokeQuery): Flow<List<PokemonDomain.Pokemon>> {
        return flow {
            api.getThePokemons(
                query.limit,
                query.offset
            )?.let {
                emit(
                    it.result.map {
                        mapper.convertTo(it)
                    }
                )
            }
        }
    }
}