package com.example.pokedex.data.remote.response

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    @SerializedName("results")
    val result: List<PokemonItemResponse>
)