package com.fetch.repo

import com.fetch.models.Item
import com.fetch.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val apiService: ApiService) {
    suspend fun getItems(): List<Item> = withContext(Dispatchers.IO) {
        apiService.fetchItems()
    }
}