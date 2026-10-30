package com.nikz.app

import android.content.Context
import android.util.Log
import java.io.File

object NikzAssetManager {
    private const val TAG = "GameDataAssets"
    const val ASSET_FOLDER = "GameData"

    fun extractAll(context: Context): File {
        val outDir = File(context.filesDir, ASSET_FOLDER).apply {
            deleteRecursively()
            mkdirs()
        }
        val files = context.assets.list(ASSET_FOLDER) ?: emptyArray()
        Log.d(TAG, "Extracting ${files.size} files...")
        for (name in files) {
            val out = File(outDir, name)
            context.assets.open("$ASSET_FOLDER/$name").use { inp ->
                out.outputStream().use { inp.copyTo(it) }
            }
        }
        return outDir
    }

    fun chmodAll(dir: File): Boolean {
        return try {
            val proc = Runtime.getRuntime()
                .exec(arrayOf("su", "-c", "chmod -R 755 ${dir.absolutePath}"))
            proc.waitFor() == 0
        } catch (e: Exception) { false }
    }
}
