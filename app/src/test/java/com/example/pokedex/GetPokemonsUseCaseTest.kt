package com.example.pokedex

import com.example.pokedex.data.repository.ListOfPokemonsRepositoryImp
import com.example.pokedex.domain.domain_object.PokemonDomain
import com.example.pokedex.domain.query_object.PokeQuery
import com.example.pokedex.domain.repository.ListOfPokemonsRepository
import com.example.pokedex.domain.use_case.GetPokemonsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetPokemonsUseCaseTest {

    private lateinit var useCase: GetPokemonsUseCase
    private lateinit var repository: ListOfPokemonsRepository

    private val pokemonDomain = listOf<PokemonDomain.Pokemon>(
        PokemonDomain.Pokemon(
            name = "bulbasaur",
            url = "https://pokeapi.co/api/v2/pokemon/1/"
        ),
        PokemonDomain.Pokemon(
            name = "venusaur",
            url = "https://pokeapi.co/api/v2/pokemon/2/"
        ),
        PokemonDomain.Pokemon(
            name = "charmander",
            url = "https://pokeapi.co/api/v2/pokemon/3/"
        ),
        PokemonDomain.Pokemon(
            name = "charmaleon",
            url = "https://pokeapi.co/api/v2/pokemon/4/"
        )

    )

    private val pokemonQueriedAnswer = listOf<PokemonDomain.Pokemon>(
        PokemonDomain.Pokemon(
            name = "bulbasaur",
            url = "https://pokeapi.co/api/v2/pokemon/1/"
        )
    )

    @Before
    fun setUp(){
        repository = mockk<ListOfPokemonsRepository>()
        useCase = GetPokemonsUseCase(repository)
    }

    @Test
    fun whenPokemonsAreRequested_shouldReturnListAccordingToOffset(): Unit = runBlocking{

        //GIVEN
        coEvery { repository.getPokemons(PokeQuery(4,0)) } returns flow {
            emit(pokemonDomain)
        }

        //WHEN
        val flow = useCase.execute(PokeQuery(4,0))

        //THEN
        flow.collect{
            assertEquals(it, pokemonDomain)
        }

        coVerify {
            repository.getPokemons(PokeQuery(4, 0))
        }
    }

    @Test
    fun whenPokemonsAreQueried_shouldReturnQueriedResult(): Unit = runBlocking {

        //GIVEW
        coEvery { repository.getPokemons(PokeQuery(10000, 0, "bulbasaur")) } returns flow {
            emit(pokemonQueriedAnswer)
        }

        //WHEN
        val flow = useCase.execute(PokeQuery(10000, 0, "bulbasaur"))

        //THEN
        flow.collect{
            assertEquals(it, pokemonQueriedAnswer)
        }

        coVerify {
            repository.getPokemons(PokeQuery(10000, 0, "bulbasaur"))
        }
    }


}