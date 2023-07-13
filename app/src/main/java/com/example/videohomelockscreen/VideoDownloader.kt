package com.example.videohomelockscreen

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL


object VideoDownloader {
    private const val DIRECTORY_NAME = "Videos"

    fun downloadFile(context: Context, fileUrl: String, fileName: String): Boolean {
        return try {
            val url = URL(fileUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            // Kiểm tra xem kết nối có thành công không
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                return false
            }

            // Tạo một InputStream để đọc dữ liệu từ URL
            val inputStream = connection.inputStream

            // Tạo một FileOutputStream để ghi dữ liệu vào thư mục cache
            val cacheDir = context.cacheDir
            val outputFile = File(cacheDir, fileName)
            val outputStream = FileOutputStream(outputFile)

            // Đọc dữ liệu từ InputStream và ghi vào FileOutputStream
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            // Đóng các luồng
            outputStream.close()
            inputStream.close()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    private fun getVideosDirectory(context: Context): File? {
        val externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
        return if (externalFilesDir != null) {
            File(externalFilesDir, DIRECTORY_NAME)
        } else null
    }

    private val isExternalStorageAvailable: Boolean
        private get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state
        }
}
