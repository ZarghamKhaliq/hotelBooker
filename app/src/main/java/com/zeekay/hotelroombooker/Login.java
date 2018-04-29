package com.zeekay.hotelroombooker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zeekay.hotelroombooker.models.user;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText pass;
    private EditText username;
    FirebaseDatabase db;
    DatabaseReference CurrentUser;

    ProgressDialog pd ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        db=FirebaseDatabase.getInstance();
        CurrentUser=db.getReference().child("users");

        pass=findViewById(R.id.password);
        username=findViewById(R.id.email);
    }

    public void Login (View view){
        pd= new ProgressDialog(this);
        pd.setMessage("Fetching Data");
        pd.setCancelable(false);
        pd.show();

        String email=username.getText().toString();
        String password=pass.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //  Log.d(TAG, "createUserWithEmail:success");

                            Toast.makeText(getApplicationContext(), "Login",
                                    Toast.LENGTH_SHORT).show();

                            FirebaseUser user = mAuth.getCurrentUser();

                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Login", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(final FirebaseUser u) {
        pd.cancel();


        if(u!=null) {
            Log.e("Login", "User not Null");

            CurrentUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> childs=dataSnapshot.getChildren();

                    int t=-1;

                    Log.d("LoginUser","Data Fetched+");
                    for (DataSnapshot child:childs) {


                        user temp = child.getValue(user.class);

                        if(temp.getEmail().equals(u.getEmail())) {
                            Log.d("LoginU", temp.getFirstname());
                            t = temp.getType();
                        }

                    }

                    SharedPreferences.Editor editor = getSharedPreferences("prefs", MODE_PRIVATE).edit();
                    editor.putInt("type", t);
                    editor.apply();

                    if (t == 0) {

                        startActivity(new Intent(getApplicationContext(), adminMenu.class));

                    } else {

                        startActivity(new Intent(getApplicationContext(), userMenu.class));

                    }
                    finish();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




        }
        else
            Log.e("LoginU", "User Null");

    }

    public void registerScreen(View v){
        startActivity(new Intent(this,Register.class));
    }
}
