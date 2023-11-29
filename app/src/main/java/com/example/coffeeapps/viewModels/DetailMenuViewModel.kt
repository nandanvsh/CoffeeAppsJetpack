package com.example.coffeeapps.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeapps.data.MenuRepository
import com.example.coffeeapps.data.model.Menu
import com.example.coffeeapps.data.model.OrderMenu
import com.example.coffeeapps.screen.StateScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailMenuViewModel(private val repository: MenuRepository) : ViewModel() {
    private val _stateScreen : MutableStateFlow<StateScreen<OrderMenu>> = MutableStateFlow(
        StateScreen.Loading)
    val stateScreen: StateFlow<StateScreen<OrderMenu>>
        get() = _stateScreen

    fun getMenuById(idMenu: Long) {
        viewModelScope.launch {
            _stateScreen.value = StateScreen.Loading
            _stateScreen.value = StateScreen.Success(repository.getOrderMenuById(idMenu))
        }
    }
    fun addToKeranjang(menu: Menu, count: Int ){
        viewModelScope.launch {
            repository.updateOrderMenu(menu.id, count)
        }

    }
}