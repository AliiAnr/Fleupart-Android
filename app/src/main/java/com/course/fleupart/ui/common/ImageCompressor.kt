package com.course.fleupart.ui.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ImageCompressor {

    private const val MAX_FILE_SIZE = 1024 * 1024 // 1MB in bytes
    private const val MAX_WIDTH = 1080
    private const val MAX_HEIGHT = 1080
    private const val INITIAL_QUALITY = 90
    private const val MIN_QUALITY = 20
    private const val QUALITY_STEP = 10

    /**
     * Compresses image to be under 1MB while maintaining reasonable quality
     * @param context Android context
     * @param imageUri Uri of the image to compress
     * @param outputFile File where compressed image will be saved
     * @return Boolean indicating success/failure
     */
    fun compressImage(context: Context, imageUri: Uri, outputFile: File): Boolean {
        return try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            if (originalBitmap == null) return false

            // Fix orientation based on EXIF data
            val rotatedBitmap = fixImageOrientation(context, imageUri, originalBitmap)

            // Resize bitmap if too large
            val resizedBitmap = resizeBitmap(rotatedBitmap, MAX_WIDTH, MAX_HEIGHT)

            // Compress to meet file size requirement
            val compressedBytes = compressBitmapToSize(resizedBitmap, MAX_FILE_SIZE)

            // Save to output file
            val fileOutputStream = FileOutputStream(outputFile)
            fileOutputStream.write(compressedBytes)
            fileOutputStream.close()

            // Clean up bitmaps
            if (rotatedBitmap != originalBitmap) {
                rotatedBitmap.recycle()
            }
            originalBitmap.recycle()
            resizedBitmap.recycle()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Fixes image orientation based on EXIF data
     */
    private fun fixImageOrientation(context: Context, imageUri: Uri, bitmap: Bitmap): Bitmap {
        return try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val exif = ExifInterface(inputStream!!)
            inputStream.close()

            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
                else -> bitmap
            }
        } catch (e: Exception) {
            bitmap
        }
    }

    /**
     * Rotates bitmap by specified angle
     */
    private fun rotateBitmap(bitmap: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**
     * Resizes bitmap to fit within max width and height while maintaining aspect ratio
     */
    private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        if (width <= maxWidth && height <= maxHeight) {
            return bitmap
        }

        val aspectRatio = width.toFloat() / height.toFloat()

        val (newWidth, newHeight) = if (aspectRatio > 1) {
            // Landscape
            val w = minOf(width, maxWidth)
            val h = (w / aspectRatio).toInt()
            w to h
        } else {
            // Portrait or square
            val h = minOf(height, maxHeight)
            val w = (h * aspectRatio).toInt()
            w to h
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    /**
     * Compresses bitmap to meet target file size
     */
    private fun compressBitmapToSize(bitmap: Bitmap, targetSizeBytes: Int): ByteArray {
        var quality = INITIAL_QUALITY
        var compressedBytes: ByteArray

        do {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            compressedBytes = outputStream.toByteArray()
            outputStream.close()

            if (compressedBytes.size <= targetSizeBytes || quality <= MIN_QUALITY) {
                break
            }

            quality -= QUALITY_STEP
        } while (quality > MIN_QUALITY)

        return compressedBytes
    }

    /**
     * Gets file size in bytes
     */

    fun getFileSize(file: File): Long {
        return if (file.exists()) file.length() else 0
    }

    /**
     * Formats file size to human readable format
     */
    fun formatFileSize(sizeBytes: Long): String {
        return when {
            sizeBytes < 1024 -> "$sizeBytes B"
            sizeBytes < 1024 * 1024 -> "${sizeBytes / 1024} KB"
            else -> "${sizeBytes / (1024 * 1024)} MB"
        }
    }
}
