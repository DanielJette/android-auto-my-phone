package dev.jette.myphone.data

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class BatteryInfo(
    val level: Int,
    val scale: Int,
    val status: Int,
    val health: Int,
    val plugged: Int,
    val batteryLow: Int,
    val chargingStatus: Int?,
    val cycleCount: Int?,
    val present: Int,
    val technology: Int,
    val temperature: Int,
    val voltage: Int,
) {

    val percentage: Int
        get() = if (level != -1 && scale != -1) {
            val batteryPct: Int = (level * 100 / scale.toFloat()).toInt()
            Log.d("BatteryLevelReceiver", "Current Battery Percentage: $batteryPct%")
            batteryPct
        } else {
            0
        }

    override fun toString(): String =
        "\n" +
                "level = $level\n" +
                "scale = $scale\n" +
                "status = $status\n" +
                "health = $health\n" +
                "plugged = $plugged\n" +
                "batteryLow = $batteryLow\n" +
                "chargingStatus = $chargingStatus\n" +
                "cycleCount = $cycleCount\n" +
                "present = $present\n" +
                "technology = $technology\n" +
                "temperature = $temperature\n" +
                "voltage = $voltage"


}

class BatteryLevelReceiver(private val context: Context) : BroadcastReceiver() {

    private val batteryInfo = MutableStateFlow<BatteryInfo?>(null)

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BATTERY_CHANGED) {
            val info = BatteryInfo(
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
                voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1),
            )
            batteryInfo.value = info
        }
    }

    fun getInitialBatteryLevel(): Int {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus: Intent? = context.applicationContext.registerReceiver(null, intentFilter)
        val level: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1

        return if (level != -1 && scale != -1) {
            (level * 100 / scale.toFloat()).toInt()
        } else {
            0
        }
    }

    companion object {

        private var batteryLevelReceiver: BatteryLevelReceiver? = null

        fun registerForBatteryUpdates(context: Context): StateFlow<BatteryInfo?>? {
            if (batteryLevelReceiver == null) {
                batteryLevelReceiver = BatteryLevelReceiver(context)

                val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                context.applicationContext.registerReceiver(batteryLevelReceiver, intentFilter)

//                batteryLevelReceiver?.getInitialBatteryLevel()
            }
            return batteryLevelReceiver?.batteryInfo
        }

        fun unregisterBatteryReceiver(application: Application) {
            application.applicationContext.unregisterReceiver(batteryLevelReceiver)
            batteryLevelReceiver = null
        }
    }
}
