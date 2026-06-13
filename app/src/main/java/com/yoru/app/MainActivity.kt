package com.yoru.app

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.height
import coil.compose.AsyncImage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
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

                var customerName by remember {
                    mutableStateOf("")
                }

                var customerEmail by remember {
                    mutableStateOf("")
                }

                var customerComment by remember {
                    mutableStateOf("")
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

                            cartCount =
                                cartViewModel.cartItems.sumOf {
                                    it.quantity
                                },

                            onScreenSelected = {
                                selectedScreen = it
                            }
                        )
                    }

                ) { paddingValues ->

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {

                        Image(
                            painter = painterResource(
                                R.drawable.yoru_background2
                            ),

                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Color.Black.copy(alpha = 0.55f)
                                )
                        )

                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {

                            when (selectedScreen) {

                            "catalog" -> {
                                Column(

                                    modifier = Modifier
                                        .padding(12.dp)
                                        .background(
                                            Color(0xCC181312)
                                        )
                                        .padding(16.dp)

                                ) {

                                    Text(
                                        text = "Y O R U",

                                        fontSize = 42.sp,

                                        color = Color.White
                                    )
                                }


                                Row(
                                    modifier = Modifier
                                        .horizontalScroll(
                                            rememberScrollState()
                                        )
                                        .padding(
                                            horizontal = 12.dp,
                                            vertical = 12.dp
                                        )
                                ) {

                                    categories.forEach { category ->

                                        val isSelected =
                                            selectedCategory == category

                                        Text(

                                            text = category,

                                            modifier = Modifier
                                                .padding(end = 8.dp)
                                                .background(

                                                    if (isSelected)
                                                        Color(0xCC181312)
                                                    else
                                                        Color.Transparent,

                                                    RoundedCornerShape(8.dp)

                                                )
                                                .clickable {
                                                    selectedCategory = category
                                                }
                                                .padding(
                                                    horizontal = 16.dp,
                                                    vertical = 10.dp
                                                ),

                                            color =
                                                if (isSelected)
                                                    Color.White
                                                else
                                                    Color.LightGray
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

                                    if (cartViewModel.cartItems.isEmpty()) {

                                        Column(
                                            modifier = Modifier.padding(24.dp)
                                        ) {

                                            Text(
                                                text = "◐",

                                                fontSize = 48.sp
                                            )

                                            Text(
                                                text = "Ваш архив пока пуст",

                                                fontSize = 22.sp,

                                                modifier = Modifier.padding(
                                                    top = 8.dp
                                                )
                                            )

                                            Text(
                                                text = "Добавьте что-нибудь из коллекции",

                                                color = Color.Gray,

                                                modifier = Modifier.padding(
                                                    top = 4.dp
                                                )
                                            )
                                        }

                                    } else {

                                        cartViewModel.cartItems.forEach { item ->

                                            val product =
                                                viewModel.getProductById(
                                                    item.productId
                                                )

                                            if (product != null) {

                                                Card(

                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(
                                                            horizontal = 16.dp,
                                                            vertical = 8.dp
                                                        ),

                                                    colors = CardDefaults.cardColors(
                                                        containerColor = Color(0x22181312)
                                                    ),

                                                    border = BorderStroke(
                                                        1.dp,
                                                        Color(0x33FFFFFF)
                                                    )

                                                ) {

                                                    Row(
                                                        modifier = Modifier.padding(16.dp)
                                                    ) {

                                                        AsyncImage(

                                                            model = product.imageUrl,

                                                            contentDescription = product.name,

                                                            modifier = Modifier
                                                                .height(70.dp)
                                                                .fillMaxWidth(0.22f),

                                                            contentScale = ContentScale.Crop
                                                        )

                                                        Column(
                                                            modifier = Modifier.padding(start = 12.dp)
                                                        ) {

                                                            Text(
                                                                text = product.name,

                                                                color = Color.White,

                                                                fontSize = 18.sp
                                                            )

                                                            Text(
                                                                text =
                                                                    "${product.priceInKopecks * item.quantity / 100} ₽",

                                                                color = Color.LightGray,

                                                                modifier = Modifier.padding(
                                                                    top = 4.dp,
                                                                    bottom = 8.dp
                                                                )
                                                            )

                                                            Row {

                                                                TextButton(
                                                                    onClick = {
                                                                        cartViewModel.decreaseQuantity(
                                                                            this@MainActivity,
                                                                            item.productId,
                                                                            item.sizeId
                                                                        )
                                                                    }
                                                                ) {
                                                                    Text(

                                                                        text = "-",

                                                                        modifier = Modifier
                                                                            .background(
                                                                                Color(0x33FFFFFF),
                                                                                RoundedCornerShape(50)
                                                                            )
                                                                            .padding(
                                                                                horizontal = 10.dp,
                                                                                vertical = 4.dp
                                                                            )
                                                                    )
                                                                }

                                                                Text(
                                                                    text = item.quantity.toString(),

                                                                    color = Color.White,

                                                                    fontSize = 18.sp,

                                                                    fontWeight = FontWeight.Bold,

                                                                    modifier = Modifier.padding(
                                                                        horizontal = 12.dp
                                                                    )
                                                                )

                                                                TextButton(
                                                                    onClick = {
                                                                        cartViewModel.increaseQuantity(
                                                                            this@MainActivity,
                                                                            item.productId,
                                                                            item.sizeId
                                                                        )
                                                                    }
                                                                ) {
                                                                    Text(

                                                                        text = "+",

                                                                        modifier = Modifier
                                                                            .background(
                                                                                Color(0x33FFFFFF),
                                                                                RoundedCornerShape(50)
                                                                            )
                                                                            .padding(
                                                                                horizontal = 10.dp,
                                                                                vertical = 4.dp
                                                                            )
                                                                    )
                                                                }
                                                            }

                                                            Text(
                                                                text = "Удалить",

                                                                color = Color.Gray,

                                                                fontSize = 12.sp,

                                                                modifier = Modifier
                                                                    .clickable {
                                                                        cartViewModel.removeItem(
                                                                            this@MainActivity,
                                                                            item.productId,
                                                                            item.sizeId
                                                                        )
                                                                    }
                                                                    .padding(top = 4.dp)
                                                            )
                                                        }
                                                    }
                                                }
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
                                        OutlinedTextField(
                                            value = customerName,
                                            onValueChange = {
                                                customerName = it
                                            },
                                            label = {
                                                Text("Имя")
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        )

                                        OutlinedTextField(
                                            value = customerEmail,
                                            onValueChange = {
                                                customerEmail = it
                                            },
                                            label = {
                                                Text("Email")
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        )

                                        OutlinedTextField(
                                            value = customerComment,
                                            onValueChange = {
                                                customerComment = it
                                            },
                                            label = {
                                                Text("Комментарий")
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        )
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

                                            cartViewModel.clearCart(
                                                this@MainActivity
                                            )

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
                                        this@MainActivity,
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

                                            cartViewModel.clearCart(
                                                this@MainActivity
                                            )

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
}