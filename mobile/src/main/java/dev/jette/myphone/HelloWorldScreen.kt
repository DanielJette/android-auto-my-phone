package dev.jette.myphone

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.ActionStrip
import androidx.car.app.model.CarIcon
import androidx.car.app.model.Pane
import androidx.car.app.model.PaneTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import androidx.core.graphics.drawable.IconCompat
import androidx.lifecycle.lifecycleScope
import dev.jette.myphone.data.BatteryInfo
import dev.jette.myphone.di.viewModel
import dev.jette.myphone.ui.main.MyScreenViewModel
import kotlinx.coroutines.launch

class HelloWorldScreen(carContext: CarContext) : Screen(carContext) {

    private val myViewModel by viewModel<MyScreenViewModel>()
    private var batteryInfo: BatteryInfo? = null

    init {
        lifecycleScope.launch {
            myViewModel.batteryInfo.collect {
                batteryInfo = it
                invalidate()
            }
        }
    }


    override fun onGetTemplate(): Template {

        val row1 = Row.Builder()
            .setTitle("${if (batteryInfo?.isLow == true) "\uD83E\uDEAB" else ""}${batteryInfo?.percentage}% Battery")
            .addText("${batteryInfo?.batteryHealth}")

        val row2 = Row.Builder()
            .setTitle("${batteryInfo?.changeSource}")

        if (batteryInfo?.isCharging == true) {
            row2.addText("${batteryInfo?.chargeQuality}")
        } else {
            row2.addText("${batteryInfo?.batteryDuration}")
        }

        val pane = Pane
            .Builder()
            .addRow(row1.build())
            .addRow(row2.build())

        if (batteryInfo?.isPowerSaveMode == true) {
            pane.addRow(
                Row.Builder()
                    .setTitle("\uD83D\uDFE0 Battery Saver enabled")
                    .build()
            )
        }
        val template = PaneTemplate.Builder(pane.build())

//        if (batteryInfo?.isLow == true) {
//        val icon = CarIcon.Builder(IconCompat.createWithResource(carContext, R.drawable.ic_battery_saver)).build()
//        val action = Action.Builder()
//            .setTitle("Battery Saver")
//            .setIcon(icon)
//            .setEnabled(true)
//            .setOnClickListener {
//
//            }
//            .build()
//            template.setActionStrip(ActionStrip.Builder().addAction(action).build())
//        }

        return template.setTitle("My Phone").build()
    }
}