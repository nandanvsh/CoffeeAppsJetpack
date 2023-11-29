package com.example.coffeeapps.screen.fav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coffeeapps.CoffeeApps
import com.example.coffeeapps.R
import com.example.coffeeapps.data.di.Injection
import com.example.coffeeapps.screen.StateScreen
import com.example.coffeeapps.screen.components.navigation.CartItem
import com.example.coffeeapps.screen.detail.OrderButton
import com.example.coffeeapps.ui.theme.CoffeeAppsTheme
import com.example.coffeeapps.viewModels.KeranjangViewModel
import com.example.coffeeapps.viewModels.ViewModelFactory

@Composable
fun KeranjangScreen(
    viewModel: KeranjangViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository()
        )
    ),
    onOrderButtonClicked: (String) -> Unit,
){
    viewModel.stateScreen.collectAsState(initial = StateScreen.Loading).value.let { stateScreen ->
        when (stateScreen) {
            is StateScreen.Loading -> {
                viewModel.getAllOrder()
            }
            is StateScreen.Success -> {
                KeranjangContent(
                    stateScreen.data,
                    onProductCountChanged = {
                                            idMenu, count ->
                        viewModel.updateOrder(idMenu, count)
                    }, onOrderButtonClicked = onOrderButtonClicked
                )
            }
            is StateScreen.Error -> {}
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeranjangContent(
    state: KeranjangState,
    onProductCountChanged: (id: Long, count: Int) -> Unit,
    onOrderButtonClicked: (String) -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.menu_keranjang),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(weight = 1f)
        ) {
            items(state.orderReward, key = {it.orderMenu.id}){item ->
                CartItem(
                    idMenu = item.orderMenu.id,
                    image = item.orderMenu.image,
                    title = item.orderMenu.title,
                    price = item.orderMenu.price,
                    count = item.count,
                    onProductCountChanged = onProductCountChanged
                )
                Divider()
            }
        }
        OrderButton(
            text = stringResource(R.string.total_order, state.totalRequiredPoint),
            enabled = state.orderReward.isNotEmpty(),
            onClick = {
                onOrderButtonClicked("")
            },
            modifier = Modifier.padding(16.dp)
        )
    }

}
@Preview(showBackground = true)
@Composable
fun CoffeeAppsPreview() {
    CoffeeAppsTheme {
        CoffeeApps()
    }
}