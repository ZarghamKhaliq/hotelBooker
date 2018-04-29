package com.zeekay.hotelroombooker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class adminMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);
    }
    public void AddRoom(View v){
        startActivity(new Intent(this,addRoom.class));
    }
    public void freeRooms(View v){
        startActivity(new Intent(this,roomList.class));
    }
    public void logout(View v){

        SharedPreferences.Editor editor = getSharedPreferences("prefs", MODE_PRIVATE).edit();
        editor.clear().commit();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,Login.class));
    }
    public void DeleteUsers(View v){
        startActivity(new Intent(this,userList.class));
    }
    public void bookedRoom(View v){
        startActivity(new Intent(this ,bookedRoom.class));
    }


}
