package com.example.pokedex

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.pokedex.domain.domain_object.PokemonDomain
import com.example.pokedex.domain.query_object.PokeQuery
import com.example.pokedex.domain.use_case.FlowUseCase
import com.example.pokedex.domain.use_case.GetPokemonsUseCase
import com.example.pokedex.presentation.view.action.pokemon_list.PokemonsListUiAction
import com.example.pokedex.presentation.view.events.PokemonsListEvents
import com.example.pokedex.presentation.view.state.pokemon_list.PokemonsListUiState
import com.example.pokedex.presentation.viewmodel.pokemon_list.PokemonsListViewModel
import io.mockk.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception


class GetPokemonsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    var dispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: PokemonsListViewModel
    private lateinit var useCase: FlowUseCase<PokeQuery, List<PokemonDomain.Pokemon>>

    val pokemons = listOf<PokemonDomain.Pokemon>(
        PokemonDomain.Pokemon(
            "1",
            ""
        ),
        PokemonDomain.Pokemon(
            "2",
            ""
        ),
        PokemonDomain.Pokemon(
            "3",
            ""
        ),
        PokemonDomain.Pokemon(
            "4",
            ""
        ),

    )

    val queriedResponse = listOf<PokemonDomain.Pokemon>(
        PokemonDomain.Pokemon(
            "1",
            ""
        )
    )

    @Before
    fun setUp(){
        useCase = mockk<GetPokemonsUseCase>()
        viewModel = PokemonsListViewModel(useCase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun viewModel_InitialRequestForPokemons_shouldReturnListOfPokemons() = runTest(dispatcher){

        var stateObserver = mockk<Observer<PokemonsListUiState>>()
        var actionObserver = mockk<Observer<PokemonsListUiAction>>()

        var stateSlot = slot<PokemonsListUiState>()
        var actionSlot = slot<PokemonsListUiAction>()

        var stateCapturedList = arrayListOf<PokemonsListUiState>()
        var actionCapturedLst = arrayListOf<PokemonsListUiAction>()



        coEvery { useCase.execute(PokeQuery(4, 0))} returns flow{
            emit(pokemons)
        }

        coEvery { stateObserver.onChanged(capture(stateSlot)) } answers{
            stateCapturedList.add(stateSlot.captured)
        }

        coEvery { actionObserver.onChanged(capture(actionSlot))} answers {
            actionCapturedLst.add(actionSlot.captured)
        }

        viewModel.state.observeForever(stateObserver)
        viewModel.action.observeForever(actionObserver)

        //WHEN
        viewModel.toggleEvent(PokemonsListEvents.InitialRequestForPokemons)

        //THEN
        stateCapturedList.forEachIndexed { index, states ->
            when(index){
                1 -> assertEquals(pokemons, states.pokemons)
            }
        }

        actionCapturedLst.forEachIndexed{ index, action ->
            when(index){
                0 -> assert(action is PokemonsListUiAction.ShowProgress)
            }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun viewModel_whenMorePokemonsArerequested_shouldProgressRecycler_and_shouldReturnPokemons() = runTest(dispatcher){

        var stateObserver = mockk<Observer<PokemonsListUiState>>()
        var actionObserver = mockk<Observer<PokemonsListUiAction>>()

        var stateSlot = slot<PokemonsListUiState>()
        var actionSlot = slot<PokemonsListUiAction>()

        var stateList = arrayListOf<PokemonsListUiState>()
        var actionList = arrayListOf<PokemonsListUiAction>()

        coEvery { useCase.execute(PokeQuery(4, 0)) } returns flow {
            emit(pokemons)
        }

        coEvery { stateObserver.onChanged(capture(stateSlot)) } answers {
            stateList.add(stateSlot.captured)
        }

        coEvery { actionObserver.onChanged(capture(actionSlot)) } answers {
            actionList.add(actionSlot.captured)
        }

        viewModel.state.observeForever(stateObserver)
        viewModel.action.observeForever(actionObserver)

        //WHEN
        viewModel.toggleEvent(PokemonsListEvents.RequestForMorePokemons)

        //THEN
        stateList.forEachIndexed{ index, state ->
            when(index){
                1 -> assertEquals(pokemons, state.pokemons)
            }
        }

        actionList.forEachIndexed { index, pokemonsListUiAction ->
            when(index){
                0 -> assert(pokemonsListUiAction is PokemonsListUiAction.ShowRecyclerViewProgress)
            }
        }

    }

    @Test
    fun viewModel_whenQueryingIsHappening_shouldReturnQueryingStateAndPokemons(){

        var stateObserver = mockk<Observer<PokemonsListUiState>>()
        var actionObserver = mockk<Observer<PokemonsListUiAction>>()

        var stateSlot = slot<PokemonsListUiState>()
        var actionSlot = slot<PokemonsListUiAction>()

        var stateList = arrayListOf<PokemonsListUiState>()
        var actionList = arrayListOf<PokemonsListUiAction>()

        coEvery { useCase.execute(PokeQuery(10000, 0,"1"))} returns flow {
            emit(queriedResponse)
        }

        coEvery { stateObserver.onChanged(capture(stateSlot)) } answers {
            stateList.add(stateSlot.captured)
        }

        coEvery { actionObserver.onChanged(capture(actionSlot)) } answers {
            actionList.add(actionSlot.captured)
        }

        viewModel.state.observeForever(stateObserver)
        viewModel.action.observeForever(actionObserver)

        //WHEN
        viewModel.toggleEvent(PokemonsListEvents.QueryPokemons("1"))

        //THEN
        stateList.forEachIndexed { index, state ->
            when(index){
                1 -> assert(state.isQuerying)
            }
        }

        actionList.forEachIndexed{index, action ->
            when(index){
                0 -> assert(action is PokemonsListUiAction.ResetAdapter)
            }
        }
    }

}