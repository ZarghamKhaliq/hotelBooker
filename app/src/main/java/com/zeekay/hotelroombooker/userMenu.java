package com.zeekay.hotelroombooker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zeekay.hotelroombooker.models.room;

import java.util.ArrayList;

public class userMenu extends AppCompatActivity {

    String m_Text ;

    FirebaseDatabase db;
    DatabaseReference rev;
    DatabaseReference rateRef;

    ArrayList<String> reviews;
    ArrayList<Integer> ratings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);


        reviews=new ArrayList<>();
        ratings=new ArrayList<>();
        db=FirebaseDatabase.getInstance();
        rateRef=db.getReference().child("ratings");
        rev=db.getReference().child("reviews");
        rev.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> childs=dataSnapshot.getChildren();

                reviews.clear();
                Log.d("Data","Data Fetched");
                for (DataSnapshot child:childs) {

                    String temp = child.getValue(String.class);
                    reviews.add(temp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> childs=dataSnapshot.getChildren();

                ratings.clear();
                Log.d("Data","Data Fetched");
                for (DataSnapshot child:childs) {

                    int temp = child.getValue(Integer.class);
                    ratings.add(temp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public  void bookRoom(View V){
        startActivity(new Intent(this,roomList.class));
    }
    public  void giveReview(View V){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Review");
        builder.setMessage("Give us your valuable reviews ");
        builder.setCancelable(false);

// Set up the input
        final EditText input = new EditText(this);


// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                addReviewFirebase(m_Text);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }
    public void addReviewFirebase(String m){
        reviews.add(m);
        db.getReference().child("reviews").setValue(reviews);
    }
    public void addRatingFirebase(int r){
        ratings.add(r);
        db.getReference().child("ratings").setValue(ratings);
    }
    public  void rate(View V){
        final Dialog rankDialog = new Dialog(this);
        rankDialog.setContentView(R.layout.rank_dialog);
        rankDialog.setTitle("Rate us");
        rankDialog.setCancelable(false);
        final RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
        ratingBar.setRating(0);


        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int r= (int) ratingBar.getRating();
                addRatingFirebase(r);
                rankDialog.dismiss();

            }
        });
        //now that the dialog is set up, it's time to show it
        rankDialog.show();

    }
    public  void showProfile(View V){
        startActivity(new Intent(this,UserProfile.class));
    }
    public  void logout(View V){
        SharedPreferences.Editor editor = getSharedPreferences("prefs", MODE_PRIVATE).edit();
        editor.clear().commit();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,Login.class));

    }
}
