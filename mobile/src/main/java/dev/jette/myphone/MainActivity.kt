package dev.jette.myphone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.jette.myphone.ui.main.MainScreen
import dev.jette.myphone.ui.main.MyScreenViewModel
import dev.jette.myphone.ui.theme.MyPhoneTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPhoneTheme {
                MainScreen(viewModel = viewModel<MyScreenViewModel>())
            }
        }
    }
}
