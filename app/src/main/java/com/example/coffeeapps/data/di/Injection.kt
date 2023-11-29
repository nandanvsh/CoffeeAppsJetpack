package com.example.coffeeapps.data.di

import com.example.coffeeapps.data.MenuRepository

object Injection {
    fun provideRepository(): MenuRepository {
        return MenuRepository.getInstance()
    }
}