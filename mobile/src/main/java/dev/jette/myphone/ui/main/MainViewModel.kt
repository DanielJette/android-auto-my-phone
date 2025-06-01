// Create a new Kotlin file, e.g., MyScreenViewModel.kt
package dev.jette.myphone.ui.main // Use your app's package name

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.jette.myphone.data.BatteryInfo
import dev.jette.myphone.data.BatteryLevelReceiver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

class MyScreenViewModel(application: Application) : AndroidViewModel(application) {

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

}
