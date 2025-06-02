package dev.jette.myphone.data

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.PowerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BatteryLevelReceiver : BroadcastReceiver() {

    private val batteryInfo = MutableStateFlow(BatteryInfo())

    override fun onReceive(context: Context?, intent: Intent?) {

        val currentInfo = batteryInfo.value

        batteryInfo.value = when (intent?.action) {
            PowerManager.ACTION_POWER_SAVE_MODE_CHANGED -> {
                val powerManager = context?.getSystemService(PowerManager::class.java)
                currentInfo.copy(
                    isPowerSaveMode = powerManager?.isPowerSaveMode,
                    batteryDischargePrediction = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) powerManager?.batteryDischargePrediction else null
                )
            }

            Intent.ACTION_BATTERY_CHANGED -> {
                val batteryManager = context?.getSystemService(BatteryManager::class.java)
                val powerManager = context?.getSystemService(PowerManager::class.java)

                currentInfo.copy(
                    level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1),
                    scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1),
                    status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1),
                    health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1),
                    plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1),
                    batteryLow = intent.getIntExtra(BatteryManager.EXTRA_BATTERY_LOW, -1),
                    chargingStatus = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) intent.getIntExtra(BatteryManager.EXTRA_CHARGING_STATUS, -1) else null,
                    cycleCount = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) intent.getIntExtra(BatteryManager.EXTRA_CYCLE_COUNT, -1) else null,
                    present = intent.getIntExtra(BatteryManager.EXTRA_PRESENT, -1),
                    technology = intent.getIntExtra(BatteryManager.EXTRA_TECHNOLOGY, -1),
                    temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1),
                    voltage = intent.getLongExtra(BatteryManager.EXTRA_VOLTAGE, -1),
                    current = batteryManager?.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW) ?: -1L,
                    energyCounter = batteryManager?.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER) ?: -1L,
                    isPowerSaveMode = powerManager?.isPowerSaveMode,
                    batteryDischargePrediction = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) powerManager?.batteryDischargePrediction else null
                )
            }

            else -> currentInfo
        }
    }

    companion object {

        private var batteryLevelReceiver: BatteryLevelReceiver? = null

        fun registerForBatteryUpdates(context: Context): StateFlow<BatteryInfo?>? {
            if (batteryLevelReceiver == null) {
                batteryLevelReceiver = BatteryLevelReceiver()

                val intentFilter = IntentFilter().apply {
                    addAction(Intent.ACTION_BATTERY_CHANGED)
                    addAction(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED)
                }

                context.applicationContext.registerReceiver(batteryLevelReceiver, intentFilter)
            }
            return batteryLevelReceiver?.batteryInfo
        }

        fun unregisterBatteryReceiver(application: Application) {
            application.applicationContext.unregisterReceiver(batteryLevelReceiver)
            batteryLevelReceiver = null
        }
    }
}
