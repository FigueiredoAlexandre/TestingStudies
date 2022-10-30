package com.example.pokedex.data.remote.api

import com.example.pokedex.data.remote.response.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApi {

    @GET("pokemon")
    suspend fun getThePokemons(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonResponse?
}