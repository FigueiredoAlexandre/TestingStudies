package com.example.pokedex.presentation.viewmodel.pokemon_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.domain.domain_object.PokemonDomain
import com.example.pokedex.domain.query_object.PokeQuery
import com.example.pokedex.domain.use_case.FlowUseCase
import com.example.pokedex.presentation.view.action.pokemon_list.PokemonsListUiAction
import com.example.pokedex.presentation.view.events.PokemonsListEvents
import com.example.pokedex.presentation.view.state.pokemon_list.PokemonsListUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class PokemonsListViewModel(
    private val useCase: FlowUseCase<PokeQuery, List<PokemonDomain.Pokemon>>
): ViewModel() {

    private var _state = MutableLiveData<PokemonsListUiState>(PokemonsListUiState())
    var state:LiveData<PokemonsListUiState> = _state

    private var _action = MutableLiveData<PokemonsListUiAction>()
    var action:LiveData<PokemonsListUiAction> = _action

    private var currentOffset: Int = 0



    fun toggleEvent(events: PokemonsListEvents){
        when(events){
            is PokemonsListEvents.QueryPokemons -> {
                queryPokemons(events.enteredName)
            }
            is PokemonsListEvents.RequestForMorePokemons ->{
                requestPokemons(false)
            }
            is PokemonsListEvents.InitialRequestForPokemons -> {
                requestPokemons(true)
            }
        }
    }

    private fun queryPokemons(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            setQueryingState(query.isNotEmpty())
            useCase.execute(
                PokeQuery(
                    if (query.isNotEmpty()) 10000 else 0,
                    if (query.isNotEmpty()) 0 else currentOffset,
                    query
                )
            ).onStart {
                _action.postValue(
                    PokemonsListUiAction.ResetAdapter
                )
            }.catch {
                _state.postValue(
                    _state.value?.copy(
                        error = it.message.orEmpty()
                    )
                )
            }.collect{ incomingPokemons ->
                _state.postValue(
                    _state.value?.copy(
                        pokemons = incomingPokemons
                    )
                )
            }
        }
    }

    private fun requestPokemons(isInitial: Boolean){
        viewModelScope.launch(Dispatchers.IO){
            useCase.execute(
                PokeQuery(
                    limit = 0,
                    offset = currentOffset
                )
            ).onStart {
                if(isInitial){
                    _action.postValue(
                        PokemonsListUiAction.ShowRecyclerViewProgress
                    )
                } else {
                    _action.postValue(
                        PokemonsListUiAction.ShowProgress
                    )
                }

            }.catch {
                _state.postValue(
                    _state.value?.copy(
                        error = it.message.orEmpty()
                    )
                )
            }.collect{ pokemons ->
                _state.postValue(
                    _state.value?.copy(
                        pokemons = pokemons
                    )
                )
            }
            currentOffset += 10
        }
    }

    private fun setQueryingState(querying:Boolean){
        _state.postValue(
            _state.value?.copy(
                isQuerying = querying
            )
        )
    }
}