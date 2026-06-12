package com.yoru.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import model.Product
import model.Size
import androidx.compose.material3.Button
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductBottomSheet(
    product: Product,
    onDismiss: () -> Unit,
    onAddToCart: (String, String) -> Unit
){

    var selectedSize by remember {
        mutableStateOf<Size?>(
            product.sizes.firstOrNull()
        )
    }
    var showInfoDialog by remember {
        mutableStateOf(false)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState()
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Box {

                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxWidth()
                )

                FlowRow(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    product.tags.forEach { tag ->

                        AssistChip(
                            onClick = {},
                            label = {
                                Text(tag)
                            }
                        )
                    }
                }
            }

            Text(
                text = product.name,
                modifier = Modifier.padding(top = 12.dp)
            )

            Text(
                text = "${product.priceInKopecks / 100} ₽",
                modifier = Modifier.padding(top = 8.dp)
            )
            IconButton(
                onClick = {
                    showInfoDialog = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Информация"
                )
            }

            Text(
                text = "Размер"
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                product.sizes.forEach { size ->

                    FilterChip(
                        selected = selectedSize == size,
                        onClick = {
                            selectedSize = size
                        },
                        label = {
                            Text(size.name)
                        }
                    )
                }
            }

            Text(
                text = product.longDescription,
                modifier = Modifier.padding(top = 12.dp)
            )
            Button(
                onClick = {

                    selectedSize?.let {

                        onAddToCart(
                            product.id,
                            it.id
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("В корзину")
            }
        }
    }
    if (showInfoDialog) {

        AlertDialog(
            onDismissRequest = {
                showInfoDialog = false
            },

            title = {
                Text("Характеристики")
            },

            text = {
                Text(
                    """
Материал: ${product.material}

Вес: ${product.weight}

Сезон: ${product.season}

Страна производства: ${product.countryOfOrigin}
                """.trimIndent()
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        showInfoDialog = false
                    }
                ) {
                    Text("Закрыть")
                }
            }
        )
    }
}
