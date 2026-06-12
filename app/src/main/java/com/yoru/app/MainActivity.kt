package com.yoru.app

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.TextButton
import model.Product
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
import androidx.compose.material3.Button

class MainActivity : ComponentActivity() {

    private val viewModel: CatalogViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartViewModel.loadCart(this)

        viewModel.loadData()

        setContent {

            YoruTheme {

                var selectedProduct by remember {
                    mutableStateOf<Product?>(null)
                }

                var showClearDialog by remember {
                    mutableStateOf(false)
                }

                var showOrderDialog by remember {
                    mutableStateOf(false)
                }

                var selectedScreen by remember {
                    mutableStateOf("catalog")
                }

                var selectedCategory by remember {
                    mutableStateOf("Новинки")
                }

                val categories = viewModel.getCategoriesWithNew()

                if (viewModel.isLoading) {

                    Column {
                        Text("Загрузка...")
                    }

                    return@YoruTheme
                }

                if (viewModel.errorMessage != null) {

                    Column {

                        Text(viewModel.errorMessage!!)

                        Button(
                            onClick = {
                                viewModel.retryLoading()
                            }
                        ) {
                            Text("Повторить")
                        }
                    }

                    return@YoruTheme
                }

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

                                        ProductCard(
                                            product = product,
                                            onClick = {
                                                selectedProduct = product
                                            }
                                        )
                                    }
                                }
                            }

                            "cart" -> {
                                Column {

                                    Text("Корзина")

                                    if (cartViewModel.cartItems.isEmpty()) {

                                        Text("Корзина пуста")

                                    } else {

                                        cartViewModel.cartItems.forEach { item ->

                                            val product =
                                                viewModel.getProductById(
                                                    item.productId
                                                )

                                            if (product != null) {

                                                Text(
                                                    text = product.name
                                                )

                                                Row {

                                                    TextButton(
                                                        onClick = {
                                                            cartViewModel.decreaseQuantity(
                                                                item.productId,
                                                                item.sizeId
                                                            )
                                                        }
                                                    ) {
                                                        Text("-")
                                                    }

                                                    Text(
                                                        text = item.quantity.toString()
                                                    )

                                                    TextButton(
                                                        onClick = {
                                                            cartViewModel.increaseQuantity(
                                                                item.productId,
                                                                item.sizeId
                                                            )
                                                        }
                                                    ) {
                                                        Text("+")
                                                    }
                                                }

                                                TextButton(
                                                    onClick = {
                                                        cartViewModel.removeItem(
                                                            item.productId,
                                                            item.sizeId
                                                        )
                                                    }
                                                ) {
                                                    Text("Удалить")
                                                }

                                                Text(
                                                    text =
                                                        "Цена: ${
                                                            product.priceInKopecks *
                                                                    item.quantity / 100
                                                        } ₽"
                                                )

                                                Text("-------------------")
                                            }
                                        }

                                        Text(
                                            text =
                                                "Итого: ${
                                                    cartViewModel.getTotalPrice(viewModel) / 100
                                                } ₽"
                                        )

                                        Button(
                                            onClick = {
                                                showClearDialog = true
                                            }
                                        ) {
                                            Text("Очистить корзину")
                                        }
                                        Button(
                                            onClick = {
                                                showOrderDialog = true
                                            }
                                        ) {
                                            Text("Оформить заказ")
                                        }
                                    }
                                }
                            }
                        }

                        if (showClearDialog) {

                            androidx.compose.material3.AlertDialog(

                                onDismissRequest = {
                                    showClearDialog = false
                                },

                                title = {
                                    Text("Очистить корзину?")
                                },

                                text = {
                                    Text("Все товары будут удалены")
                                },

                                confirmButton = {

                                    TextButton(
                                        onClick = {

                                            cartViewModel.clearCart()

                                            showClearDialog = false
                                        }
                                    ) {
                                        Text("Да")
                                    }
                                },

                                dismissButton = {

                                    TextButton(
                                        onClick = {
                                            showClearDialog = false
                                        }
                                    ) {
                                        Text("Нет")
                                    }
                                }

                            )
                        }

                            if (selectedProduct != null) {

                            ProductBottomSheet(
                                product = selectedProduct!!,

                                onDismiss = {
                                    selectedProduct = null
                                },

                                onAddToCart = { productId, sizeId ->

                                    cartViewModel.addToCart(
                                        productId,
                                        sizeId
                                    )
                                }

                            )
                        }
                        if (showOrderDialog) {

                            androidx.compose.material3.AlertDialog(

                                onDismissRequest = {
                                    showOrderDialog = false
                                },

                                title = {
                                    Text("Заказ оформлен")
                                },

                                text = {
                                    Text("Спасибо за покупку!")
                                },

                                confirmButton = {

                                    TextButton(
                                        onClick = {

                                            cartViewModel.clearCart()

                                            showOrderDialog = false
                                        }
                                    ) {
                                        Text("OK")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}