package com.example.myproject.Api


import com.example.myproject.Model.JishoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface JishoApiService {
    @GET("api/v1/search/words")
    suspend fun searchWords(@Query("keyword") keyword: String): JishoResponse
}