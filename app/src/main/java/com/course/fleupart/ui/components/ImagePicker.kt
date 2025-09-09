package com.course.fleupart.ui.components

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil3.compose.rememberAsyncImagePainter
import com.course.fleupart.R
import com.course.fleupart.ui.common.cleanupTempFiles
import com.course.fleupart.ui.common.startCropping
import com.course.fleupart.ui.theme.base300
import com.course.fleupart.ui.theme.primaryLight
import com.yalantis.ucrop.UCrop
import java.io.File
import kotlin.text.compareTo
import kotlin.text.get

@Composable
fun ImagePickerCard(
    label: String = "Citizen Card Picture",
    imageUri: Uri? = null,
    onImagePicked: (Uri) -> Unit
) {
    val context = LocalContext.current

    var permissionGranted by remember { mutableStateOf(false) }
    var showPreviewDialog by remember { mutableStateOf(false) }
    var showPickerDialog by remember { mutableStateOf(false) }
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) {
        permissionGranted = checkPermissions(context)
    }

    DisposableEffect(Unit) {
        onDispose {
            cleanupTempFiles(context)
        }
    }

    // Create temporary file for camera capture
    val createTempImageUri = {
        val tempFile = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            tempFile
        )
    }

    val cropLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    val resultUri = UCrop.getOutput(result.data!!)
                    resultUri?.let { onImagePicked(it) }
                } else {
                    // Cleanup jika crop dibatalkan
                    cleanupTempFiles(context)
                }
            } catch (e: Exception) {
                cleanupTempFiles(context)
            }
        }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        try {
            if (success) {
                tempImageUri?.let { uri ->
                    startCropping(context, uri, cropLauncher)
                }
            } else {
                // Cleanup jika camera capture gagal
                cleanupTempFiles(context)
            }
        } catch (e: Exception) {
            cleanupTempFiles(context)
        } finally {
            showPickerDialog = false
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        try {
            uri?.let {
                startCropping(context, it, cropLauncher)
            }
        } catch (e: Exception) {
            cleanupTempFiles(context)
        } finally {
            showPickerDialog = false
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] == true
        val storageGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions[Manifest.permission.READ_MEDIA_IMAGES] == true
        } else {
            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
        }

        permissionGranted = cameraGranted && storageGranted
        if (permissionGranted) {
            showPickerDialog = true
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, base300, RoundedCornerShape(10.dp))
                .clickable {
                    if (imageUri != null) {
                        showPreviewDialog = true
                    } else {
                        if (permissionGranted) {
                            showPickerDialog = true
                        } else {
                            permissionLauncher.launch(getRequiredPermissions())
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Selected Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.empty_image),
                        contentDescription = "Add Image",
                        tint = Color(0xFF292D32),
                    )
                    Text("Add image", color = Color.Black)
                }
            }
        }

        // Image Picker Dialog
        if (showPickerDialog) {
            ImagePickerDialog(
                onDismiss = { showPickerDialog = false },
                onCameraClick = {
                    tempImageUri = createTempImageUri()
                    cameraLauncher.launch(tempImageUri!!)
                },
                onGalleryClick = {
                    galleryLauncher.launch("image/*")
                }
            )
        }

        if (showPreviewDialog) {
            CustomImagePreviewDialog(
                imageUri = imageUri,
                onDismiss = { showPreviewDialog = false },
                onRetake = {
                    if (permissionGranted) {
                        showPickerDialog = true
                        showPreviewDialog = false
                    } else {
                        permissionLauncher.launch(getRequiredPermissions())
                    }
                }
            )
        }
    }
}

@Composable
fun ImagePickerDialog(
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Select Image Source",
                fontWeight = FontWeight.Bold,
                color = primaryLight
            )
        },
        text = {
            Text("Choose how you want to add an image")
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onCameraClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryLight
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.empty_image), // You'll need this icon
                            contentDescription = "Camera",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Camera")
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onGalleryClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryLight
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.google_ic), // You'll need this icon
                            contentDescription = "Gallery",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Gallery")
                    }
                }
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                )
            ) {
                Text("Cancel")
            }
        }
    )
}

private fun getRequiredPermissions(): Array<String> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_MEDIA_IMAGES
        )
    } else {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
}

fun checkPermissions(context: Context): Boolean {
    val cameraPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    return cameraPermission && storagePermission
}


@Composable
fun ImagePickerList(
    label: String = "Citizen Card Picture",
    imageUri: List<Uri> = emptyList(),
    onImagePicked: (Uri) -> Unit,
    onImageRemoved: (Uri) -> Unit = {} // Tambahan parameter untuk menghapus gambar
) {
    val context = LocalContext.current

    var permissionGranted by remember { mutableStateOf(false) }
    var showPreviewDialog by remember { mutableStateOf(false) }
    var showPickerDialog by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) {
        permissionGranted = checkPermissions(context)
    }

    // Cleanup temporary files saat composable dispose
    DisposableEffect(Unit) {
        onDispose {
            cleanupTempFiles(context)
        }
    }

    // Create temporary file for camera capture
    val createTempImageUri = {
        val tempFile = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            tempFile
        )
    }

    val cropLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    val resultUri = UCrop.getOutput(result.data!!)
                    resultUri?.let { onImagePicked(it) }
                } else {
                    cleanupTempFiles(context)
                }
            } catch (e: Exception) {
                cleanupTempFiles(context)
            }
        }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        try {
            if (success) {
                tempImageUri?.let { uri ->
                    startCropping(context, uri, cropLauncher)
                }
            } else {
                cleanupTempFiles(context)
            }
        } catch (e: Exception) {
            cleanupTempFiles(context)
        } finally {
            showPickerDialog = false
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        try {
            uri?.let {
                startCropping(context, it, cropLauncher)
            }
        } catch (e: Exception) {
            cleanupTempFiles(context)
        } finally {
            showPickerDialog = false
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] == true
        val storageGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions[Manifest.permission.READ_MEDIA_IMAGES] == true
        } else {
            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
        }

        permissionGranted = cameraGranted && storageGranted
        if (permissionGranted) {
            showPickerDialog = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = label,
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.W600,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (imageUri.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, base300, RoundedCornerShape(10.dp))
                        .clickable {
                            if (permissionGranted) {
                                showPickerDialog = true
                            } else {
                                permissionLauncher.launch(getRequiredPermissions())
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(R.drawable.empty_image),
                            contentDescription = "Add Image",
                            tint = Color(0xFF292D32),
                        )
                        Text("Add image", color = Color.Black)
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LazyRow(
                        modifier = when {
                            imageUri.size >= 3 -> Modifier.weight(1f)
                            imageUri.size == 2 -> Modifier.width(210.dp)
                            else -> Modifier.width(110.dp)
                        },
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(imageUri) { image ->
                            Box(
                                modifier = Modifier
                                    .height(100.dp)
                                    .width(100.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        selectedImageUri = image
                                        showPreviewDialog = true
                                    },
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(image),
                                    contentDescription = "Selected Image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )

                                // Tombol delete di kanan atas
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .size(20.dp)
                                        .background(Color.Gray.copy(alpha = 0.5f), CircleShape)
                                        .clickable {
                                            onImageRemoved(image)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.close_icon), // Ganti dengan icon X yang Anda miliki
                                        contentDescription = "Remove Image",
                                        tint = Color.Black,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.add_icon),
                            tint = primaryLight,
                            contentDescription = "Add Image",
                            modifier = Modifier
                                .size(35.dp)
                                .clickable {
                                    if (permissionGranted) {
                                        showPickerDialog = true
                                    } else {
                                        permissionLauncher.launch(getRequiredPermissions())
                                    }
                                }
                        )
                    }
                }
            }
        }
    }

    // Image Picker Dialog
    if (showPickerDialog) {
        ImagePickerDialog(
            onDismiss = { showPickerDialog = false },
            onCameraClick = {
                tempImageUri = createTempImageUri()
                cameraLauncher.launch(tempImageUri!!)
            },
            onGalleryClick = {
                galleryLauncher.launch("image/*")
            }
        )
    }

    if (showPreviewDialog) {
        CustomImagePreviewDialog(
            imageUri = selectedImageUri,
            onDismiss = { showPreviewDialog = false },
            onRetake = {
                if (permissionGranted) {
                    showPickerDialog = true
                    showPreviewDialog = false
                } else {
                    permissionLauncher.launch(getRequiredPermissions())
                }
            }
        )
    }
}

//fun checkPermissions(context: Context): Boolean {
//    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//        ContextCompat.checkSelfPermission(
//            context,
//            Manifest.permission.READ_MEDIA_IMAGES
//        ) == PackageManager.PERMISSION_GRANTED
//    } else {
//        ContextCompat.checkSelfPermission(
//            context,
//            Manifest.permission.READ_EXTERNAL_STORAGE
//        ) == PackageManager.PERMISSION_GRANTED
//    }
//}

fun getUCropOptions(context: Context): UCrop.Options {
    return UCrop.Options().apply {
        setHideBottomControls(false)
        setShowCropFrame(true)
        setShowCropGrid(true)
        setFreeStyleCropEnabled(true)
        setStatusBarColor(context.getColor(R.color.black))
        setToolbarColor(context.getColor(R.color.ic_fleura_id_background))
        setToolbarWidgetColor(context.getColor(R.color.white))
        setActiveControlsWidgetColor(context.getColor(R.color.ic_fleura_id_background))
        setToolbarTitle("Crop Image")
    }
}

@Composable
fun CustomImagePreviewDialog(
    imageUri: Uri?,
    onDismiss: () -> Unit,
    onRetake: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onDismiss)
                .clip(RoundedCornerShape(12.dp))
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .padding(bottom = 60.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Preview Image",
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryLight
                        )
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = onRetake,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryLight
                        )
                    ) {
                        Text("Retake")
                    }
                }
            }
        }
    }
}
