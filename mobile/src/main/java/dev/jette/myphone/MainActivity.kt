package dev.jette.myphone

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.jette.myphone.demo.UserPresenter
import dev.jette.myphone.ui.main.MainScreen
import dev.jette.myphone.ui.main.MyScreenViewModel
import dev.jette.myphone.ui.theme.MyPhoneTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val presenter: UserPresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPhoneTheme {
                Log.d("JETTE", presenter.sayHello("Daniel"))
                MainScreen(
//                    viewModel = viewModel<MyScreenViewModel>()
                )
            }
        }
    }
}
