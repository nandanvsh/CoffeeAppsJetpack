package com.example.coffeeapps.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coffeeapps.Banner
import com.example.coffeeapps.R
import com.example.coffeeapps.SectionText
import com.example.coffeeapps.data.di.Injection
import com.example.coffeeapps.screen.components.navigation.MenuItem
import com.example.coffeeapps.data.model.OrderMenu
import com.example.coffeeapps.screen.StateScreen
import com.example.coffeeapps.viewModels.MenuViewModel
import com.example.coffeeapps.viewModels.ViewModelFactory


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MenuViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetail: (Long) -> Unit,
){
    viewModel.stateScreen.collectAsState(initial = StateScreen.Loading).value.let { stateScreen ->
        when (stateScreen) {
            is StateScreen.Loading -> {
                viewModel.getAllMenus()
            }
            is StateScreen.Success -> {
                MenuColumn(
                    listMenu = stateScreen.data,
                    modifier = modifier,
                    navigateToDetail = navigateToDetail,
                )
            }
            is StateScreen.Error -> {}
        }
    }
}
@Composable
fun MenuColumn(
    listMenu: List<OrderMenu>,
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit
){
    Column {
        Banner()
        SectionText(stringResource(R.string.section_menu))
        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
        ){
            items(listMenu){
                    data ->
                MenuItem(
                    image = data.orderMenu.image,
                    title = data.orderMenu.title,
                    price = data.orderMenu.price,
                    modifier = Modifier.clickable {
                        navigateToDetail(data.orderMenu.id)
                    })
            }
        }
    }

}