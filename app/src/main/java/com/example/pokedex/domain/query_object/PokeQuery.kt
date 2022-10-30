package com.example.pokedex.domain.query_object

data class PokeQuery(
    val limit: Int,
    val offset:Int,
    val query: String = ""
)