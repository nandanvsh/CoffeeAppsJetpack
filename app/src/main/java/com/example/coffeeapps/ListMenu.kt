package com.example.coffeeapps

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.coffeeapps.data.MenuRepository
import com.example.coffeeapps.screen.components.navigation.Search
import com.example.coffeeapps.screen.components.navigation.NavigationItem
import com.example.coffeeapps.screen.components.navigation.Screen
import com.example.coffeeapps.screen.detail.Detail
import com.example.coffeeapps.screen.fav.KeranjangScreen
import com.example.coffeeapps.screen.home.HomeScreen
import com.example.coffeeapps.screen.profile.Profile
import com.example.coffeeapps.ui.theme.CoffeeAppsTheme
import com.example.coffeeapps.viewModels.SearchViewModel
import com.example.coffeeapps.viewModels.ViewModelFactory


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CoffeeApps(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold (
        bottomBar = {
            if (currentRoute != Screen.DetailMenu.route) {
                BottomBar(navController)
            }
        },
        modifier = modifier
    ){ innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ){
                composable(Screen.Home.route) {
                    HomeScreen(navigateToDetail = {
                            idMenu ->
                        navController.navigate(Screen.DetailMenu.createRoute(idMenu))
                    })
                }
                composable(Screen.Keranjang.route) {
                    val context = LocalContext.current
                    KeranjangScreen(
                        onOrderButtonClicked = { message ->
                            shareOrder(context, message)
                        }
                    )
                }
                composable( Screen.Profile.route){
                    Profile()
                }
                composable(
                    route = Screen.DetailMenu.route,
                    arguments = listOf(navArgument("idMenu") { type = NavType.LongType }),
                ) {
                    val id = it.arguments?.getLong("idMenu") ?: -1L
                    Detail(
                        idMenu = id,
                        navigateBack = {
                            navController.navigateUp()
                        },
                        navigateToKeranjang = {
                            navController.popBackStack()
                            navController.navigate(Screen.Keranjang.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }

            }

        }


}
private fun shareOrder(context: Context, summary: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.menu_keranjang))
        putExtra(Intent.EXTRA_TEXT, summary)
    }

    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.menu_keranjang)
        )
    )
}


@Preview(showBackground = true)
@Composable
fun CoffeeAppsPreview() {
    CoffeeAppsTheme {
        CoffeeApps()
    }
}


@Composable
fun Banner(
    viewModel: SearchViewModel = viewModel(factory = ViewModelFactory(MenuRepository())),
    modifier: Modifier = Modifier,
){
    val query by viewModel.query
    Box(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.banner),
            contentDescription = "Banner Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.height(120.dp)
        )
        Search(
            query = query,
            onQueryChange = viewModel::search
        )
    }
}
@Composable
fun SectionText(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.ExtraBold
        ),
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}



@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar (
        modifier = modifier,
        ){
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.menu_home),
                icon = Icons.Default.Home,
                screen = Screen.Home
            ),
            NavigationItem(
                title = stringResource(R.string.menu_keranjang),
                icon = Icons.Default.ShoppingCart,
                screen = Screen.Keranjang
            ),
            NavigationItem(
                title = stringResource(R.string.menu_profile),
                icon = Icons.Default.AccountCircle,
                screen = Screen.Profile
            ),
        )
        navigationItems.map { item ->
            NavigationBarItem(
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title)
            },
                label = { Text(item.title) },
                )

        }

    }
}

