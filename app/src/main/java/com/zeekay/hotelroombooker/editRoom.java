package com.zeekay.hotelroombooker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zeekay.hotelroombooker.models.room;

import java.io.IOException;
import java.util.ArrayList;

public class editRoom extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference rooms;
    private StorageReference mStorageRef;

    Uri uri1;
    Uri uri2;
    Uri uri3;

    CheckBox tv;
    CheckBox ac;
    CheckBox wifi;
    EditText beds;
    EditText price;
    EditText num;
    ArrayList<room> roomList;

    private int PICK_IMAGE1_REQUEST = 1;
    private int PICK_IMAGE2_REQUEST = 2;
    private int PICK_IMAGE3_REQUEST = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        db=FirebaseDatabase.getInstance();
        roomList=new ArrayList<>();
        rooms=db.getReference().child("rooms");

        rooms.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> childs=dataSnapshot.getChildren();
                roomList.clear();
                Log.d("Data","Data Fetched");
                for (DataSnapshot child:childs) {

                    room temp = child.getValue(room.class);
                    roomList.add(temp);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        beds=findViewById(R.id.beds);
        price=findViewById(R.id.price);
        num=findViewById(R.id.num);
        tv=findViewById(R.id.tv);
        ac=findViewById(R.id.ac);
        wifi=findViewById(R.id.wifi);

    }

    public void add(View v){
        boolean acc;
        boolean wifii;
        boolean tvv;


        acc=ac.isChecked();
        wifii=wifi.isChecked();
        tvv=tv.isChecked();
        room r= new room(acc,wifii,tvv,num.getText().toString(),beds.getText().toString(),price.getText().toString(),"url1","url2","url3");
        r.setBooked(false);

        uploadImage(uri1);
        uploadImage(uri2);
        uploadImage(uri3);

        //room( boolean tv, boolean wifi, boolean ac, String num,String beds, String price, String im1, String im2, String im3)

        roomList.add(r);

        db.getReference().child("rooms").setValue(roomList);
        //rooms.setValue(roomList);

    }

    public void uploadIm1(View v){

        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE1_REQUEST);

    }
    public void uploadIm2(View v){
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE2_REQUEST);

    }
    public void uploadIm3(View v){
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE3_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE1_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri1 = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri1);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.image1);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE2_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri2 = data.getData();


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri2);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.image2);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE3_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri3 = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri3);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.image3);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void uploadImage(Uri file){
        //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        StorageReference riversRef = mStorageRef.child("images/rivers.jpg");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
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
    protected void onStop() {
        super.onStop();
        //finish();
    }
}
