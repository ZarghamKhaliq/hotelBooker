package com.zeekay.hotelroombooker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;

public class editRoomList extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference rooms;

    ListView list;
    ArrayList<String> roomName;
    ArrayList<String> roomNum;
    ArrayList<room> roomList;
    ArrayAdapter adapter;

    ProgressDialog pd ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room_list);



        roomName=new ArrayList<>();
        roomNum=new ArrayList<>();
        roomList=new ArrayList<>();

        db=FirebaseDatabase.getInstance();
        rooms=db.getReference().child("rooms");

        list=findViewById(R.id.list);
        pd= new ProgressDialog(this);
        pd.setMessage("Fetching Data");
        pd.setCancelable(false);
        pd.show();


        rooms.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> childs=dataSnapshot.getChildren();
                roomList.clear();
                roomName.clear();
                Log.d("Data","Data Fetched");
                for (DataSnapshot child:childs) {

                    room temp = child.getValue(room.class);
                    roomList.add(temp);
                    if(temp.isBooked()==false) {
                        roomName.add("Room # " + temp.getNum());
                        roomNum.add(temp.getNum());
                    }
                }
                pd.cancel();
                updateRoomList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(),"Error Fetching Data",Toast.LENGTH_LONG).show();
                pd.cancel();
            }
        });



        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,roomName);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),Integer.toString(position),Toast.LENGTH_SHORT).show();
                editRoomActivity(position);
            }
        });

        //33700293
    }

    private void updateRoomList() {
        adapter.notifyDataSetChanged();
    }

    public void editRoomActivity(final int pos) {

                // User clicked OK button
                String num=roomNum.get(pos);
                int index=0;

                for(int i=0;i<roomList.size();i++){
                    if(roomList.get(i).getNum().equals(num)) {
                        Log.d("listRoom",roomList.get(i).getNum());
                        index=i;
                        //db.getReference().child("rooms").child(Integer.toString(i)).child("isBooked").setValue(false);
                        break;
                    }
                }
        Intent i =new Intent(this,editRoom.class);
                i.putExtra("index",index);
                i.putExtra("num",num);
        startActivity(i);



    }

}
