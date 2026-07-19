package com.activemap.platform

import java.io.File

class DesktopFileExporter : FileExporter {

    private val reportsDir: File by lazy {
        val home = System.getProperty("user.home") ?: "."
        val dir = File(home, "ActiveMap_Reports")
        if (!dir.exists()) dir.mkdirs()
        dir
    }

    override suspend fun exportMarkdown(content: String, filename: String): String {
        val file = File(reportsDir, filename)
        file.writeText(content)
        return file.absolutePath
    }

    override suspend fun readMarkdown(filename: String): String? {
        val file = File(reportsDir, filename)
        return if (file.exists()) file.readText() else null
    }
}
