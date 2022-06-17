package com.example.erknly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CheckReservation extends AppCompatActivity {

    DatabaseReference reference,reference1,reference2;
    FirebaseAuth auth;
    FirebaseUser user;
    GoogleSignInClient mGoogleSignInClient;
    TextView drivername,carnumber,carcolor,cartype;
    ArrayList<String> DriversList = new ArrayList<>();
    int Pos,Available,Slots;
    Double Rate,XCoord,YCoord;
    String Name,Address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_reservation);


        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        drivername=findViewById(R.id.name);
        carnumber=findViewById(R.id.carnumber);
        carcolor=findViewById(R.id.carcolor);
        cartype=findViewById(R.id.cartype);

        Intent intent=getIntent();

        Pos=intent.getIntExtra("Pos",0);
        Available=intent.getIntExtra("Available",0);
        Address=intent.getStringExtra("Address");
        Name=intent.getStringExtra("Name");
        Rate=intent.getDoubleExtra("Rate",0);
        Slots=intent.getIntExtra("Slots",0);
        XCoord=intent.getDoubleExtra("XCoord",0);
        YCoord=intent.getDoubleExtra("YCoord",0);
        Available=Available-1;



        BottomNavigationView navigationView=findViewById(R.id.nav_bottom);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id =menuItem.getItemId();

                if(id==R.id.home){
                    Intent intent=new Intent(getApplicationContext(),GarageOwnerProfile.class);
                    startActivity(intent);
                    return true;
                }

                if(id==R.id.signout){
                    signOut();
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    return true;
                }

                return false;
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Reservations Information").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    DriversList.add(snapshot.getKey());
                }

                for(int i=0;i<DriversList.size();i++){

                    reference1 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Car Information").child(DriversList.get(Pos));
                    reference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            drivername.setText(dataSnapshot.child("licenseHolder").getValue().toString());
                            carnumber.setText(dataSnapshot.child("plateNumber").getValue().toString());
                            carcolor.setText(dataSnapshot.child("carColor").getValue().toString());
                            cartype.setText(dataSnapshot.child("carType").getValue().toString());

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


    }

    public void ConfirmReservation(View view) {


            reference2= FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Information").child(user.getUid());
            Garage garage1=new Garage();
            garage1.setAddress(Address);
            garage1.setGarageName(Name);
            garage1.setNumberofSlots(Slots);
            garage1.setRatePerHour(Rate);
            garage1.setNumberofAvailable(Available);
            garage1.setX_CoordGoogleMap(XCoord);
            garage1.setY_CoordGoogleMap(YCoord);
            reference2.setValue(garage1);

            Toast.makeText(getApplicationContext(), "The Reservation is Checked and Car Enter To Garage", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), GarageOwnerProfile.class);
            startActivity(intent);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.garageowner_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id =item.getItemId();

        if(id==R.id.updategarage){
            Intent intent=new Intent(this,UpdateGarage.class);
            startActivity(intent);
            return true;
        }

        if(id==R.id.status){
            Intent intent=new Intent(this,GarageStatusActivity.class);
            startActivity(intent);
            return true;
        }

        if(id==R.id.updatepersonal){
            Intent intent=new Intent(this,UpdateGarageOwner.class);
            startActivity(intent);
            return true;
        }

        if(id==R.id.reserveation){
            Intent intent=new Intent(this,ReservationsList.class);
            startActivity(intent);
            return true;
        }

        if(id==R.id.signout){
            signOut();
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
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

}