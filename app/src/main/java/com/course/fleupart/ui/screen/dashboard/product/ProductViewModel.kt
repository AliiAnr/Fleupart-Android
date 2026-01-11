package com.course.fleupart.ui.screen.dashboard.product

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.course.fleupart.data.model.remote.ApiUpdateResponse
import com.course.fleupart.data.model.remote.CreateProductPayload
import com.course.fleupart.data.model.remote.DeleteProductRequest
import com.course.fleupart.data.model.remote.GetAllCategoryResponse
import com.course.fleupart.data.model.remote.PersonalizeResponse
import com.course.fleupart.data.model.remote.StoreProduct
import com.course.fleupart.data.repository.ProductRepository
import com.course.fleupart.data.resource.Resource
import com.course.fleupart.ui.common.ImageCompressor
import com.course.fleupart.ui.common.ResultResponse
import com.course.fleupart.ui.theme.err
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import kotlin.collections.addAll
import kotlin.collections.containsKey
import kotlin.collections.remove
import kotlin.text.category
import kotlin.text.clear
import kotlin.text.contains
import kotlin.text.get
import kotlin.text.set
import kotlin.toString


class ProductViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _updateProductState = MutableStateFlow<ResultResponse<PersonalizeResponse>>(ResultResponse.None)
    val updateProductState = _updateProductState.asStateFlow()

    private val _productState =
        MutableStateFlow<ResultResponse<PersonalizeResponse>>(ResultResponse.None)
    val productState: StateFlow<ResultResponse<PersonalizeResponse>> = _productState

    var errorMessage = MutableStateFlow("")
    var existingImageUrls = mutableStateListOf<String>()
        private set

    private val okHttpClient = OkHttpClient()
    private val cachedExistingImageFiles = mutableMapOf<String, File>()
    private var preloadExistingImagesJob: Job? = null

    private val _categoryState = MutableStateFlow<ResultResponse<GetAllCategoryResponse>>(ResultResponse.None)
    val categoryState: StateFlow<ResultResponse<GetAllCategoryResponse>> = _categoryState
    private var currentEditingProductId: String = ""

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


    fun setInitialEditData(product: StoreProduct) {
        currentEditingProductId = product.id
        setName(product.name)
        setDescription(product.description)
        setPrice(normalizeServerPrice(product.price))
        setStock(product.stock.toString())
        setPoint(product.point.toString())
        setArrangeTime(product.arrangeTime)
        setCategoryId(product.category.id)
        setPreOrder(product.preOrder)

        _productImages.clear()
        productImageFiles.forEach { it.delete() }
        productImageFiles.clear()

        preloadExistingImagesJob?.cancel()
        clearExistingCachedImages()

        val urls = product.picture.map { it.path }
        existingImageUrls.clear()
        existingImageUrls.addAll(urls)
        prepareExistingImageCache(urls)
    }

    private fun snapshotCurrentImageFiles(): List<File> {
        val retainedExisting = existingImageUrls.mapNotNull { cachedExistingImageFiles[it] }
        val newOnes = productImageFiles.toList()
        return retainedExisting + newOnes
    }

    private fun prepareExistingImageCache(urls: List<String>) {
        preloadExistingImagesJob = viewModelScope.launch {
            urls.forEach { url ->
                if (!existingImageUrls.contains(url)) return@forEach
                downloadImageToCache(url)?.let { cachedExistingImageFiles[url] = it }
            }
        }
    }

    private suspend fun ensureExistingImageFiles(): Boolean {
        preloadExistingImagesJob?.join()

        val missingUrls = existingImageUrls.filterNot { cachedExistingImageFiles.containsKey(it) }
        missingUrls.forEach { url ->
            downloadImageToCache(url)?.let { cachedExistingImageFiles[url] = it }
        }
        return cachedExistingImageFiles.isNotEmpty()
    }


    fun updateProduct() {
        val payload = buildPayload(requireNewImages = false) ?: return
        if (currentEditingProductId.isBlank()) {
            errorMessage.value = "Invalid product id"
            return
        }

        viewModelScope.launch {
            // Unduh (atau pastikan ada) file untuk gambar yang tetap dipertahankan
            if (existingImageUrls.isNotEmpty()) {
                ensureExistingImageFiles()
            }

            val files = snapshotCurrentImageFiles()
            if (files.isEmpty()) {
                errorMessage.value = "Please keep at least one product image"
                Log.e("ProductViewModel", "updateProduct: Please keep at least one product image")
                return@launch
            }

            productRepository.updateProductWithCategory(
                productId = currentEditingProductId,
                payload = payload,
                imageFiles = files
            ).collect { result -> _updateProductState.value = result }
        }
    }

    fun removeExistingImage(url: String) {
        existingImageUrls.remove(url)
        cachedExistingImageFiles.remove(url)?.delete()
    }
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

    fun resetUpdateState() {
        _updateProductState.value = ResultResponse.None
    }

//    fun updateProduct() {
//        viewModelScope.launch {
//            _updateProductState.value = ResultResponse.Loading
//            try {
//                // Prepare Text Parts
//                val namePart = createPartFromString(nameValue)
//                val descriptionPart = createPartFromString(descriptionValue)
//                val pricePart = createPartFromString(priceValue)
//                val stockPart = createPartFromString(stockValue)
//                val pointPart = createPartFromString(pointValue)
//                val arrangeTimePart = createPartFromString(arrangeTimeValue)
//                val categoryIdPart = createPartFromString(categoryIdValue)
//                val preOrderPart = createPartFromString(isPreOrderValue.toString())
//                val productIdPart = createPartFromString(currentEditingProductId)
//
//                // Prepare Image Parts (Only new URIs)
//                val imageParts = productImages.map { uri ->
//                    val file = File(uri.path ?: "") // You might need a proper Uri to File converter here
//                    // Note: In a real app, use a ContentResolver to get the file or stream
//                    // For this snippet, assuming you have a helper or using the same logic as AddProduct
////                    val requestFile = uri.asRequestBody("image/*".toMediaTypeOrNull())
////                    MultipartBody.Part.createFormData("files[]", File(uri.path).name, requestFile)
//                }
//
//                // Note: You need to implement the actual repository call here.
//                // Based on your prompt, the backend takes specific fields.
//                // If your repository doesn't have updateProduct, you need to add it similar to createProduct
//
//                /*
//                   Example Repository Call (You need to add this to ProfileRepository/ProductRepository):
//                   val response = productRepository.updateProduct(
//                       productId = productIdPart,
//                       name = namePart,
//                       // ... other parts
//                       files = imageParts
//                   )
//                */
//
//                // Mocking success for the UI flow since Repository code wasn't fully provided for Update
//                // _updateProductState.value = ResultResponse.Success(ApiUpdateResponse(...))
//
//            } catch (e: Exception) {
//                _updateProductState.value = ResultResponse.Error(e.message ?: "Unknown Error")
//            }
//        }
//    }

    // Helper for Multipart
    private fun createPartFromString(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
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
        val payload = buildPayloadForCreate() ?: return
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

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            try {
                productRepository.deleteProduct(
                    deleteProductRequest = DeleteProductRequest(
                        productId = productId
                    )
                ).collect { result ->
                    _productState.value = result
                }
            } catch (e: Exception) {
                _productState.value = ResultResponse.Error("Delete product failed: ${e.message}")
            }
        }
    }

    private fun normalizeServerPrice(raw: String): String =
        runCatching { raw.toBigDecimal().stripTrailingZeros().toPlainString() }
            .getOrDefault(raw)

    private fun buildPayloadForCreate(): CreateProductPayload? {
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

    private fun buildPayload(requireNewImages: Boolean = true): CreateProductPayload? {
        val stock = stockValue.toIntOrNull()
        val point = pointValue.toIntOrNull()
        val price = priceValue.toLongOrNull()
        val hasAnyImage = productImageFiles.isNotEmpty() || existingImageUrls.isNotEmpty()

        return when {
            nameValue.isBlank() -> {
                errorMessage.value = "Name is required"
                Log.e("ProductViewModel", "buildPayload: Name is required")
                null
            }
            stock == null || stock <= 0 -> {
                errorMessage.value = "Stock must be a positive number"
                Log.e("ProductViewModel", "buildPayload: Stock must be a positive number")
                null
            }
            descriptionValue.isBlank() -> {
                errorMessage.value = "Description is required"
                Log.e("ProductViewModel", "buildPayload: Description is required")
                null
            }
            arrangeTimeValue.isBlank() -> {
                errorMessage.value = "Arrange time is required"
                Log.e("ProductViewModel", "buildPayload: Arrange time is required")
                null
            }
            point == null || point < 0 -> {
                errorMessage.value = "Point must be zero or greater"
                Log.e("ProductViewModel", "buildPayload: Point must be zero or greater")
                null
            }
            price == null || price <= 0 -> {
                errorMessage.value = "Price must be a positive number"
                Log.e("ProductViewModel", "buildPayload: Price must be a positive number")
                null
            }
            categoryIdValue.isBlank() -> {
                errorMessage.value = "Category is required"
                Log.e("ProductViewModel", "buildPayload: Category is required")
                null
            }
            requireNewImages && productImageFiles.isEmpty() -> {
                errorMessage.value = "Please add at least one product image"
                Log.e("ProductViewModel", "buildPayload: Please add at least one product image")
                null
            }
            !requireNewImages && !hasAnyImage -> {
                errorMessage.value = "Please keep at least one product image"
                Log.e("ProductViewModel", "buildPayload: Please keep at least one product image")
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
        preloadExistingImagesJob?.cancel()
        clearExistingCachedImages()
        existingImageUrls.clear()
        productImageFiles.forEach { it.delete() }
        productImageFiles.clear()
        _productImages.clear()
        currentEditingProductId = ""
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
    }

    private suspend fun downloadImageToCache(url: String): File? = withContext(Dispatchers.IO) {
        runCatching {
            val request = Request.Builder().url(url).build()
            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext null
                val body = response.body ?: return@withContext null
                val tempFile = File.createTempFile(
                    "existing_product_${System.currentTimeMillis()}",
                    ".jpeg",
                    Resource.appContext.cacheDir
                )
                body.byteStream().use { input ->
                    tempFile.outputStream().use { output -> input.copyTo(output) }
                }
                tempFile
            }
        }.getOrNull()
    }

    private fun clearExistingCachedImages() {
        cachedExistingImageFiles.values.forEach { if (it.exists()) it.delete() }
        cachedExistingImageFiles.clear()
    }
}