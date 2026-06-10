package com.yoru.app

import androidx.lifecycle.ViewModel
import model.CatalogData
import model.Category
import model.Product

class CatalogViewModel : ViewModel() {

    private var catalogData: CatalogData? = null

    fun setCatalogData(data: CatalogData) {
        catalogData = data
    }

    fun getCategories(): List<Category> {
        return catalogData?.categories ?: emptyList()
    }

    fun getProducts(): List<Product> {
        return catalogData?.items ?: emptyList()
    }

    fun getProductsByCategory(categoryId: String): List<Product> {
        return catalogData?.items?.filter {
            it.categoryId == categoryId
        } ?: emptyList()
    }
}