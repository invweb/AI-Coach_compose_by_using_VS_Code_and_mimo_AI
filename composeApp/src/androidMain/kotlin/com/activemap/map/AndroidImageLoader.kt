package com.activemap.map

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

actual suspend fun loadImageBitmap(bytes: ByteArray): ImageBitmap {
    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    return bitmap.asImageBitmap()
}
