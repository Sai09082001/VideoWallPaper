package com.example.videohomelockscreen

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var videoView: VideoView
                private lateinit var playerView : PlayerView
                private var player : SimpleExoPlayer ?= null
    private val REQUEST_CODE_PERMISSIONS = 101
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       playerView = findViewById(R.id.player_view)
//        val videoView = findViewById<VideoView>(R.id.videoView)
//        val videoPath = "android.resource://" + packageName + "/" + R.raw.my_video_test
//        val videoUri = Uri.parse(videoPath)
//        videoView.setVideoURI(videoUri)
//        videoView.start()
       //     startService(Intent(this, LockScreenService::class.java))
        val tvTest = findViewById<TextView>(R.id.tv_test)
        tvTest.setOnClickListener {
            loadMedia(this)
        }

        val tvDown = findViewById<TextView>(R.id.tv_down)
        tvDown.setOnClickListener {
           isAllowPermission()
          //  Toast.makeText(this, "Ok$"+downloadFile, Toast.LENGTH_SHORT).show()

        }

    }

    private fun isAllowPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permission  = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission,REQUEST_CODE_PERMISSIONS)
                } else{
                    startDownLoadFile()
            }
        } else {
            startDownLoadFile()
        }
    }

    private fun handleCacheFile () {

    }
    @SuppressLint("SuspiciousIndentation")
    private fun startDownLoadFile() {
            val downloadUrl = "http://vapp-expert.com/transparent/Video/5.mp4"
        val fileName = "video1.mp4"
        val cacheDir = cacheDir // Lấy thư mục cache của ứng dụng
        val file = File(cacheDir, fileName) // Tạo đối tượng File để lưu tệp tin trong cache

            if (file.exists()) {
                Toast.makeText(this,"Đã có",Toast.LENGTH_SHORT).show()
                // Tệp tin đã tồn tại trong cache
                if (player == null) {
                    player = SimpleExoPlayer.Builder(this).build();
                    playerView.setPlayer(player);
                }

                val mediaItem = MediaItem.fromUri(Uri.fromFile(file))
                player!!.setMediaItem(mediaItem);
                player!!.prepare();
                player!!.playWhenReady = false;
                // TODO: Thực hiện các thao tác sử dụng tệp tin từ cache ở đây
            } else {
                Toast.makeText(this,"Chua có",Toast.LENGTH_SHORT).show()
                // Tệp tin chưa tồn tại trong cache, tiến hành tải xuống và lưu vào cache
                val request = DownloadManager.Request(Uri.parse(downloadUrl))
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                request.setTitle("Download")
                request.setDescription("Download file....")
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationInExternalPublicDir( Environment.DIRECTORY_DOWNLOADS, fileName)

                val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadManager.enqueue(request)
            }
//
//        val request = DownloadManager.Request (Uri.parse("http://vapp-expert.com/transparent/Video/6.mp4"))
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
//        request.setTitle("Download")
//        request.setDescription("Download file....")
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, ""+ System.currentTimeMillis())
//        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//        downloadManager.enqueue(request)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
           if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
               startDownLoadFile()
           } else {
               Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
           }
       }

    }
    fun downLoadFile() {
        GlobalScope.launch(Dispatchers.IO) {
            mSaveMediaToStorage()
        }

    }

    private fun mSaveMediaToStorage() {

        val filename = "http://vapp-expert.com/transparent/Video/6.mp4"


        // save root
        try {
            val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).path
            val imagesDir = File("$root/VideoHomeLockScreen")
            if (!imagesDir.exists()) {
                imagesDir.mkdir()
            }
            val image = File(imagesDir, filename)
            if (image.exists()) {
                return
            }
            val fos: OutputStream = FileOutputStream(image)
            fos.use {
                fos.flush()
                fos.close()
            }

        }catch (e:Exception){

        }



    }
    fun loadMedia(context: Activity){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT);
        val mime_type = arrayOf(
            "image/png",
            "image/jpg",
            "image/gif",
            "video/mp4",
            "video/mpeg",
            "image/jpeg",
            "video/webm"
        )
        intent.setType("*/*");

        intent.putExtra(Intent.EXTRA_MIME_TYPES,mime_type);
        val i = Intent.createChooser(intent,"Select a wallpaper");
        context.startActivityForResult(i,8777);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 8777 && resultCode == RESULT_OK ){


            val wallpaperpath =data!!.data!!


            this.contentResolver.takePersistableUriPermission(wallpaperpath,Intent.FLAG_GRANT_READ_URI_PERMISSION)

            val conUri = Uri.parse(data!!.data!!.toString());

            val conres = this.contentResolver;

            Log.d("outInfo","reqcode : $requestCode  res: $resultCode  data: ${data!!.data!!.path} type: ${conres.getType(conUri)!!.split('/')[1]} ");

            var wallpapername = conUri.lastPathSegment!!;//mime firs

            var type = UrlType.Image;//defualt

            //set wallpaper type
            when(conres.getType(conUri)!!.split('/')[0].lowercase()){
                "image" ->{
                    if(conres.getType(conUri)!!.lowercase().contains("gif")){
                        type = UrlType.Gif
                    }
                }
                "video" ->{
                    type = UrlType.Video;
                }
            }

            data.data?.let { uri ->
                conres.query(conUri,null,null,null,null)
            }?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                cursor.moveToFirst();
                wallpapername = cursor.getString(nameIndex)
            }



            val Imageinfo = Image_Info(wallpaperpath.toString(),wallpaperpath.toString(),wallpapername,"unknown",wallpapername,"",Image_Ratio(1,1),type);


            val intent = Intent(this, Image_Activity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


            Image_Activity.MYDATA = Imageinfo;
            Image_Activity.THUMBNAIL = null;
            Image_Activity.save_local_external = false;
            Image_Activity.postmode = Image_Activity.mode.reddit;
            Image_Activity.loadedPreview = false;
            startActivity(intent);

        }
    }

}