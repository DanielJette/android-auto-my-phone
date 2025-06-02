package dev.jette.myphone.ui.battery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.jette.myphone.data.BatteryInfo
import dev.jette.myphone.data.BatteryLevelReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class BatterViewModel(application: Application) : AndroidViewModel(application) {

    private val _batteryInfo = MutableStateFlow<BatteryInfo?>(null)
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
