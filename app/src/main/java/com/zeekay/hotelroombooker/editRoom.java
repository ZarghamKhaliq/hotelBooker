package com.zeekay.hotelroombooker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zeekay.hotelroombooker.models.room;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class editRoom extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference rooms;
    private StorageReference mStorageRef;
    ImageView im1;
    ImageView im2;
    ImageView im3;

    Uri uri1;
    Uri uri2;
    Uri uri3;

    int index;

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
        setContentView(R.layout.activity_edit_room);

        uri1=null;
        uri2=null;
        uri3=null;

        im1 = findViewById(R.id.image1);
        im2 = findViewById(R.id.image2);
        im3 = findViewById(R.id.image3);

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
                Intent i=getIntent();
                index=i.getIntExtra("index",-1);
                populateView(roomList.get(index));

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

    public void populateView(room r){
        Toast.makeText(this,r.getNum(),Toast.LENGTH_SHORT).show();
        tv.setChecked(r.isTv());
        ac.setChecked(r.isAc());
        wifi.setChecked(r.isWifi());
        beds.setText(r.getBeds());
        price.setText(r.getPrice());
        num.setText(r.getNum());
        try {
            setPicture(r.getNum(),"im1",im1);
            setPicture(r.getNum(),"im2",im2);
            setPicture(r.getNum(),"im3",im3);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void add(View v){
        boolean acc;
        boolean wifii;
        boolean tvv;


        acc=ac.isChecked();
        wifii=wifi.isChecked();
        tvv=tv.isChecked();



        roomList.get(index).setAc(acc);
        roomList.get(index).setTv(tvv);
        roomList.get(index).setWifi(wifii);
        roomList.get(index).setBeds(beds.getText().toString());
        roomList.get(index).setPrice(price.getText().toString());
        roomList.get(index).setNum(num.getText().toString());

        db.getReference().child("rooms").setValue(roomList);


        String rn=num.getText().toString();

        if(uri1!=null)
            uploadImage(uri1,rn,"im1",index);
        if(uri2!=null)
            uploadImage(uri2,rn,"im2",index);
        if(uri3!=null)
            uploadImage(uri3,rn,"im3",index);



    }

    public void setPicture(String Rname, final String img, final ImageView dp) throws IOException {
        StorageReference riversRef = mStorageRef.child("images/"+Rname+img+".jpg");

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
    public void uploadImage(Uri file, String Rname, final String img, final int roomIndex){
        //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        StorageReference riversRef = mStorageRef.child("images/"+Rname+img+".jpg");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        db.getReference().child("rooms").child(Integer.toString(roomIndex)).child(img).setValue(downloadUrl.toString());
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
