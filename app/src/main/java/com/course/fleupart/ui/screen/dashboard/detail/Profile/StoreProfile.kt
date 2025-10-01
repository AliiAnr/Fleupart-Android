package com.course.fleupart.ui.screen.dashboard.detail.Profile

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.course.fleupart.R
import com.course.fleupart.data.model.remote.StoreDetailData
import com.course.fleupart.ui.common.ResultResponse
import com.course.fleupart.ui.components.CustomButton
import com.course.fleupart.ui.components.CustomPopUpDialog
import com.course.fleupart.ui.components.CustomTextInput
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.components.FlexibleImagePicker
import com.course.fleupart.ui.screen.dashboard.profile.ProfileViewModel
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.base20
import com.course.fleupart.ui.theme.primaryLight


// Store Header Photo
//== Section 1
// Store Name
// Store Description
// Store Address
// Store Phone Number

//Section 2
// Title: Opening Hours
// Opening Day Monday - Friday
// Opening Hour 08.00 - 20.00


@Composable
fun StoreProfileDetail(
    modifier
    : Modifier = Modifier,
    onBackClick: () -> Unit,
    profileViewModel: ProfileViewModel,
) {

    var showCircularProgress by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var isShowSheet by remember { mutableStateOf(false) }

    var contentType by remember { mutableStateOf(ContentType.Logo) }
    var updateType by remember { mutableStateOf(UpdateType.StoreDetail) }

    val updateImageState by profileViewModel.updateImageState.collectAsStateWithLifecycle(
        initialValue = ResultResponse.None
    )

    val updateStoreDetailState by profileViewModel.updateStoreDetailState.collectAsStateWithLifecycle(
        initialValue = ResultResponse.None
    )

    LaunchedEffect(updateImageState) {
        when (updateImageState) {
            is ResultResponse.Success -> {
                showCircularProgress = false
                showSuccessDialog = true
            }

            is ResultResponse.Loading -> {
                showCircularProgress = true
            }

            is ResultResponse.Error -> {
                showCircularProgress = false
            }

            else -> {}
        }
    }

    LaunchedEffect(updateStoreDetailState) {
        when (updateStoreDetailState) {
            is ResultResponse.Success -> {
                showCircularProgress = false
                showSuccessDialog = true
            }

            is ResultResponse.Loading -> {
                showCircularProgress = true
            }

            is ResultResponse.Error -> {
                showCircularProgress = false
            }

            else -> {}
        }
    }

    val initialData = remember {
        profileViewModel.storeInformationValue.value?.let {
            StoreDetailData(
                id = it.id ?: "",
                name = it.name ?: "",
                description = it.description ?: "",
                phone = it.phone ?: "",
                logo = it.logo ?: "",
                picture = it.picture ?: "",
                operationalDay = it.operationalDay ?: "",
                operationalHour = it.operationalHour ?: ""
            )
        }
    }

    var name by remember { mutableStateOf(profileViewModel.storeInformationValue.value?.name ?: "") }
    var description by remember { mutableStateOf(profileViewModel.storeInformationValue.value?.description ?: "") }
    var phone by remember { mutableStateOf(profileViewModel.storeInformationValue.value?.phone ?: "") }
    var openingDay by remember { mutableStateOf(profileViewModel.storeInformationValue.value?.operationalDay ?: "") }
    var openingHour by remember { mutableStateOf(profileViewModel.storeInformationValue.value?.operationalHour ?: "") }

    val isChanged = remember(
        name,
        description,
        phone,
        openingDay,
        openingHour
    ) {
        name != (initialData?.name ?: "") ||
                description != (initialData?.description ?: "") ||
                phone != (initialData?.phone ?: "") ||
                openingDay != (initialData?.operationalDay ?: "") ||
                openingHour != (initialData?.operationalHour ?: "")
    }

    val isAllFieldsFilled =
        name.isNotBlank() &&
                description.isNotBlank() &&
                phone.isNotBlank() &&
                openingDay.isNotBlank() &&
                openingHour.isNotBlank()

    val isButtonAvailable = !showCircularProgress && isChanged && isAllFieldsFilled



    StoreProfileDetail(
        modifier = modifier,
        storeLogo = initialData?.logo ?: "",
        storeBanner = initialData?.picture ?: "",
        contentType = contentType,
        updateType = updateType,
        name = name,
        description = description,
        phone = phone,
        openingDay = openingDay,
        openingHour = openingHour,
        onNameChange = { name = it },
        onDescriptionChange = { description = it },
        onPhoneChange = { phone = it },
        onOpeningDayChange = { openingDay = it },
        onOpeningHourChange = { openingHour = it },
        onBackClick = onBackClick,
        showSuccessDialog = showSuccessDialog,
        isButtonAvailable = isButtonAvailable,
        profileViewModel = profileViewModel,
        isShowSheet = isShowSheet,
        showCircularProgress = showCircularProgress,
        onShowSheet = { isShowSheet = it },
        onContentTypeChange = { contentType = it },
        onUpdateTypeChange = { updateType = it },
        onDismiss = {
            showSuccessDialog = false
            profileViewModel.resetImageUpdateState()
            profileViewModel.resetUpdateStoreDetailState()
            onBackClick()
        },
        onSaveClick = {
            profileViewModel.updateStoreDetail(
                name = name,
                description = description,
                phone = phone,
                operationalDay = openingDay,
                operationalHour = openingHour
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StoreProfileDetail(
    modifier: Modifier = Modifier,
    storeLogo: String,
    storeBanner: String,
    contentType: ContentType,
    updateType: UpdateType,
    name: String,
    description: String,
    phone: String,
    openingDay: String,
    openingHour: String,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onOpeningDayChange: (String) -> Unit,
    onOpeningHourChange: (String) -> Unit,
    onBackClick: () -> Unit,
    showSuccessDialog: Boolean,
    isButtonAvailable: Boolean,
    profileViewModel: ProfileViewModel,
    showCircularProgress: Boolean = false,
    isShowSheet: Boolean,
    onShowSheet: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    onSaveClick: () -> Unit,
    onContentTypeChange: (ContentType) -> Unit,
    onUpdateTypeChange: (UpdateType) -> Unit,
    ) {


    val bottomSheetState = rememberModalBottomSheetState()

    var storeImageUrl by remember { mutableStateOf("https://udykufvgdmysypdcsnnp.supabase.co/storage/v1/object/public/Image/store/picture/26fcf508-6aa0-46b0-9f88-22be8309ff35/1744966830438.jpg") } // Testing URL
    var selectetBannerUri by remember { mutableStateOf<Uri?>(null) }
    var selectetLogoUri by remember { mutableStateOf<Uri?>(null) }


    FleupartSurface(
        modifier = modifier.fillMaxSize(),
    ) {
        val focusManager = LocalFocusManager.current
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    // Hapus fokus jika area lain diklik
                    indication = null, // Tidak ada animasi klik
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                },
            contentAlignment = Alignment.Center

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .background(base20),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomTopAppBar(
                    title = "Store Detail",
                    showNavigationIcon = true,
                    onBackClick = onBackClick
                )

                Box(
                    modifier = Modifier.weight(1f) // Membuat LazyColumn fleksibel
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .padding(top = 14.dp, bottom = 8.dp, start = 20.dp, end = 20.dp)
                            ) {

                                Text(
                                    text = "Logo",
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    FlexibleImagePicker(
                                        imageUrl = storeLogo,
                                        selectedImageUri = selectetLogoUri,
                                        size = 100.dp,
                                        shape = CircleShape,
                                        onImageClick = {
                                            onShowSheet(true)
                                            onContentTypeChange(ContentType.Logo)
                                        },
                                        showEditButton = false,
                                        onImagePicked = { uri ->
                                            selectetLogoUri = profileViewModel.setStoreLogo(uri)
                                        }
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                            ) {

                                Text(
                                    text = "Banner",
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(
                                        top = 14.dp,
                                        bottom = 8.dp,
                                        start = 20.dp,
                                        end = 20.dp
                                    )
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    FlexibleImagePicker(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 8.dp),
                                        shape = RectangleShape,
                                        imageUrl = storeBanner,
                                        selectedImageUri = selectetBannerUri,
                                        height = 250.dp,
                                        onImageClick = {
                                            onShowSheet(true)
                                            onContentTypeChange(ContentType.Banner)
                                        },
                                        showEditButton = false,
                                        onImagePicked = { uri ->
                                            selectetBannerUri = profileViewModel.setStoreBanner(uri)
                                        }
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            DetailContent(
                                name = name,
                                description = description,
                                phone = phone,
                                onNameChange = onNameChange,
                                onDescriptionChange = onDescriptionChange,
                                onPhoneChange = onPhoneChange
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            TimeContent(
                                openingDay = openingDay,
                                openingHour = openingHour,
                                onOpeningDayChange = onOpeningDayChange,
                                onOpeningHourChange = onOpeningHourChange
                            )
                        }

                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .height(90.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CustomButton(
                        text = "Save",
                        onClick = {
                            onUpdateTypeChange(UpdateType.StoreDetail)
                            onSaveClick()
                        },
                        isAvailable = isButtonAvailable
                    )
                }
            }

            if (isShowSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        // Reset selected image
                        if (contentType == ContentType.Logo) {
                            selectetLogoUri = null
                        } else {
                            selectetBannerUri = null
                        }
                        onShowSheet(false)
                    },
                    containerColor = Color.White,
                    sheetState = bottomSheetState,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BottomSheetContent(
                        imageUrl = if (contentType == ContentType.Logo) storeLogo else storeBanner,
                        selectedImageUri = if (contentType == ContentType.Logo) selectetLogoUri else selectetBannerUri,
                        onImagePicked = { uri ->
                            if (contentType == ContentType.Logo) {
                                selectetLogoUri = uri
                            } else {
                                selectetBannerUri = uri
                            }
                        },

                        contentType = contentType,
                        onSave = {
                            // TODO: Save to server here
                            // For now, just update the URL with selected image
//                            selectedImageUri?.let {
//                                // In real implementation, upload to server and get new URL
//                                storeImageUrl = it.toString() // This is temporary
//                            }
//                            isShowSheet = false
                            onShowSheet(false)
                            if (contentType == ContentType.Logo) {
                                onUpdateTypeChange(UpdateType.StoreLogo)
                                selectetLogoUri?.let {
                                    profileViewModel.uploadStoreLogo(it)
                                }
                            } else {
//                                onUpdateTypeChange(UpdateType.StoreBanner)
//                                selectetBannerUri?.let {
//                                    profileViewModel.uploadStoreBanner(it)
//                                }
                            }
                        },
                        onCancel = {
                            // Reset selected image
                            if(contentType == ContentType.Logo) {
                                selectetLogoUri = null
                            } else {
                                selectetBannerUri = null
                            }
                            onShowSheet(false)
                        }
                    )
                }
            }

            if (showCircularProgress) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            // Do nothing - this prevents clicks from passing through
                        }
                ) {
                    CircularProgressIndicator(color = primaryLight)
                }
            }

            if (showSuccessDialog) {
                CustomPopUpDialog(
                    onDismiss = onDismiss,
                    isShowIcon = true,
                    isShowTitle = true,
                    isShowDescription = true,
                    isShowButton = false,
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(80.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ceklist),
                                contentDescription = null,
                                tint = Color.Unspecified,
                            )
                        }
                    },
                    title = "Update ${updateType.displayName} Success",
                    description = "Click the X button to close this dialog.",
                )
            }
        }
    }
}

// Store Header Photo
//== Section 1
// Store Name
// Store Description
// Store Address
// Store Phone Number

@Composable
private fun DetailContent(
    modifier: Modifier = Modifier,
    name: String,
    description: String,
    phone: String,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 14.dp, bottom = 8.dp)
    ) {
        EditItem(
            label = "Name",
            value = name,
            onChage = onNameChange,
            borderColor = Color.Black,
            placeHolder = "e.g. Fleupart Store"
        )

        EditItem(
            label = "Short Description",
            value = description,
            onChage = onDescriptionChange,
            isLongText = true,
            placeHolder = "This is a flower made from ...",
            height = 100.dp,
            borderColor = Color.Black,
        )

        EditItem(
            label = "Phone Number",
            value = phone,
            onChage = onPhoneChange,
            borderColor = Color.Black,
            placeHolder = "e.g. 081234567890"
        )
    }
}

enum class ContentType(val displayName: String) {
    Logo("Store Logo"),
    Banner("Store Banner")
}

enum class UpdateType(val displayName: String) {
    StoreDetail("Store Detail"),
    StoreLogo("Store Logo"),
    StoreBanner("Store Banner")
}

@Composable
private fun BottomSheetContent(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    selectedImageUri: Uri? = null,
    onImagePicked: (Uri) -> Unit,
    contentType: ContentType = ContentType.Logo,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Edit ${contentType.displayName}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(20.dp))

        when (contentType) {
            ContentType.Logo ->
                FlexibleImagePicker(
                    imageUrl = imageUrl,
                    selectedImageUri = selectedImageUri,
                    size = 200.dp,
                    shape = CircleShape,
                    onImagePicked = onImagePicked
                )

            ContentType.Banner ->
                FlexibleImagePicker(
                    imageUrl = imageUrl,
                    modifier = Modifier.fillMaxWidth(),
                    selectedImageUri = selectedImageUri,
                    height = 200.dp,
                    onImagePicked = onImagePicked
                )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomButton(
                text = "Cancel",
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                isAvailable = true
            )

            CustomButton(
                text = "Save",
                onClick = onSave,
                modifier = Modifier.weight(1f),
                isAvailable = selectedImageUri != null
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun EditItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    height: Dp = 50.dp,
    leadingIcon: Int? = null,
    leadingText: String? = null,
    borderColor: Color,
    placeHolder: String = "",
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    errorMessage: String = "",
    isLongText: Boolean = false, // Tambahkan parameter ini
    onChage: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.W600,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        CustomTextInput(
            value = value,
            onChange = onChage,
            placeholder = placeHolder,
            isError = isError,
            errorMessage = errorMessage,
            leadingIcon = leadingIcon,
            leadingText = leadingText,
            borderColor = borderColor,
            keyboardType = keyboardType,
            height = height,
            isLongText = isLongText // Tambahkan parameter ini
        )
    }
}


@Composable
private fun TimeContent(
    modifier: Modifier = Modifier,
    openingDay: String,
    openingHour: String,
    onOpeningDayChange: (String) -> Unit,
    onOpeningHourChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 14.dp, bottom = 8.dp)
    ) {
        EditItem(
            label = "Opening Day",
            value = openingDay,
            onChage = onOpeningDayChange,
            borderColor = Color.Black,
            placeHolder = "e.g. Monday - Friday"
        )

        EditItem(
            label = "Opening Hour",
            value = openingHour,
            onChage = onOpeningHourChange,
            borderColor = Color.Black,
            placeHolder = "e.g. 08.00 - 20.00"
        )
    }
}


// Store Header Photo
//== Section 1
// Store Name
// Store Description
// Store Address
// Store Phone Number
