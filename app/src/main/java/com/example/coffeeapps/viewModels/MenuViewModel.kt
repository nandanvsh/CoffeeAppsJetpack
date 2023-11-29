package com.example.coffeeapps.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeapps.data.MenuRepository
import com.example.coffeeapps.data.model.OrderMenu
import com.example.coffeeapps.screen.StateScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MenuViewModel(
    private val repository: MenuRepository
) : ViewModel() {
    private val _stateScreen : MutableStateFlow<StateScreen<List<OrderMenu>>> = MutableStateFlow(StateScreen.Loading)
    val stateScreen: StateFlow<StateScreen<List<OrderMenu>>>
        get() = _stateScreen

    fun getAllMenus(){
        viewModelScope.launch {
            repository.getMenu()
                .catch {
                    _stateScreen.value = StateScreen.Error(it.message.toString())
                }
                .collect { orderMenus ->
                    _stateScreen.value = StateScreen.Success(orderMenus)
                }
        }
    }
}