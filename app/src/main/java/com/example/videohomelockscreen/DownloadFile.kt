package com.example.videohomelockscreen

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DownloadFile : ViewModel(){

    fun downloadFile(url : String,context: Context)  {
        GlobalScope.launch {
            val fileName = "video.mp4"

            withContext(Dispatchers.IO) {
                downloadVideo(url, fileName, context )
            }
        }

    }
    @SuppressLint("Range")
    private fun downloadVideo(url: String, fileName: String, context: Context) {
        var myDownloader : Long = 0

        val uri = Uri.parse(url)

        val request = DownloadManager.Request(uri).setTitle("test.mp4")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE).setAllowedOverMetered(true).setVisibleInDownloadsUi(false)
       // val id: Long = downloadManager.enqueue(request)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "fileName.mp4")
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
       myDownloader = downloadManager.enqueue(request)
       request.setDestinationInExternalFilesDir(context,"/file","Test1.mp4")
       // val query = DownloadManager.Query().setFilterById(downloadId)
//        var downloading = true
//
//        while (downloading) {
//            val cursor = downloadManager.query(query)
//            cursor.moveToFirst()
//
//            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
//
//            if (status == DownloadManager.STATUS_SUCCESSFUL) {
//                downloading = false
//                Log.i("KMFG", "downloadVideo: success ")
//                // Tải xuống thành công
//            } else if (status == DownloadManager.STATUS_FAILED) {
//                downloading = false
//                Log.i("KMFG", "downloadVideo: fail ")
//                // Tải xuống thất bại
//            }
//
//            cursor.close()
//        }
    }


}