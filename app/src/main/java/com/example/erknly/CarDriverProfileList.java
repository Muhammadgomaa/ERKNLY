package com.example.erknly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CarDriverProfileList extends AppCompatActivity {

    DatabaseReference reference,reference1,reference2,reference3;
    FirebaseAuth auth;
    FirebaseUser user;
    GoogleSignInClient mGoogleSignInClient;
    ListView garageslist;
    ArrayList<String> GaragesNameList = new ArrayList<>();
    ArrayList<String> GaragesAddressList = new ArrayList<>();
    ArrayList<String> GaragesAvailableList = new ArrayList<>();
    ArrayList<String> OwnersList = new ArrayList<>();
    ArrayList<String> OwnersID = new ArrayList<>();
    ArrayList<String> DriversID = new ArrayList<>();
    MyAdapter adapter;
    int Pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_driver_profile_list);


        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        reference = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Information");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    OwnersList.add(snapshot.getKey());
                }

                for(int i=0;i<OwnersList.size();i++){
                    reference1 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Information").child(OwnersList.get(i));
                    reference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            GaragesNameList.add(dataSnapshot.child("garageName").getValue().toString());
                            GaragesAddressList.add(dataSnapshot.child("address").getValue().toString());
                            GaragesAvailableList.add(dataSnapshot.child("numberofAvailable").getValue().toString());

                            garageslist=findViewById(R.id.list);
                            adapter=new MyAdapter(GaragesNameList,GaragesAddressList,GaragesAvailableList);
                            garageslist.setAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        reference2 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Reservations Information");
        reference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                OwnersID.add(snapshot.getKey());

                            for(int i=0;i<OwnersID.size();i++){

                                reference3 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Reservations Information").child(OwnersID.get(i));
                                reference3.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for(DataSnapshot snapshot: dataSnapshot.getChildren()) {

                                            DriversID.add(snapshot.getKey());

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                              }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cardriver_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id =item.getItemId();

        if(id==R.id.home){
            Intent intent=new Intent(this, CarDriverProfileMap.class);
            startActivity(intent);
            return true;
        }

        if(id==R.id.updatepersonal){
            Intent intent=new Intent(this,UpdateCarDriver.class);
            startActivity(intent);
            return true;
        }

        if(id==R.id.updatecar){
            Intent intent=new Intent(this,UpdateCar.class);
            startActivity(intent);
            return true;
        }

        if(id==R.id.profile){
            Intent intent=new Intent(this,CarDriverProfile.class);
            startActivity(intent);
            return true;
        }

        if(id==R.id.signout){
            signOut();
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Thanks For Using Application",Toast.LENGTH_LONG).show();
                        //finish();
                    }
                });
    }

    class MyAdapter extends BaseAdapter {

        ArrayList<String> GarageName;
        ArrayList<String> GarageAddress;
        ArrayList<String> GarageAvailable;



        MyAdapter(ArrayList<String>Name,ArrayList<String>Address,ArrayList<String>Available){

            this.GarageName=Name;
            this.GarageAddress=Address;
            this.GarageAvailable=Available;

        }


        @Override
        public int getCount() {
            return GarageName.size();
        }

        @Override
        public Object getItem(int position) {
            return GarageName.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater layoutInflater=getLayoutInflater();

            final View view=layoutInflater.inflate(R.layout.row,parent,false);

            final TextView garagename=view.findViewById(R.id.garagename);
            TextView garageaddress=view.findViewById(R.id.garageaddress);
            TextView garageavailable=view.findViewById(R.id.garageavailable);
            final Button button=view.findViewById(R.id.buttonreserv);

            garagename.setText(GarageName.get(position));
            garageaddress.setText(GarageAddress.get(position));
            garageavailable.setText(GarageAvailable.get(position));


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Pos=position;

                    if(DriversID.contains(user.getUid())){

                        Toast.makeText(getApplicationContext(),"You Are On Live Reservation You Can Not Make Another One",Toast.LENGTH_LONG).show();
                        button.setEnabled(false);

                    }

                    else{

                        Intent intent=new Intent(getApplicationContext(),MakeReservation.class);
                        intent.putExtra("Position",Pos);
                        startActivity(intent);

                    }


                }
            });

            return view;
        }

    }


}