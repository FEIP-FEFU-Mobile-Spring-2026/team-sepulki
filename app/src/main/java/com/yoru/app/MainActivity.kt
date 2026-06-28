package com.yoru.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.yoru.app.ui.theme.YoruTheme

class MainActivity : ComponentActivity() {

    private val viewModel: CatalogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadData(this)

        setContent {

            YoruTheme {

                var selectedScreen by remember {
                    mutableStateOf("catalog")
                }

                var selectedCategory by remember {
                    mutableStateOf("Новинки")
                }

                val categories = viewModel.getCategoriesWithNew()

                androidx.compose.material3.Scaffold(

                    bottomBar = {
                        BottomNavigationBar(
                            selectedScreen = selectedScreen,
                            onScreenSelected = {
                                selectedScreen = it
                            }
                        )
                    }

                ) { paddingValues ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {

                        when (selectedScreen) {

                            "catalog" -> {

                                TabRow(
                                    selectedTabIndex = categories.indexOf(selectedCategory)
                                ) {

                                    categories.forEach { category ->

                                        Tab(
                                            selected = selectedCategory == category,
                                            onClick = {
                                                selectedCategory = category
                                            },
                                            text = {
                                                Text(category)
                                            }
                                        )
                                    }
                                }

                                LazyColumn {

                                    items(
                                        viewModel.getProductsForCategory(selectedCategory)
                                    ) { product ->

                                        ProductCard(product)
                                    }
                                }
                            }

                            "cart" -> {

                                Column {

                                    Text("Корзина")

                                    Text("Товары пока не добавлены")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
