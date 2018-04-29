package com.zeekay.hotelroombooker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zeekay.hotelroombooker.models.room;
import com.zeekay.hotelroombooker.models.user;

import java.util.ArrayList;

public class userList extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference users;

    ListView list;
    ArrayList<String> userName;
    ArrayList<user> userList;
    ArrayAdapter adapter;

    ProgressDialog pd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);


        userName=new ArrayList<>();
        userList=new ArrayList<>();

        db=FirebaseDatabase.getInstance();
        users=db.getReference().child("users");

        list=findViewById(R.id.list);
        pd= new ProgressDialog(this);
        pd.setMessage("Fetching Data");
        pd.setCancelable(false);
        pd.show();


        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> childs=dataSnapshot.getChildren();
                userList.clear();
                Log.d("Data","Data Fetched");
                for (DataSnapshot child:childs) {

                    user temp = child.getValue(user.class);
                    userList.add(temp);
                    if(temp.getType()==1)
                        userName.add(temp.getFirstname());
                }
                pd.cancel();
                updateUserList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(),"Error Fetching Data",Toast.LENGTH_LONG).show();
                pd.cancel();
            }
        });



        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,userName);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),Integer.toString(position),Toast.LENGTH_SHORT).show();
                ShowDialog(position);
            }
        });

        //33700293
    }

    private void updateUserList() {
        adapter.notifyDataSetChanged();
    }

    public void ShowDialog(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You want to delete the user ?");
        // Add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                String uid=userList.get(pos).getUid();

                db.getReference().child("users").child(uid).removeValue();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
