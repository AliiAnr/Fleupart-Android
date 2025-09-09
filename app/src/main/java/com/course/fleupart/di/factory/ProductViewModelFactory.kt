package com.course.fleupart.di.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.course.fleupart.data.repository.ProductRepository
import com.course.fleupart.data.repository.RegisterRepository
import com.course.fleupart.di.Injection
import com.course.fleupart.ui.screen.authentication.register.RegisterScreenViewModel
import com.course.fleupart.ui.screen.dashboard.product.ProductViewModel

class ProductViewModelFactory private constructor(private val productRepository: ProductRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ProductViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                ProductViewModel(productRepository = productRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ProductViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            context: Context
        ): ProductViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProductViewModelFactory(Injection.provideProductRepository(context)).also {
                    INSTANCE = it
                }
            }
    }
}