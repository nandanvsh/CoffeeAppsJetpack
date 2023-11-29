package com.example.coffeeapps.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeapps.data.MenuRepository

class ViewModelFactory(private val repository: MenuRepository) : ViewModelProvider.NewInstanceFactory(){
//    companion object{
//        @Volatile
//        private var INSTANCE: ViewModelFactory? = null
//
//        @JvmStatic
//        fun getInstance(repository: MenuRepository): ViewModelFactory{
//            if (INSTANCE == null) {
//                synchronized(ViewModelFactory::class.java){
//                    INSTANCE = ViewModelFactory(repository)
//                }
//            }
//            return INSTANCE as ViewModelFactory
//        }
//    }
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
            return MenuViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(DetailMenuViewModel::class.java)){
            return DetailMenuViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(KeranjangViewModel::class.java)){
            return KeranjangViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(SearchViewModel::class.java)){
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
}
}