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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zeekay.hotelroombooker.models.user;

public class Register extends AppCompatActivity {

    EditText em;
    EditText fn;
    EditText ln;
    EditText pass;

    FirebaseDatabase db;
    DatabaseReference userRef;
    private FirebaseAuth mAuth;
    String uid;

    ProgressDialog pd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        db=FirebaseDatabase.getInstance();
        userRef=db.getReference().child("users");

        em=findViewById(R.id.em);
        fn=findViewById(R.id.fn);
        ln=findViewById(R.id.ln);
        pass=findViewById(R.id.pass);


    }

    public void Register(View v){
        pd= new ProgressDialog(this);
        pd.setMessage("Fetching Data");
        pd.setCancelable(false);
        pd.show();

        String email=em.getText().toString();
        String password=pass.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Toast.makeText(getApplicationContext(), "Reg succ",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            uid=user.getUid();
                            addUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(getApplicationContext(), "Reg failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }


                });

    }

    private void updateUI(FirebaseUser user) {
        pd.cancel();
    }

    public void addUser() {

        user u = new user(fn.getText().toString(), ln.getText().toString(), em.getText().toString(), pass.getText().toString(), 1);
        u.setUid(uid);

        userRef.child(uid).setValue(u);
        //Gson gson = new Gson();
        //String json = gson.toJson(u);
        //Log.d("Json",json);

        SharedPreferences.Editor editor = getSharedPreferences("prefs", MODE_PRIVATE).edit();

        if (u.getType() == 0) {
            editor.putInt("type", 0);
            editor.apply();
            startActivity(new Intent(this, adminMenu.class));

        } else {
            editor.putInt("type", 1);
            editor.apply();
            startActivity(new Intent(this, userMenu.class));

        }

    }
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

}
