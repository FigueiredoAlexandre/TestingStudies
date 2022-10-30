package com.example.pokedex.data.remote.response

import com.google.gson.annotations.SerializedName

data class PokemonItemResponse(
    @SerializedName("name")
    val name:String?,
    @SerializedName("url")
    val url:String?
)