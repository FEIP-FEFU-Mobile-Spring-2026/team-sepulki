package com.yoru.app

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import model.CartItem

object CartStorage {

    private const val PREFS_NAME = "yoru_cart"

    private const val CART_KEY = "cart_items"

    fun saveCart(
        context: Context,
        cartItems: List<CartItem>
    ) {

        val prefs =
            context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )

        val json =
            Gson().toJson(cartItems)

        prefs.edit()
            .putString(
                CART_KEY,
                json
            )
            .apply()
    }

    fun loadCart(
        context: Context
    ): List<CartItem> {

        val prefs =
            context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )

        val json =
            prefs.getString(
                CART_KEY,
                null
            )

        if (json == null) {
            return emptyList()
        }

        val type =
            object : TypeToken<List<CartItem>>() {}.type

        return Gson().fromJson(
            json,
            type
        )
    }

}
