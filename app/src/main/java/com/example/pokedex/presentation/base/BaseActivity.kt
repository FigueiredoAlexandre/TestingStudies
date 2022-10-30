package com.example.pokedex.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.pokedex.common.extensions.getViewBinding

class BaseActivity<T: ViewBinding>: AppCompatActivity() {

    private lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instantiateViewBinding(layoutInflater)
        setContentView(binding.root)

    }

    private fun instantiateViewBinding(layoutInflater: LayoutInflater){
        binding = getViewBinding(layoutInflater)
    }
}