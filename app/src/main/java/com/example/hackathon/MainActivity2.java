package com.example.hackathon;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity2 extends AppCompatActivity {

    BottomNavigationView bottomNav;
    BottomNavigationView bottomNavigationView1;
    Bitmap insertImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNavigationView1=findViewById(R.id.bottom_navigation1);
        Intent intent=getIntent();
        String filename=intent.getStringExtra("filename");
        View view=new ImageGame(this,filename);
        final com.example.hackathon.ImageGame y=(com.example.hackathon.ImageGame)findViewById(R.id.image_game);
        y.setName(filename);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.crop:
                        if(y.isCrop()){
                            y.setCrop(false);
                            y.cropimage();
                        }else{
                            y.setCrop(true);
                        }
                        break;
                    case R.id.imageinsert:


                        if(y.isInsertimage()){
                            y.setInsertimage(false);
                        }else{
                            y.setInsertimage(true);
                            showChooser(MainActivity2.this);
                            if(insertImage!=null){
                                y.AskImageInsert(insertImage);
                            }
                        }

                        break;
                    case R.id.textinsert:

                        if(y.isInserttext()){
                            y.setInserttext(false);
                        }else{
                            y.setInserttext(true);
                            y.DrawTextView();
                        }
                        break;
                    case R.id.doodle:
                        if(y.isDoodle()){
                            y.setDoodle(false);
                        }else{
                            y.setDoodle(true);
                        }
                        break;
                    case R.id.color_pick:
                        if(y.isColorpick()){
                            y.setColorpick(false);
                        }else{
                            y.setColorpick(true);
                        }
                        break;
                    case R.id.color_replace:
                        if(y.isColorreplace()){
                            y.setColorreplace(false);
                        }else{
                            y.setColorreplace(true);
                        }
                        break;
                    case R.id.blur:
                        if(y.isBlur()){
                            y.setBlur(false);
                        }else{
                            y.setBlur(true);
                        }
                        break;
                    case R.id.rotate:
                        if(y.isRotate()){
                            y.setRotate(false);
                        }else{
                            y.setRotate(true);
                            y.rotate();
                        }
                        break;

                    case R.id.filter:
                        if(y.isFilter()){
                            y.setFilter(false);
                        }else{
                            y.setFilter(true);
                            y.Filter();
                        }

                    case R.id.save:
                        try {
                            y.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                }


                return true;
            }
        });

        bottomNavigationView1.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.color_replace:
                        if(y.isColorreplace()){
                            y.setColorreplace(false);
                        }else{
                            y.setColorreplace(true);
                        }
                        break;
                    case R.id.blur:
                        if(y.isBlur()){
                            y.setBlur(false);
                        }else{
                            y.setBlur(true);
                        }
                        break;
                    case R.id.rotate:
                        if(y.isRotate()){
                            y.setRotate(false);
                        }else{
                            y.setRotate(true);
                            y.rotate();
                        }
                        break;

                    case R.id.filter:
                        if(y.isFilter()){
                            y.setFilter(false);
                        }else{
                            y.setFilter(true);
                            y.Filter();
                        }

                    case R.id.save:
                        try {
                            y.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return true;

            }
        });


    }

    public void showChooser(Context context) {
        Intent target = new Intent(Intent.ACTION_GET_CONTENT);
        target.setType("image/*");
        startActivityForResult(target, 100);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK) {
            if (data != null) {
                final Uri uri = data.getData();

                try {
                    final String filepath = uri.getPath();
                   File file = new File(filepath);

                    Thread thread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            InputStream inputStream = null;
                            try {
                                inputStream = getContentResolver().openInputStream(uri);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            try {
                                saveFile(getBytes(inputStream),".jpg");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();










                } catch (Exception e) {
                    Toast.makeText(MainActivity2.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
    public void saveFile(byte[] decodedString,String extension) {
        String path = "Last Shared File";
        String name = "/" + "Transact"+extension;
        File root = new File(getExternalFilesDir(path).getAbsolutePath() + name);
        try {
            OutputStream fileOutputStream = new FileOutputStream(root);
            fileOutputStream.write(decodedString);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        insertImage= BitmapFactory.decodeFile(root.getAbsolutePath());
      /*  RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),root);

        multipartBody = MultipartBody.Part.createFormData("file",file.getName(),requestFile);*/

    }
}