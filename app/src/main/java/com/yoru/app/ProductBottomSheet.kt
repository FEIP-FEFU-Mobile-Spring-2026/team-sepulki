package com.yoru.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductBottomSheet(
    product: Product,
    onDismiss: () -> Unit
) {

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Box {

                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxWidth()
                )

                FlowRow(
                    modifier = Modifier
                        .padding(12.dp),
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

            Text(
                text = product.longDescription,
                modifier = Modifier.padding(top = 12.dp)
            )

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
    }