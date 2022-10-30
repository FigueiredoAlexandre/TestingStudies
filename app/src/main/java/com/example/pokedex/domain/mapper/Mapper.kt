package com.example.pokedex.domain.mapper

interface Mapper<T:Any, S:Any> {

    fun convertTo(input:T): S
}