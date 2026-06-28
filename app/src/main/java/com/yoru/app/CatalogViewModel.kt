package com.yoru.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import model.CatalogData
import model.Category
import model.Product

class CatalogViewModel : ViewModel() {

    private val repository = CatalogRepository()

    private var catalogData by mutableStateOf<CatalogData?>(null)

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadData() {

        viewModelScope.launch {

            isLoading = true
            errorMessage = null

            try {

                val response = repository.loadCatalog()

                if (response.isSuccessful) {

                    catalogData = response.body()

                } else {

                    errorMessage = "Ошибка сервера"
                }

            } catch (e: Exception) {

                errorMessage = "Ошибка сети"
            }

            isLoading = false
        }
    }

    fun getCategories(): List<Category> {
        return catalogData?.categories ?: emptyList()
    }

    fun getNewProducts(): List<Product> {

        return catalogData?.items?.filter {
            "New" in it.tags
        } ?: emptyList()
    }

    fun getProductsForCategory(
        categoryName: String
    ): List<Product> {

        if (categoryName == "Новинки") {
            return getNewProducts()
        }

        val category =
            catalogData?.categories?.find {
                it.name == categoryName
            }

        return catalogData?.items?.filter {
            it.categoryId == category?.id
        } ?: emptyList()
    }

    fun getCategoriesWithNew(): List<String> {

        val categories =
            mutableListOf("Новинки")

        categories.addAll(
            catalogData?.categories?.map {
                it.name
            } ?: emptyList()
        )

        return categories
    }

    fun retryLoading() {
        loadData()
    }
    fun getProductById(
        productId: String
    ): Product? {

        return catalogData?.items?.find {
            it.id == productId
        }
    }
}