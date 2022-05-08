package com.example.mc_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class CaptureActivity extends AppCompatActivity {

    public static final int CAMERA_PERM_CODE = 101;
    private static final int REQUEST_IMAGE_CAPTURE = 102,GALLERY_REQUEST=105 ;
    ImageView selectedImage;
    TextView tvUpload;
    ProgressBar pg;
    private String currentPhotoPath;
    int countImgs = -1;

    //private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");
    private StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mc-project-c7fbc.appspot.com");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        Button capture = findViewById(R.id.buttonCapture);
        Button upload = findViewById(R.id.buttonUpload);
        selectedImage =findViewById(R.id.imageViewCapture);
        tvUpload = findViewById(R.id.tvUpload);
        pg = findViewById(R.id.progressBar3);


        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Patients").child(user);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("image_count").exists()){
                    countImgs = dataSnapshot.child("image_count").getValue(Integer.class);
                }
                else{
                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Patients").child(user).child("image_count");
                    ref2.setValue(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
        Log.i("tagsuccess","userid " + user);


        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                askCameraPermission();
            }


        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery,GALLERY_REQUEST);
            }
        });

    }

    @Override
    protected void onStop() {
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Patients").child(user).child("image_count");
        ref.setValue(countImgs);
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //OPENca
//                dispatchTakePictureIntent();
                openCamera();
            } else {
//                Toast.makeText(this, "Camera permission denied!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void askCameraPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_PERM_CODE);
        }else{
//            dispatchTakePictureIntent();
                openCamera();
            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("OnActivityResult","Done");
        Log.i("requestCode", String.valueOf(requestCode));
        Log.i("resultCode", String.valueOf(resultCode));
        Log.i("data", String.valueOf(data));

        if(requestCode==REQUEST_IMAGE_CAPTURE  ){
            Log.i("insideeeee","hogya bhai capture");
//            Bitmap mImageUri = (Bitmap) data.getExtras().get("data");
//            selectedImage.setImageBitmap(mImageUri);
            File f = new File(currentPhotoPath);
            selectedImage.setImageURI(Uri.fromFile(f));
            Log.d("tag","absolute image"+Uri.fromFile(f));
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            File f = new File(currentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
            uploadImageToFirebase(f.getName(),contentUri);
        }
        //HANDLE GALLERY UPLOAD
        if(requestCode==GALLERY_REQUEST  ){
            Log.i("insideeeee","hogya bhai capture");
            Uri contentUri = data.getData();
            String timestamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
            String fileName = "JPEG_"+timestamp+"."+getFileExt(contentUri);
            Log.i("imageeeeee",fileName);
            selectedImage.setImageURI(contentUri);
            uploadImageToFirebase(fileName,contentUri);
        }

    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();

        Log.i("imageeeeee","CONTENTRESOLVEREEEE");

        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    private void uploadImageToFirebase(String name, Uri contentUri) {
        StorageReference imageToUpload = reference.child("images/"+name);

        imageToUpload.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageToUpload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.i("tagsuccess","SUCCESS TO UPLOAD " + uri);
                        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Log.i("tagsuccess","userid " + user);
                        ImageModel model = new ImageModel(String.valueOf(uri));

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Patients").child(user).child("reports");

                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    countImgs = (int) snapshot.getChildrenCount();
                                    Log.i("tagcount","count " + countImgs);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        HashMap<String,Object> images = new HashMap<>();
                        images.put("image_uri"+(countImgs), String.valueOf(uri));
                        ref.updateChildren(images).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.i("tagsuccess","done ");
                            }
                        });
                        tvUpload.setText("Your image has been upload!");


                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CaptureActivity.this,"Upload failed",Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * (taskSnapshot.getBytesTransferred() )/ taskSnapshot.getTotalByteCount());
                System.out.println("Upload is " + progress + "% done");
                int currentprogress = (int) progress;
                pg.setProgress(currentprogress);
            }
        });
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        Log.i("CreateImageFile","DOne");
//                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
//    private void dispatchTakePictureIntent() {
//        Log.i("innnsiee","final");
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            Log.i("iNOTNULL","Dvsdf");
//
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//                Log.i("inscscw","Dvsdf");
//
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Log.i("inscscw","Dvsdf");
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.android.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//            }
//        }
//    }

    private void openCamera() {
        Toast.makeText(this,"Open Camera",Toast.LENGTH_LONG).show();
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = createImageFile();
//            Log.i("inscscw","Dvsdf");

        } catch (IOException ex) {
            // Error occurred while creating the File

        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Log.i("photofile","Done");
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.example.android.fileprovider",
                    photoFile);
            Log.i("photokiloc", String.valueOf(photoURI));
            camera.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(camera, REQUEST_IMAGE_CAPTURE);
//            startActivity(camera);
        }
//        startActivityForResult(camera,1);


    }
}