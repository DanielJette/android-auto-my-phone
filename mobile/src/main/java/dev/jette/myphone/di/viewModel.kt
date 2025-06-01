package dev.jette.myphone.di

import androidx.car.app.Screen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import dev.jette.myphone.getViewModelStoreOwner
import org.koin.android.compat.ViewModelCompat
import org.koin.core.parameter.ParametersDefinition

inline fun <reified T : ViewModel> Screen.viewModel(
    viewModelStoreOwner: ViewModelStoreOwner = getViewModelStoreOwner(),
    noinline parameters: ParametersDefinition? = null,
): Lazy<T> =
    lazy {
        ViewModelCompat.getViewModel(
            owner = viewModelStoreOwner,
            clazz = T::class.java,
            qualifier = null,
            parameters = parameters
        )
    }
