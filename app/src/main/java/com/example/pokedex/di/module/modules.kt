package com.example.pokedex.di.module

import com.example.pokedex.data.remote.api.PokemonApi
import com.example.pokedex.data.repository.ListOfPokemonsRepositoryImp
import com.example.pokedex.domain.mapper.PokemonResponseToPokemon
import com.example.pokedex.domain.use_case.GetPokemonsUseCase
import com.example.pokedex.presentation.viewmodel.pokemon_list.PokemonsListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val pokemonListModule = module {


    factory {
        PokemonResponseToPokemon()
    }
    factory{
        Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApi::class.java)
    }
    factory {
        ListOfPokemonsRepositoryImp(
            api = get(),
            mapper = get()
        )
    }
    factory { GetPokemonsUseCase(repository = get()) }
    viewModel { PokemonsListViewModel(useCase = get()) }

}