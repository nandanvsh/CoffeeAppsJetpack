package com.example.coffeeapps.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeapps.data.MenuRepository
import com.example.coffeeapps.data.model.Menu
import com.example.coffeeapps.data.model.OrderMenu
import com.example.coffeeapps.screen.StateScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: MenuRepository) : ViewModel() {
    private val _stateScreen : MutableStateFlow<StateScreen<List<OrderMenu>>> = MutableStateFlow(
        StateScreen.Loading)
    val stateScreen: StateFlow<StateScreen<List<OrderMenu>>>
        get() = _stateScreen

    private val _sortedMenu = MutableStateFlow(
        repository.getDataMenu().sortedBy { it.title }.groupBy { it.title[0] }
    )
    val groupMenu: StateFlow<Map<Char,List<Menu>>> get() = _sortedMenu

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun search(newQuery: String) {
        _query.value = newQuery
        viewModelScope.launch{
            repository.searchMenus(_query.value)
                .catch{
                _stateScreen.value = StateScreen.Error(it.message.toString()) }
                .collect { orderMenus ->
                    _stateScreen.value = StateScreen.Success(orderMenus)
                }
        }
    }
}