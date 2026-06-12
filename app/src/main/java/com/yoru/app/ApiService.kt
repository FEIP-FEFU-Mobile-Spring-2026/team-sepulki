package com.yoru.app

import model.CatalogData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {

    @GET("catalog")
    suspend fun getCatalog(
        @Header("Authorization")
        token: String
    ): Response<CatalogData>
}