package com.example.pokedex.presentation.view.action.pokemon_list

sealed class PokemonsListUiAction {
    object ShowProgress: PokemonsListUiAction()
    object ShowRecyclerViewProgress: PokemonsListUiAction()
    object ResetAdapter: PokemonsListUiAction()
}