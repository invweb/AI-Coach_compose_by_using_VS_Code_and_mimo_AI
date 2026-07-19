package com.activemap.map

import androidx.compose.ui.graphics.ImageBitmap

expect suspend fun loadImageBitmap(bytes: ByteArray): ImageBitmap

object OsmTileLoader {
    private val cache = mutableMapOf<String, ImageBitmap?>()

    private val tileServers = listOf(
        "https://basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}.png",
        "https://basemaps.cartocdn.com/rastertiles/light_all/{z}/{x}/{y}.png",
    )

    suspend fun loadTile(z: Int, x: Int, y: Int): ImageBitmap? {
        val key = "$z/$x/$y"
        cache[key]?.let { return it }

        for (template in tileServers) {
            val urlStr = template.replace("{z}", "$z").replace("{x}", "$x").replace("{y}", "$y")
            try {
                val url = java.net.URL(urlStr)
                val conn = url.openConnection() as java.net.HttpURLConnection
                conn.connectTimeout = 8000
                conn.readTimeout = 8000
                conn.setRequestProperty("User-Agent", "ActiveMap/1.0")
                conn.connect()

                val code = conn.responseCode
                if (code == 200) {
                    val bytes = conn.inputStream.readBytes()
                    conn.disconnect()
                    if (bytes.isNotEmpty()) {
                        val bitmap = loadImageBitmap(bytes)
                        cache[key] = bitmap
                        return bitmap
                    }
                }
                conn.disconnect()
            } catch (e: Exception) {
                continue
            }
        }
        cache[key] = null
        return null
    }

    fun clearCache() {
        cache.clear()
    }
}
