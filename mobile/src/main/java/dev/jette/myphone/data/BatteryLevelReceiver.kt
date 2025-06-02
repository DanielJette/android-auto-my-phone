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
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.toKotlinDuration

data class BatteryInfo(
    val level: Int = -1,
    val scale: Int = -1,
    val status: Int = -1,
    val health: Int = -1,
    val plugged: Int = -1,
    val batteryLow: Int = -1,
    val chargingStatus: Int? = -1,
    val cycleCount: Int? = -1,
    val present: Int = -1,
    val technology: Int = -1,
    val temperature: Int = -1,
    val voltage: Long = -1,
    val current: Long = -1,
    val energyCounter: Long = -1,
    val isPowerSaveMode: Boolean? = null,
    val batteryDischargePrediction: Duration? = null,
) {

    val percentage: Int
        get() = if (level != -1 && scale != -1) {
            (level * 100 / scale.toFloat()).toInt()
        } else {
            0
        }

    val batteryStatus: String
        get() = when (status) {
            BatteryManager.BATTERY_STATUS_CHARGING -> "Charging"
            BatteryManager.BATTERY_STATUS_DISCHARGING -> "Discharging"
            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "Not charging"
            BatteryManager.BATTERY_STATUS_FULL -> "Fully charged"
            else -> "Unknown"
        }

    val batteryHealth: String
        get() = when (health) {
            BatteryManager.BATTERY_HEALTH_GOOD -> "✅ Healthy"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "\uD83C\uDF21\uFE0F Overheating"
            BatteryManager.BATTERY_HEALTH_DEAD -> "☠\uFE0F Dead"
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "⚡ Over voltage"
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "⛔ Failure"
            BatteryManager.BATTERY_HEALTH_COLD -> "❄\uFE0F Cold"
            else -> ""
        }

    val changeSource: String
        get() = when (plugged) {
            BatteryManager.BATTERY_PLUGGED_AC -> "Plugged into AC Adapter"
            BatteryManager.BATTERY_PLUGGED_USB -> "Charging via USB cable"
            BatteryManager.BATTERY_PLUGGED_WIRELESS -> "Charging wirelessly"
            BatteryManager.BATTERY_PLUGGED_DOCK -> "Docked"
            else -> batteryStatus
        }

    val isCharging: Boolean
        get() = when (status) {
            BatteryManager.BATTERY_STATUS_CHARGING -> true
            else -> false
        }

    val chargeQuality: String
        get() {
            val mA = voltage.toDouble() * 0.001

            return if (mA < 1000.0) {
                "Charging slowly"
            } else {
                "Charging rapidly"
            }
        }

    val power: String
        get() = ((current.toDouble() * voltage.toDouble()) / 1000000.0).toString().takeIf { (voltage > -1) && (current > -1) } ?: ""

    val isLow: Boolean
        get() = (percentage <= 20)

    val batteryDuration: String
        get() {
            if (batteryDischargePrediction != null) {
                val a = batteryDischargePrediction.toMinutes()
                val expiryTime = LocalDateTime.now().plusMinutes(a)
                val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("'Until' h:mm a", Locale.getDefault())
                return expiryTime.format(formatter)
            }
            return ""
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
                "voltage = $voltage mV\n" +
                "current = $current mA\n" +
                "power = $power W\n" +
                "energyCounter = $energyCounter\n" +
                "isPowerSaveMode = $isPowerSaveMode\n" +
                "batteryDischargePrediction = $batteryDischargePrediction"


}

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
