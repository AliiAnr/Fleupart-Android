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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil3.compose.rememberAsyncImagePainter
import com.course.fleupart.R
import com.course.fleupart.ui.theme.base300
import com.course.fleupart.ui.theme.primaryLight
import com.yalantis.ucrop.UCrop
import java.io.File

@Composable
fun ImagePickerCard(
    label: String = "Citizen Card Picture",
    imageUri: Uri? = null,
    onImagePicked: (Uri) -> Unit
) {
    val context = LocalContext.current

    var permissionGranted by remember { mutableStateOf(false) }
    var showPreviewDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        permissionGranted = checkPermissions(context)
    }

    val cropLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val resultUri = UCrop.getOutput(result.data!!)
                resultUri?.let { onImagePicked(it) }
            }
        }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val destinationUri = Uri.fromFile(
                File(
                    context.cacheDir,
                    "cropped_image_${System.currentTimeMillis()}.jpg"
                )
            )
            val uCrop = UCrop.of(it, destinationUri)
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(1000, 1000)
                .withOptions(getUCropOptions(context))
            cropLauncher.launch(uCrop.getIntent(context))
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissionGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions[Manifest.permission.READ_MEDIA_IMAGES] == true
        } else {
            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
        }
        if (permissionGranted) {
            launcher.launch("image/*")
            showPreviewDialog = false
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
                            launcher.launch("image/*")
                        } else {
                            permissionLauncher.launch(
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                                } else {
                                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                                }
                            )
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

        if (showPreviewDialog) {
            CustomImagePreviewDialog(
                imageUri = imageUri,
                onDismiss = { showPreviewDialog = false },
                onRetake = {
                    if (permissionGranted) {
                        launcher.launch("image/*")
                        showPreviewDialog = false
                    } else {
                        permissionLauncher.launch(
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                            } else {
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                        )
                    }
                }
            )
        }
    }
}


@Composable
fun ImagePickerList(
    label: String = "Citizen Card Picture",
    imageUri: List<Uri> = emptyList(),
    onImagePicked: (Uri) -> Unit
) {
    val context = LocalContext.current

    var permissionGranted by remember { mutableStateOf(false) }
    var showPreviewDialog by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) {
        permissionGranted = checkPermissions(context)
    }

    val cropLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val resultUri = UCrop.getOutput(result.data!!)
                resultUri?.let { onImagePicked(it) }
            }
        }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val destinationUri = Uri.fromFile(
                File(
                    context.cacheDir,
                    "cropped_image_${System.currentTimeMillis()}.jpg"
                )
            )
            val uCrop = UCrop.of(it, destinationUri)
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(1000, 1000)
                .withOptions(getUCropOptions(context))
            cropLauncher.launch(uCrop.getIntent(context))
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissionGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions[Manifest.permission.READ_MEDIA_IMAGES] == true
        } else {
            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
        }
        if (permissionGranted) {
            launcher.launch("image/*")
            showPreviewDialog = false
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
                            if (imageUri.isNotEmpty()) {
                                showPreviewDialog = true
                            } else {
                                if (permissionGranted) {
                                    launcher.launch("image/*")
                                } else {
                                    permissionLauncher.launch(
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                                        } else {
                                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                                        }
                                    )
                                }
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
                            imageUri.size >= 3 -> Modifier
                                .weight(1f)

                            imageUri.size == 2 -> Modifier
                                .width(210.dp)

                            else -> Modifier
                                .width(110.dp)
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
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(image),
                                    contentDescription = "Selected Image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
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
                                .clickable(
                                    onClick = {
                                        if (permissionGranted) {
                                            launcher.launch("image/*")
                                        } else {
                                            permissionLauncher.launch(
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                                                } else {
                                                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                                                }
                                            )
                                        }
                                    }
                                )
                        )
                    }
                }
            }
        }
    }

    if (showPreviewDialog) {
        CustomImagePreviewDialog(
            imageUri = selectedImageUri,
            onDismiss = { showPreviewDialog = false },
            onRetake = {
                if (permissionGranted) {
                    launcher.launch("image/*")
                    showPreviewDialog = false
                } else {
                    permissionLauncher.launch(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                        } else {
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    )
                }
            }
        )
    }
}

fun checkPermissions(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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
}

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
