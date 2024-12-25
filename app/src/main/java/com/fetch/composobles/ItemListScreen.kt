package com.fetch.composobles

import com.fetch.viewModel.ItemViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ItemListScreen(viewModel: ItemViewModel) {
    val items by viewModel.items.observeAsState(emptyMap())
    val loading by viewModel.loading.observeAsState(false)

    LaunchedEffect(Unit) {
        viewModel.fetchItems()
    }

    if (loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items.forEach { (listId, items) ->
                item {
                    Text(
                        text = "List ID: $listId",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(items) { item ->
                    Text(text = item.name ?: "Unnamed Item")
                }
            }
        }
    }
}