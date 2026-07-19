package com.activemap.platform

import android.content.Context
import java.io.File

class AndroidFileExporter(private val context: Context) : FileExporter {

    override suspend fun exportMarkdown(content: String, filename: String): String {
        val dir = File(context.getExternalFilesDir(null), "active_map_reports")
        if (!dir.exists()) dir.mkdirs()

        val file = File(dir, filename)
        file.writeText(content)
        return file.absolutePath
    }

    override suspend fun readMarkdown(filename: String): String? {
        val dir = File(context.getExternalFilesDir(null), "active_map_reports")
        val file = File(dir, filename)
        return if (file.exists()) file.readText() else null
    }
}
