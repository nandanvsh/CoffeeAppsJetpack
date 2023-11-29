package com.example.coffeeapps.screen.fav

import com.example.coffeeapps.data.model.OrderMenu

data class KeranjangState(
    val orderReward: List<OrderMenu>,
    val totalRequiredPoint: Int
)
