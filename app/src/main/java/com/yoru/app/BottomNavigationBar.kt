package com.yoru.app

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomNavigationBar(
    selectedScreen: String,
    onScreenSelected: (String) -> Unit
) {

    NavigationBar {

        NavigationBarItem(
            selected = selectedScreen == "catalog",
            onClick = {
                onScreenSelected("catalog")
            },
            label = {
                Text("Каталог")
            },
            icon = {}
        )

        NavigationBarItem(
            selected = selectedScreen == "cart",
            onClick = {
                onScreenSelected("cart")
            },
            label = {
                Text("Корзина")
            },
            icon = {}
        )
    }
}