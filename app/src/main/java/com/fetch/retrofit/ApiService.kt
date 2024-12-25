package com.fetch.retrofit

import com.fetch.models.Item
import retrofit2.http.GET

interface ApiService {
    @GET("hiring.json")
    suspend fun fetchItems(): List<Item>
}