package com.example.pokedex.domain.use_case

import com.example.pokedex.domain.domain_object.PokemonDomain
import com.example.pokedex.domain.query_object.PokeQuery
import com.example.pokedex.domain.repository.ListOfPokemonsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class GetPokemonsUseCase(
    private val repository: ListOfPokemonsRepository
): FlowUseCase<PokeQuery, List<PokemonDomain.Pokemon>> {


    override suspend fun execute(value: PokeQuery): Flow<List<PokemonDomain.Pokemon>> {
        return flow {
            try {
                if (value.query.isEmpty()) {
                    repository.getPokemons(
                        value
                    ).collect { pokemons ->
                        emit(pokemons)
                    }
                } else {
                    repository.getPokemons(
                        value
                    ).collect {
                        val queriedList = it.filter {
                            it.name.contains(value.query)
                        }
                        emit(queriedList)
                    }
                }
            } catch (e: Exception){
                throw e
            }
        }
    }
}