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
import androidx.car.app.model.Toggle
import androidx.core.graphics.drawable.IconCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dev.jette.myphone.data.BatteryInfo
import dev.jette.myphone.di.viewModel
import dev.jette.myphone.ui.main.MyScreenViewModel
import kotlinx.coroutines.launch


class HelloWorldScreen(carContext: CarContext) : Screen(carContext) {

//    private val viewModelStoreOwner = getViewModelStoreOwner()
//    private val myViewModel = ViewModelProvider(viewModelStoreOwner, MyScreenViewModel.Factory)[MyScreenViewModel::class]

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

//        viewModel = viewModel<MyScreenViewModel>()

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
            .build()

        return PaneTemplate.Builder(
            Pane
                .Builder()
                .addRow(Row.Builder().setTitle("Hello Daniel").build())
                .addRow(Row.Builder().setTitle("setNumericDecoration").setNumericDecoration(3).build())
                .addRow(Row.Builder().setTitle("addText").addText("This is text").addText("This is more text").build())
                .addRow(Row.Builder().setTitle("addAction").addAction(action).build())
                .addRow(Row.Builder().setTitle("setToggle").setToggle(toggle).build())
                .build()
        )
            .setTitle("This is the title of the pane")
            .setHeaderAction(Action.APP_ICON)
            .setActionStrip(ActionStrip.Builder().addAction(action).build())
            .build()
    }
}