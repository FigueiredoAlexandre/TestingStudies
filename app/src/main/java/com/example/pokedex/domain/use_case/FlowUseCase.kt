package com.example.pokedex.domain.use_case

import kotlinx.coroutines.flow.Flow

interface FlowUseCase<T:Any,S: Any> {

    suspend fun execute(value:T): Flow<S>
}