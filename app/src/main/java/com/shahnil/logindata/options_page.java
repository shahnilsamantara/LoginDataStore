package com.shahnil.logindata;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;

public class options_page extends AppCompatActivity {



    private String imageName;
    private String Id;
    private static int PICK_IMAGE_REQUEST = 1;

    private Button mChooseButton;
    private Button mUploadButton;
    private EditText mEditTextFileName;
    private ImageView mimageview;
    private ProgressBar mprogressbar;

    private Uri mimageuri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_page);


        mChooseButton = findViewById(R.id.chooseImage);
        mUploadButton = findViewById(R.id.uploadButton);
        mEditTextFileName = findViewById(R.id.picturename);
        mprogressbar = findViewById(R.id.progressbar);
        mimageview = findViewById(R.id.image_view);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UserData");
        mStorageRef = FirebaseStorage.getInstance().getReference("UserData");




        mChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();

            }
        });


        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadfile();

            }
        });








    }


    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
            && data != null && data.getData() != null){
            mimageuri = data.getData();
            mimageview.setImageURI(mimageuri);
        }
    }

    private String getFileExtention(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadfile(){

        if(mimageuri != null){

            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                        + "." + getFileExtention(mimageuri));

            fileReference.putFile(mimageuri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mprogressbar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(options_page.this, "Upload Successful", Toast.LENGTH_LONG).show();

                            userinformation userinformation = new userinformation(mEditTextFileName.getText().toString().trim());
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadurl = uri;
                                }
                            });


                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.setValue(userinformation);




                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(options_page.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            mprogressbar.setProgress((int) progress);

                        }
                    });



        } else{
            Toast.makeText(this, "no file selected", Toast.LENGTH_SHORT).show();
        }

    }



}