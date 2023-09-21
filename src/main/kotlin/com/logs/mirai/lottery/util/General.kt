package com.logs.mirai.lottery.util

import com.logs.mirai.lottery.LotteryBot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

internal enum class CacheType {
    IMAGE
}

internal val cacheFolder: File by lazy {
    val folder = LotteryBot.dataFolder.resolve("cache").apply { mkdirs() }
    folder
}

internal val cacheFolderByType: (CacheType) -> File = {
    val folder = cacheFolder.resolve(it.name.lowercase()).apply { mkdirs() }
    folder
}

internal suspend fun File.getByUrl(url: String) = withContext(Dispatchers.IO) {
    URL(url).openStream().use { input ->
        BufferedOutputStream(FileOutputStream(this@getByUrl)).use { output ->
            input.copyTo(output)
        }
    }
}
