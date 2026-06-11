package com.yoru.app
import android.content.Context
import com.google.gson.Gson
import model.CatalogData

object JsonLoader {

    fun loadCatalog(context: Context): CatalogData {
        val json = context.assets
            .open("products.json")
            .bufferedReader()
            .use { it.readText() }

        return Gson().fromJson(
            json,
            CatalogData::class.java
        )
    }
}