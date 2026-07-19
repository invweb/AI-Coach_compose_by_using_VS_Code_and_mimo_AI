package com.activemap.map

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image

actual suspend fun loadImageBitmap(bytes: ByteArray): ImageBitmap {
    return Image.makeFromEncoded(bytes).toComposeImageBitmap()
}
