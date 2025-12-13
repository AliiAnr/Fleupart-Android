package com.course.fleupart.ui.screen.dashboard.product

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.course.fleupart.data.model.remote.CreateProductPayload
import com.course.fleupart.data.model.remote.GetAllCategoryResponse
import com.course.fleupart.data.model.remote.PersonalizeResponse
import com.course.fleupart.data.repository.ProductRepository
import com.course.fleupart.data.resource.Resource
import com.course.fleupart.ui.common.ImageCompressor
import com.course.fleupart.ui.common.ResultResponse
import com.course.fleupart.ui.theme.err
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.text.set


class ProductViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _productState =
        MutableStateFlow<ResultResponse<PersonalizeResponse>>(ResultResponse.None)
    val productState: StateFlow<ResultResponse<PersonalizeResponse>> = _productState

    var errorMessage = MutableStateFlow("")

    private val _categoryState = MutableStateFlow<ResultResponse<GetAllCategoryResponse>>(ResultResponse.None)
    val categoryState: StateFlow<ResultResponse<GetAllCategoryResponse>> = _categoryState

    var nameValue by mutableStateOf("")
        private set
    var stockValue by mutableStateOf("")
        private set
    var descriptionValue by mutableStateOf("")
        private set
    var arrangeTimeValue by mutableStateOf("")
        private set
    var pointValue by mutableStateOf("")
        private set
    var priceValue by mutableStateOf("")
        private set
    var categoryIdValue by mutableStateOf("")
        private set

    var isPreOrderValue by mutableStateOf(false)
        private set

    private val _productImages = mutableStateListOf<Uri>()
    val productImages: List<Uri> get() = _productImages

    private val productImageFiles = mutableStateListOf<File>()

    fun setName(value: String) {
        nameValue = value
    }

    fun setStock(value: String) {
        stockValue = value
    }

    fun setPreOrder(value: Boolean) {
        isPreOrderValue = value
    }


    fun setDescription(value: String) {
        descriptionValue = value
    }

    fun setArrangeTime(value: String) {
        arrangeTimeValue = value
    }

    fun setPoint(value: String) {
        pointValue = value
    }

    fun setPrice(value: String) {
        priceValue = value
    }

    fun setCategoryId(value: String) {
        categoryIdValue = value
    }

    fun resetProductState() {
        _productState.value = ResultResponse.None
    }

    fun getAllCategory() {
        viewModelScope.launch {
            try {
                _categoryState.value = ResultResponse.Loading
                productRepository.getAllCategory().collect { result ->
                    _categoryState.value = result
                }
            } catch (e: Exception) {
                _categoryState.value = ResultResponse.Error("Exception: ${e.message}")
            }
        }
    }

    fun replaceProductImage(index: Int, uri: Uri) {
        if (index !in _productImages.indices) return

        viewModelScope.launch {
            val tempFile = File.createTempFile(
                "temp_product_${System.currentTimeMillis()}",
                ".jpeg",
                Resource.appContext.cacheDir
            )

            val success = withContext(Dispatchers.IO) {
                ImageCompressor.compressImage(Resource.appContext, uri, tempFile)
            }

            if (success) {
                productImageFiles.getOrNull(index)?.delete()
                _productImages[index] = Uri.fromFile(tempFile)
                productImageFiles[index] = tempFile
            } else {
                tempFile.delete()
            }
        }
    }

    fun addProductImage(uri: Uri) {
        viewModelScope.launch {
            val tempFile = File.createTempFile(
                "temp_product_${System.currentTimeMillis()}",
                ".jpeg",
                Resource.appContext.cacheDir
            )

            val success = withContext(Dispatchers.IO) {
                ImageCompressor.compressImage(Resource.appContext, uri, tempFile)
            }

            if (success) {
                _productImages.add(Uri.fromFile(tempFile))
                productImageFiles.add(tempFile)
            } else {
                tempFile.delete()
            }
        }
    }

    fun removeProductImage(index: Int) {
        if (index in _productImages.indices) {
            productImageFiles.getOrNull(index)?.delete()
            productImageFiles.removeAt(index)
            _productImages.removeAt(index)
        }
    }

    fun createProduct() {
        val payload = buildPayload() ?: return
        val filesSnapshot = productImageFiles.toList()

        viewModelScope.launch {
            try {
                productRepository.createProductWithCategory(payload, filesSnapshot)
                    .collect { result ->
                        _productState.value = result
                    }
            } catch (e: Exception) {
                _productState.value = ResultResponse.Error("Create product failed: ${e.message}")
            } finally {
                filesSnapshot.forEach { if (it.exists()) it.delete() }
            }
        }
    }

    private fun buildPayload(): CreateProductPayload? {
        val stock = stockValue.toIntOrNull()
        val point = pointValue.toIntOrNull()
        val price = priceValue.toLongOrNull()

        return when {
            nameValue.isBlank() -> {
                errorMessage.value = "Name is required"
                null
            }

            stock == null || stock <= 0 -> {
                errorMessage.value = "Stock must be a positive number"
                null
            }

            descriptionValue.isBlank() -> {
                errorMessage.value = "Description is required"
                null
            }

            arrangeTimeValue.isBlank() -> {
                errorMessage.value = "Arrange time is required"
                null
            }

            point == null || point < 0 -> {
                errorMessage.value = "Point must be zero or greater"
                null
            }

            price == null || price <= 0 -> {
                errorMessage.value = "Price must be a positive number"
                null
            }

            categoryIdValue.isBlank() -> {
                errorMessage.value = "Category is required"
                null
            }

            productImageFiles.isEmpty() -> {
                errorMessage.value = "Please add at least one product image"
                null
            }

            else -> CreateProductPayload(
                name = nameValue,
                stock = stock,
                description = descriptionValue,
                arrangeTime = arrangeTimeValue,
                point = point,
                price = price,
                categoryId = categoryIdValue
            )
        }
    }

     fun clearProductForm() {
        nameValue = ""
        stockValue = ""
         isPreOrderValue = false
        descriptionValue = ""
        arrangeTimeValue = ""
         errorMessage.value = ""
        pointValue = ""
        priceValue = ""
        categoryIdValue = ""
         _productState.value = ResultResponse.None
         _categoryState.value = ResultResponse.None
        _productImages.forEachIndexed { index, _ ->
            productImageFiles.getOrNull(index)?.delete()
        }
        _productImages.clear()
        productImageFiles.clear()
    }
}