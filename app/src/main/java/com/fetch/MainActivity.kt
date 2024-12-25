package com.fetch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fetch.composobles.ItemListScreen
import com.fetch.repo.Repository
import com.fetch.retrofit.RetrofitInstance
import com.fetch.ui.theme.FetchTheme
import com.fetch.viewModel.ItemViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiService = RetrofitInstance.apiService
        val repository = Repository(apiService)
        val viewModel = ItemViewModel(repository)

        setContent {
            FetchTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        // Header
                        TopAppBar(
                            title = {
                                Text(text = "Fetch Items", style = MaterialTheme.typography.titleLarge)
                            },
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )

                        // Main Content
                        ItemListScreen(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FetchTheme {
        Greeting("Android")
    }
}