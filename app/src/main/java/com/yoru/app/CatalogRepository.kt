package com.yoru.app

class CatalogRepository {

    suspend fun loadCatalog() =
        RetrofitClient.api.getCatalog(
            "Bearer Cmt7wdwFgDIi1_SRX8hlJIExs0jJKPr4axflLpExAxM"
        )
}