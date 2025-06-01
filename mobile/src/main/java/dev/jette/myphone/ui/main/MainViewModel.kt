// Create a new Kotlin file, e.g., MyScreenViewModel.kt
package dev.jette.myphone.ui.main // Use your app's package name

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import dev.jette.myphone.data.BatteryInfo
import dev.jette.myphone.data.BatteryLevelReceiver
import dev.jette.myphone.demo.UserPresenter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.android.ext.android.inject

@KoinViewModel
class MyScreenViewModel(
    application: Application,
//    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val _batteryInfo = MutableStateFlow<BatteryInfo?>(null) // Initialize with null or a default
    val batteryInfo: StateFlow<BatteryInfo?> = _batteryInfo

    init {
        BatteryLevelReceiver.registerForBatteryUpdates(getApplication()).apply {
            viewModelScope.launch {
                this@apply?.collect { info ->
                    info?.let {
                        _batteryInfo.value = info
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        BatteryLevelReceiver.unregisterBatteryReceiver(getApplication())
    }

    // Define ViewModel factory in a companion object
    companion object {
//
//        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
//            @Suppress("UNCHECKED_CAST")
//            override fun <T : ViewModel> create(
//                modelClass: Class<T>,
//                extras: CreationExtras
//            ): T {
//                // Get the Application object from extras
//                val application = checkNotNull(extras[APPLICATION_KEY])
//                // Create a SavedStateHandle for this ViewModel from extras
////                val savedStateHandle = extras.createSavedStateHandle()
//
//                return MyScreenViewModel(
//                    application,
////                    savedStateHandle
//                ) as T
//            }
//        }
    }
}
