package com.example.pokedex.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.pokedex.common.extensions.getViewBinding

class BaseFragment<T: ViewBinding>: Fragment() {

    private lateinit var _binding: T

    private val binding
        get() = _binding


    private var baseActivity: BaseActivity<*>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        instantiateViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { activity ->
            if(activity is BaseActivity<*>){
                baseActivity = activity
            }
        }
    }

    private fun instantiateViewBinding(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?
    ) {
        _binding = getViewBinding(layoutInflater, viewGroup)
    }
}