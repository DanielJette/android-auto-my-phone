package dev.jette.myphone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.jette.myphone.ui.battery.MainScreen
import dev.jette.myphone.ui.theme.MyPhoneTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPhoneTheme {
                MainScreen()
            }
        }
    }
}
