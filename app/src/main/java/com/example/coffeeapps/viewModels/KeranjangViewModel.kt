package com.example.coffeeapps.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeapps.data.MenuRepository
import com.example.coffeeapps.data.model.OrderMenu
import com.example.coffeeapps.screen.StateScreen
import com.example.coffeeapps.screen.fav.KeranjangState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class KeranjangViewModel (private val repository: MenuRepository) : ViewModel() {
    private val _stateScreen : MutableStateFlow<StateScreen<KeranjangState>> = MutableStateFlow(
        StateScreen.Loading)
    val stateScreen: StateFlow<StateScreen<KeranjangState>>
        get() = _stateScreen

    fun getAllOrder() {
        viewModelScope.launch {
            _stateScreen.value = StateScreen.Loading
            repository.getAddedOrderMenu()
                .collect{orderMenu ->
                    val totalPrice =
                        orderMenu.sumOf { it.orderMenu.price * it.count }
                    _stateScreen.value = StateScreen.Success(KeranjangState(orderMenu, totalPrice))
                }
        }
    }

    fun updateOrder(idMenu: Long, count: Int){
        viewModelScope.launch {
            repository.updateOrderMenu(idMenu, count)
                .collect {isUpdate ->
                    if (isUpdate) {
                        getAllOrder()
                    }

                }
        }

    }

}