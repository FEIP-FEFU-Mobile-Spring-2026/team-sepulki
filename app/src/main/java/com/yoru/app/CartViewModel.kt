package com.yoru.app

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import model.CartItem

class CartViewModel : ViewModel() {


    fun loadCart(
        context: Context
    ) {

        cartItems =
            CartStorage.loadCart(context)
    }

    private fun saveCart(
        context: Context
    ) {

        CartStorage.saveCart(
            context,
            cartItems
        )
    }

    var cartItems by mutableStateOf<List<CartItem>>(emptyList())
        private set


    fun addToCart(
        context: Context,
        productId: String,
        sizeId: String
    ){

        val existingItem = cartItems.find {

            it.productId == productId &&
                    it.sizeId == sizeId
        }

        if (existingItem != null) {

            cartItems = cartItems.map {

                if (
                    it.productId == productId &&
                    it.sizeId == sizeId
                ) {

                    it.copy(
                        quantity = it.quantity + 1
                    )

                } else {

                    it
                }
            }

        } else {

            cartItems = cartItems + CartItem(
                productId = productId,
                sizeId = sizeId,
                quantity = 1
            )
        }
        saveCart(context)
    }

    fun increaseQuantity(
        context: Context,
        productId: String,
        sizeId: String
    ) {

        cartItems = cartItems.map {

            if (
                it.productId == productId &&
                it.sizeId == sizeId
            ) {

                it.copy(
                    quantity = it.quantity + 1
                )

            } else {

                it
            }
        }

        saveCart(context)
    }

    fun decreaseQuantity(
        context: Context,
        productId: String,
        sizeId: String
    ) {

        cartItems = cartItems.mapNotNull {

            if (
                it.productId == productId &&
                it.sizeId == sizeId
            ) {

                if (it.quantity > 1) {

                    it.copy(
                        quantity = it.quantity - 1
                    )

                } else {

                    null
                }

            } else {

                it
            }
        }

        saveCart(context)
    }
    fun removeItem(
        context: Context,
        productId: String,
        sizeId: String
    ) {

        cartItems = cartItems.filterNot {

            it.productId == productId &&
                    it.sizeId == sizeId
        }

        saveCart(context)
    }

    fun getTotalPrice(
        catalogViewModel: CatalogViewModel
    ): Int {

        return cartItems.sumOf { item ->

            val product =
                catalogViewModel.getProductById(
                    item.productId
                )

            (product?.priceInKopecks ?: 0) *
                    item.quantity
        }

    }

    fun clearCart(
        context: Context
    ) {

        cartItems = emptyList()

        saveCart(context)
    }
}


