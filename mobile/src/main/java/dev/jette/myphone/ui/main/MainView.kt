package dev.jette.myphone.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainScreen() {

    val viewModel = viewModel<MyScreenViewModel>()

    // Collect the StateFlow as State, which will trigger recomposition when the value changes
    val batteryPercentage by viewModel.batteryInfo.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp), // Added some padding for content
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Hello",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineMedium
            )
            Button(
                onClick = {
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Update Greeting")
            }

            // Display Battery Percentage
            batteryPercentage?.let { info -> // Check for null if initially null
                Text(
                    text = "Battery: $info",
                    modifier = Modifier.padding(top = 16.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
            } ?: Text(
                text = "Battery: Loading...", // Show a loading state
                modifier = Modifier.padding(top = 16.dp),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
