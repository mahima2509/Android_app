package com.sunbeam.camera_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class mainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION_CODE = 1;
    private static final int REQUEST_IMAGECAPTURE=2;

    ImageView image_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        image_view = findViewById(R.id.image_view);


    }


    public void captureImage(View view){
        // Check if camera permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {
            //Request camera permission if not granted
           ActivityCompat.requestPermissions(this,
                   new String[]{Manifest.permission.CAMERA},
                   REQUEST_CAMERA_PERMISSION_CODE);
            return;
           }

        //open the camera

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_IMAGECAPTURE);
        }

        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_IMAGECAPTURE && resultCode == RESULT_OK) {
                //getting the capture image as bitmap

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                image_view.setImageBitmap(imageBitmap);

                saveImageToGallary(imageBitmap);
            }
        }
        private void saveImageToGallary(Bitmap imageBitmap) {
        File storage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            // Create new file name for saved image
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                    .format(new Date());
            String timestamp = "";
            String fileName = "IMG_" + timestamp +".jpg";

            File imageFile = new File(storage, fileName);

            try{

                // Write image bitmap to file
                FileOutputStream outputStream = new FileOutputStream(imageFile);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();

                // Add image to gallery
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(Uri.fromFile(imageFile));
                sendBroadcast(mediaScanIntent);

                Toast.makeText(this, "Image save Successfully", Toast.LENGTH_SHORT).show();

            } catch(Exception e) {

            }
        }




    }
