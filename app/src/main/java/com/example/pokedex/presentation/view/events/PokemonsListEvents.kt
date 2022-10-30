package com.example.pokedex.presentation.view.events

sealed class PokemonsListEvents{
    data class QueryPokemons(val enteredName:String):PokemonsListEvents()
    object RequestForMorePokemons: PokemonsListEvents()
    data class ClickOverPokemon(val pokemonName:String): PokemonsListEvents()
    object InitialRequestForPokemons: PokemonsListEvents()
}
