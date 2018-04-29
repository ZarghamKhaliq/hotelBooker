package com.zeekay.hotelroombooker;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zeekay.hotelroombooker.models.user;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {
    CircleImageView dp;

    FirebaseDatabase db;
    DatabaseReference CurrentUser;
    user current=null;
    private FirebaseAuth mAuth;

    TextView name;
    TextView email;
    private StorageReference mStorageRef;
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        dp = findViewById(R.id.profile);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);


        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        db=FirebaseDatabase.getInstance();
        CurrentUser=db.getReference().child("users");
        final FirebaseUser u= mAuth.getCurrentUser();
        try {
            setPicture(u.getUid());
        } catch (IOException e) {
            e.printStackTrace();
        }


        CurrentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> childs=dataSnapshot.getChildren();

                //Log.d("User","Data Fetched+");
                for (DataSnapshot child:childs) {


                    user temp = child.getValue(user.class);

                    if(temp.getEmail().equals(u.getEmail())) {
                        Log.d("LoginU", temp.getFirstname());
                       current=temp;
                        break;
                    }

                }
                name.setText(current.getFirstname()+" "+current.getLastname());
                email.setText(current.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void setPicture(String uid) throws IOException {
        StorageReference riversRef = mStorageRef.child("images/"+uid+".jpg");

        final File localFile = File.createTempFile("images", "jpg");
        riversRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        // ...
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getPath());
                        dp.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });

    }
    public void updatePic(View v){
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


    }
    public void uploadImage(Uri file, final String uid){
        //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        StorageReference riversRef = mStorageRef.child("images/"+uid+".jpg");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        db.getReference().child("users").child(uid).child("dp").setValue(downloadUrl.toString());
                        Log.d("upload","Success"+downloadUrl);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Log.e("upload","failed",exception);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                dp.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


            uploadImage(uri,mAuth.getCurrentUser().getUid());
        }

    }

}
