package dev.jette.myphone

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.ActionStrip
import androidx.car.app.model.CarColor
import androidx.car.app.model.CarIcon
import androidx.car.app.model.Pane
import androidx.car.app.model.PaneTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import androidx.car.app.model.Toggle
import androidx.core.graphics.drawable.IconCompat

class HelloWorldScreen(carContext: CarContext) : Screen(carContext) {
    override fun onGetTemplate(): Template {
        val icon = CarIcon.Builder(IconCompat.createWithResource(carContext, R.drawable.ic_battery_saver)).build()

        val action = Action.Builder()
            .setTitle("Battery Saver")
            .setIcon(icon)
            .setEnabled(true)
            .setOnClickListener {

            }
            .build()


        val toggle = Toggle.Builder(
            object : Toggle.OnCheckedChangeListener {
                override fun onCheckedChange(isChecked: Boolean) {

                }
            }
        )
//            (value) -> {
//            writeSharedPref(prefKeyResource, value);
//        })
//        .setChecked(readSharedPref(prefKeyResource, false))
            .build()

        return PaneTemplate.Builder(
            Pane
                .Builder()
                .addRow(Row.Builder().setTitle("Hello Daniel").build())
                .addRow(Row.Builder().setTitle("setNumericDecoration").setNumericDecoration(3).build())
                .addRow(Row.Builder().setTitle("addText").addText("This is text").addText("This is more text").build())
                .addRow(Row.Builder().setTitle("addAction").addAction(action).build())
                // These can not be used in a row:
//                .addRow(Row.Builder().setTitle("APP_ICON").addAction(Action.APP_ICON).build())
//                .addRow(Row.Builder().setTitle("PAN").addAction(Action.PAN).build())
//                .addRow(Row.Builder().setTitle("BACK").addAction(Action.BACK).build())
//                .addRow(Row.Builder().setTitle("COMPOSE_MESSAGE").addAction(Action.COMPOSE_MESSAGE).build())
                .addRow(Row.Builder().setTitle("setToggle").setToggle(toggle).build())
                .build()
        )
            .setTitle("This is the title of the pane")
            .setHeaderAction(Action.APP_ICON)
            .setActionStrip(ActionStrip.Builder().addAction(action).build())
            .build()
    }
}