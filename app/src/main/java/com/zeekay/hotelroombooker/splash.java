package com.zeekay.hotelroombooker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        if (user != null) {
            int type = prefs.getInt("type", 1); //0 is the default value.

            if(type==1)
                startActivity(new Intent(this, userMenu.class));
            else
                startActivity(new Intent(this, adminMenu.class));
        } else {
            Intent i = new Intent(this, Login.class);
            startActivity(i);
        }

    }
}
