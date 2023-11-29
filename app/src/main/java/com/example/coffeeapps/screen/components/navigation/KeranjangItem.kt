package com.example.coffeeapps.screen.components.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.coffeeapps.R
import com.example.coffeeapps.ui.theme.CoffeeAppsTheme

@Composable
fun CartItem(
    idMenu: Long,
    image: Int,
    title: String,
    price: Int,
    count: Int,
    onProductCountChanged: (id: Long, count: Int) -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier.fillMaxWidth()
    ){
        Image(
            painter = painterResource(R.drawable.menu1),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)

        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(1.0f)
        ) {
            Text(
                text = title,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            Text(
                text = stringResource(
                    R.string.required_price,
                    price
                ),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall,
            )
        }
        ProductCounter(
            idMenu = idMenu,
            orderCount = count,
            onProductIncreased = { onProductCountChanged(idMenu, count + 1) },
            onProductDecreased = { onProductCountChanged(idMenu, count - 1) },
            modifier = Modifier.padding(8.dp)
        )
    }

}

@Composable
@Preview(showBackground = true)
fun CartItemPreview() {
    CoffeeAppsTheme {
        CartItem(
            4, R.drawable.menu1, "Tiramisu Coffee Milk", 4000, 0,
            onProductCountChanged = { rewardId, count -> },
        )
    }
}