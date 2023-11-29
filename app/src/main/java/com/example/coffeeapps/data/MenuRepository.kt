package com.example.coffeeapps.data

import com.example.coffeeapps.data.model.DataMenu
import com.example.coffeeapps.data.model.Menu
import com.example.coffeeapps.data.model.OrderMenu
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class MenuRepository {
    private val orderMenus = mutableListOf<OrderMenu>()

    init {
        if (orderMenus.isEmpty()) {
            DataMenu.menus.forEach {
                orderMenus.add(OrderMenu(it, 0))
            }
        }
    }
    fun getMenu(): Flow<List<OrderMenu>> {
        return flowOf(orderMenus)
    }

    fun getDataMenu(): List<Menu>{
        return DataMenu.menus
    }
    fun getOrderMenuById(idMenu: Long): OrderMenu {
        return orderMenus.first {
            it.orderMenu.id == idMenu
        }
    }
    fun updateOrderMenu(idMenu: Long, newCountValue: Int): Flow<Boolean> {
        val index = orderMenus.indexOfFirst { it.orderMenu.id == idMenu }
        val result = if (index >= 0) {
            val orderMenu = orderMenus[index]
            orderMenus[index] =
                orderMenu.copy(orderMenu = orderMenu.orderMenu, count = newCountValue)
            true
        } else {
            false
        }
        return flowOf(result)
    }
    fun getAddedOrderMenu(): Flow<List<OrderMenu>> {
        return getMenu()
            .map { orderMenus ->
                orderMenus.filter { orderMenu ->
                    orderMenu.count != 0
                }
            }
    }
    fun searchMenus(query: String) :Flow<List<OrderMenu>>{
        orderMenus.clear()
        filterMenus(query).forEach{

            orderMenus.add(OrderMenu(it,0))
        }
        return flowOf(orderMenus)
    }

    fun filterMenus(query: String): List<Menu>{
        return DataMenu.menus.filter {
            it.title.contains(query, ignoreCase = true)
        }
    }

    companion object {
        @Volatile
        private var instance: MenuRepository? = null

        fun getInstance(): MenuRepository =
            instance ?: synchronized(this) {
                MenuRepository().apply {
                    instance = this
                }
            }
    }
}