package com.yoru.app

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import model.Product

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit
) {

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 12.dp
            )
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(8.dp),

        border = BorderStroke(
            1.dp,
            Color(0x33FFFFFF)
        ),

        colors = CardDefaults.cardColors(
            containerColor = Color(0x22181312)
        )
    ) {

        Column {

            Box {

                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),

                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(
                            Color.Black.copy(alpha = 0.15f)
                        )
                )
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = product.name,

                    style = MaterialTheme.typography.titleMedium,

                    fontWeight = FontWeight.Medium,

                    color = Color.White
                )

                Text(
                    text = "${product.priceInKopecks / 100} ₽",

                    fontSize = 16.sp,

                    color = Color.LightGray,

                    modifier = Modifier.padding(
                        top = 4.dp
                    )
                )
            }
        }
    }
}