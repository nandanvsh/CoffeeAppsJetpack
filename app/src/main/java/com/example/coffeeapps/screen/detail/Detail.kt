package com.example.coffeeapps.screen.detail

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coffeeapps.R
import com.example.coffeeapps.data.di.Injection
import com.example.coffeeapps.screen.StateScreen
import com.example.coffeeapps.screen.components.navigation.ProductCounter
import com.example.coffeeapps.ui.theme.CoffeeAppsTheme
import com.example.coffeeapps.viewModels.DetailMenuViewModel
import com.example.coffeeapps.viewModels.ViewModelFactory

@Composable
fun Detail(
    idMenu: Long,
    viewModel: DetailMenuViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository()
        )
    ),
    navigateBack: () -> Unit,
    navigateToKeranjang: () -> Unit
) {
    viewModel.stateScreen.collectAsState(initial = StateScreen.Loading).value.let { stateScreen ->
        when (stateScreen) {
            is StateScreen.Loading -> {
                viewModel.getMenuById(idMenu)
            }
            is StateScreen.Success -> {
                val data = stateScreen.data
                DetailContent(
                    data.orderMenu.image,
                    data.orderMenu.title,
                    data.orderMenu.price,
                    data.count,
                    data.orderMenu.description,
                    onBackClick = navigateBack,
                    onAddToCart = { count ->
                        viewModel.addToKeranjang(data.orderMenu, count)
                        navigateToKeranjang()
                    }
                )
            }
            is StateScreen.Error -> {}
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun DetailContent(
    @DrawableRes image: Int,
    title: String,
    price: Int,
    count: Int,
    description: String,
    onBackClick: () -> Unit,
    onAddToCart: (count: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var totalPrice by rememberSaveable { mutableStateOf(0) }
    var orderCount by rememberSaveable { mutableStateOf(count) }

    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            Box {
                Image(
                    painter = painterResource(image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .height(400.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                )
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { onBackClick() }
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(35.dp)
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                )
                Text(
                    text = stringResource(R.string.required_price, price),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                )
            }
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(Color.LightGray))
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            ProductCounter(
                1,
                orderCount,
                onProductIncreased = { orderCount++ },
                onProductDecreased = { if (orderCount > 0) orderCount-- },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )
            totalPrice = price * orderCount
                OrderButton(
                    text = stringResource(R.string.add_to_fav),
                    enabled = orderCount > 0,
                    onClick = {
                        onAddToCart(orderCount)
                    }
                )
        }
    }
}

@Composable
fun OrderButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun DetailContentPreview() {
    CoffeeAppsTheme {
        DetailContent(
            R.drawable.menu1,
            "Tiramisu Coffee Milk",
            28000,
            1,
            "Tiramisu Coffee Milk adalah minuman kopi yang lezat dengan sentuhan tiramisu yang khas. Kopi yang kuat dipadukan dengan susu yang lembut, memberikan rasa kaya dan gurih, sementara hint tiramisu menambah sentuhan manis yang memanjakan lidah.",
            onBackClick = {},
            onAddToCart = {}
        )
    }
}