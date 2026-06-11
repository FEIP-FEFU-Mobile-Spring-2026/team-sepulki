package com.yoru.app

import android.content.Context
import androidx.lifecycle.ViewModel
import model.CatalogData
import model.Category
import model.Product

class CatalogViewModel : ViewModel() {

    private var catalogData: CatalogData? = null

    fun loadData(context: Context) {
        catalogData = JsonLoader.loadCatalog(context)
    }

    fun getCategories(): List<Category> {
        return catalogData?.categories ?: emptyList()
    }

    fun getNewProducts(): List<Product> {
        return catalogData?.items?.filter {
            "New" in it.tags
        } ?: emptyList()
    }

    fun getProductsForCategory(categoryName: String): List<Product> {

        if (categoryName == "Новинки") {
            return getNewProducts()
        }

        val category = catalogData?.categories?.find {
            it.name == categoryName
        }

        return catalogData?.items?.filter {
            it.categoryId == category?.id
        } ?: emptyList()
    }

    fun getCategoriesWithNew(): List<String> {
        val categories = mutableListOf("Новинки")

        categories.addAll(
            catalogData?.categories?.map { it.name }
                ?: emptyList()
        )

        return categories
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