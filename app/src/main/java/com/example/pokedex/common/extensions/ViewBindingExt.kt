package com.example.pokedex.common.extensions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.pokedex.presentation.base.BaseActivity
import com.example.pokedex.presentation.base.BaseFragment
import java.lang.Exception
import java.lang.RuntimeException
import java.lang.reflect.ParameterizedType


internal fun <T: ViewBinding> BaseFragment<T>.getViewBinding(
    layoutInflater: LayoutInflater,
    container: ViewGroup?
): T {
    return findClass().getBinding(layoutInflater,container)
}

internal fun <T:ViewBinding> BaseActivity<T>.getViewBinding(
    layoutInflater: LayoutInflater
): T {
    return findClass().getBinding(layoutInflater)
}


internal fun Any.findClass(): Class<*> {
    var javaClass = this.javaClass
    var result: Class<*>? = null

    while (result == null || !result.checkMethod()){
        result = (javaClass.genericSuperclass as? ParameterizedType)
            ?.actualTypeArguments?.firstOrNull {
                if(it is Class<*>){
                    it.checkMethod()
                } else {
                    false
                }
            } as? Class<*>
        javaClass = javaClass.superclass
    }
    return result
}


internal fun Class<*>.checkMethod(): Boolean{
    return try{
        getMethod(
            "inflate",
            LayoutInflater::class.java
        )
        true
    } catch (e: Exception){
        false
    }
}

internal fun<V:ViewBinding> Class<*>.getBinding(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?
): V{
    return try {
        getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        ).invoke(null, layoutInflater, viewGroup, false) as V
    }catch (e: Exception){
        throw  RuntimeException(" The view binding has been changed")
    }
}

internal fun <V:ViewBinding> Class<*>.getBinding(
    layoutInflater: LayoutInflater
): V{
    return try {
        getMethod(
            "inflate",
            LayoutInflater::class.java
        ).invoke(null, layoutInflater, false) as V
    }catch (e:Exception){
        throw RuntimeException(" The view binding has been changed")
    }
}