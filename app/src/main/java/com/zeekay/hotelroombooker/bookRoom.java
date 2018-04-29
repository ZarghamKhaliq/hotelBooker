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
import android.widget.TextView;
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

public class bookRoom extends AppCompatActivity {

        FirebaseDatabase db;
        DatabaseReference rooms;
        private StorageReference mStorageRef;

        ImageView im1;
        ImageView im2;
        ImageView im3;

        int index;

        CheckBox tv;
        CheckBox ac;
        CheckBox wifi;
        TextView beds;
        TextView price;
        TextView num;
        ArrayList<room> roomList;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_book_room);


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
          //  Toast.makeText(this,r.getNum(),Toast.LENGTH_SHORT).show();
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
        public void Book(View v){

            db.getReference().child("rooms").child(Integer.toString(index)).child("isBooked").setValue(true);

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


}
