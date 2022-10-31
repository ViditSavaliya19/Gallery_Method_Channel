package com.example.flutternativedemo

import android.app.PendingIntent.getActivity
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel


class MainActivity : FlutterActivity() {
    private val CHANNEL = "demo"

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL
        ).setMethodCallHandler { call, result ->

//            if (call.method == "getBatteryLevel") {
//                val batteryLevel = getBatteryLevel()
//
//                if (batteryLevel != -1) {
//                    result.success(batteryLevel)
//                } else {
//                    result.error("UNAVAILABLE", "Battery level not available.", null)
//                }
//            } else {
//                result.notImplemented()
//            }
            if (call.method == "GetPhotos") {
                var data = fetchGalleryImages(this);
                result.success(data)

            }

        }
    }



    fun fetchGalleryImages(context: Context): ArrayList<String> {
        val galleryImageUrls: ArrayList<String>
        val columns = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID
        ) //get all columns of type images
        val orderBy = MediaStore.Images.Media.DATE_TAKEN //order data by date
        val imagecursor: Cursor? = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            columns,
            null,
            null,
            "$orderBy DESC"
        )
        galleryImageUrls = ArrayList()
        imagecursor?.moveToFirst()
        while (!imagecursor!!.isAfterLast()) {
            val dataColumnIndex: Int =
                imagecursor.getColumnIndex(MediaStore.Images.Media.DATA) //get column index
            galleryImageUrls.add(imagecursor.getString(dataColumnIndex)) //get Image from column index
            imagecursor.moveToNext()
        }
        return galleryImageUrls
    }
    fun SearchStorage(context: Context): ArrayList<ImagesFolder?>? {
        folders.clear()
        var position = 0
        val uri: Uri
        val cursor: Cursor
        val column_index_data: Int
        val column_index_folder_name: Int
        var absolutePathOfImage: String? = null
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection =
            arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val orderBy = MediaStore.Images.Media.DATE_TAKEN
        cursor = context.contentResolver.query(uri, projection, null, null, "$orderBy DESC")!!
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        column_index_folder_name =
            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data)
            Log.e("Column", absolutePathOfImage)
            Log.e("Folder", cursor.getString(column_index_folder_name))
            for (i in 0 until folders.size()) {
                if (folders.get(i).getAllFolderName()
                        .equals(cursor.getString(column_index_folder_name))
                ) {
                    boolean_folder = true
                    position = i
                    break
                } else {
                    boolean_folder = false
                }
            }
            if (boolean_folder) {
                val al_path = ArrayList<String?>()
                al_path.addAll(folders.get(position).getAllImagePaths())
                al_path.add(absolutePathOfImage)
                folders.get(position).setAllImagePaths(al_path)
            } else {
                val al_path = ArrayList<String?>()
                al_path.add(absolutePathOfImage)
                val obj_model = ImagesFolder()
                obj_model.setAllFolderName(cursor.getString(column_index_folder_name))
                obj_model.setAllImagePaths(al_path)
                folders.add(obj_model)
            }
        }
        for (i in 0 until folders.size()) {
            Log.d("FOLDER", folders.get(i).getAllFolderName())
            Log.d("FOLDER SIZE", java.lang.String.valueOf(folders.get(i).getAllImagePaths().size()))
            for (j in 0 until folders.get(i).getAllImagePaths().size()) {
                Log.d("FOLDER THUMBNAIL", folders.get(i).getAllImagePaths().get(j))
            }
        }
        //obj_adapter = new Adapter_PhotosFolder(getApplicationContext(),al_images);
        //gv_folder.setAdapter(obj_adapter);
        return folders
    }


}
