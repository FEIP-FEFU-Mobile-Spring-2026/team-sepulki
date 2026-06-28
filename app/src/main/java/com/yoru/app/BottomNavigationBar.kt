package com.yoru.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun BottomNavigationBar(
    selectedScreen: String,
    cartCount: Int,
    onScreenSelected: (String) -> Unit
) {

    NavigationBar(

        containerColor = Color(0xCC181312)

    ) {

        NavigationBarItem(

            selected = selectedScreen == "catalog",

            onClick = {
                onScreenSelected("catalog")
            },

            label = {
                Text("КАТАЛОГ")
            },

            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Каталог"
                )
            }
        )

        NavigationBarItem(

            selected = selectedScreen == "cart",

            onClick = {
                onScreenSelected("cart")
            },

            label = {

                Text(

                    if (cartCount > 0)
                        "КОРЗИНА $cartCount"
                    else
                        "КОРЗИНА"
                )
            },

            icon = {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = "Корзина"
                )
            }
        )
    }
}