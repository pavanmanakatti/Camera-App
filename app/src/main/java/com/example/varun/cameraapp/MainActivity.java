package com.example.varun.cameraapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int ACTIVITY_START_CAMERA_APP = 0;
    private ImageView picCap;
    private String imgFileLoc = "";
    private static final int PICK_FROM_CAM = 1;
    private static final int PICK_FROM_FILE = 2;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        picCap = (ImageView) findViewById(R.id.capturePic);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void takePhoto(View view){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try{
            photoFile = createImageFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        startActivityForResult(intent, PICK_FROM_CAM);
    }

    public void pickFromGallery(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Complete Action Using"), PICK_FROM_FILE);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap bitmap = null;
        String path = "";

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_FROM_CAM) {
                Toast.makeText(this, "Picture captured Successfully", Toast.LENGTH_LONG).show();
                //Bundle extras = data.getExtras();
                //Bitmap picBitMap = (Bitmap) extras.get("data");
                //picCap.setImageBitmap(picBitMap);

                Bitmap photoCap = BitmapFactory.decodeFile(imgFileLoc);
                picCap.setImageBitmap(photoCap);
            }

            else{
                imageUri = data.getData();
                path = getPath(imageUri);
                picCap.setImageURI(imageUri);

                if (path == null) {
                    path = imageUri.getPath();
                    if (path != null)
                        Log.i("path of file..:", path);
                }
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    File createImageFile() throws IOException{
        /*String timeStamp = new SimpleDateFormat("yymmdd").format(new Date());
        String imageName = "IMAGE_"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageName,".jpg",storageDir);
        imgFileLoc = image.getAbsolutePath();*/

        String RootDir = Environment.getExternalStorageDirectory() + File.separator + "DigitalDiary";
        File RootFile = new File(RootDir);

        RootFile.mkdir();
        File image = new File(RootFile, "image.jpg");
        imgFileLoc = image.getAbsolutePath();

        return image;
    }
}
