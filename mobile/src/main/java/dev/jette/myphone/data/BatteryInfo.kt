package dev.jette.myphone.data

import android.os.BatteryManager
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

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
        get() = if (level != -1 && scale != -1) (level * 100 / scale.toFloat()).toInt() else 0

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
                val expiryTime = LocalDateTime.now().plusMinutes(batteryDischargePrediction.toMinutes())
                val formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                return "Until ${expiryTime.format(formatter)}"
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