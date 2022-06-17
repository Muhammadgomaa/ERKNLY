package com.example.erknly;

import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;

import androidx.annotation.NonNull;

import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;

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

public class CarDriverProfileMap extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    DatabaseReference reference,reference1,reference2,reference3,reference4;
    FirebaseAuth auth;
    FirebaseUser user;
    GoogleSignInClient mGoogleSignInClient;
    int Pos;
    String GarageName;
    double GarageXCoord,GarageYCoord;
    ArrayList<String> OwnersID = new ArrayList<>();
    ArrayList<String> DriversID = new ArrayList<>();
    ArrayList<String> GaragesIDList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_driver_profile_map);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        Intent intent=getIntent();
        Pos=intent.getIntExtra("Position",0);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        BottomNavigationView navigationView=findViewById(R.id.nav_bottom);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id =menuItem.getItemId();

                if(id==R.id.map){
                    Intent intent=new Intent(getApplicationContext(),CarDriverProfileMap.class);
                    startActivity(intent);
                    return true;
                }

                if(id==R.id.list){
                    Intent intent=new Intent(getApplicationContext(),CarDriverProfileList.class);
                    startActivity(intent);
                    return true;
                }

                return false;
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getUserPermission();

    }

    private void getUserPermission() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
            }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        LatLng cairo = new LatLng(30.0596185, 31.1884234);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cairo, 12));

        reference = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Reservations Information");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    OwnersID.add(snapshot.getKey());

                    for(int i=0;i<OwnersID.size();i++){

                        reference1 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Reservations Information").child(OwnersID.get(i));
                        final int finalI = i;
                        reference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {

                                    DriversID.add(snapshot.getKey());

                                    if (DriversID.contains(user.getUid())) {

                                        reference4 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Information").child(OwnersID.get(finalI));
                                        reference4.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                GarageName = dataSnapshot.child("garageName").getValue().toString();
                                                GarageXCoord = Double.valueOf(dataSnapshot.child("x_CoordGoogleMap").getValue().toString());
                                                GarageYCoord = Double.valueOf(dataSnapshot.child("y_CoordGoogleMap").getValue().toString());

                                                GarageXCoord = GarageXCoord - 0.0040673;
                                                GarageYCoord = GarageYCoord + 0.0012305;

                                                LatLng GarageLocation = new LatLng(GarageXCoord, GarageYCoord);
                                                mMap.addMarker(new MarkerOptions().position(GarageLocation).title(GarageName).
                                                        icon(BitmapDescriptorFactory.fromResource(R.drawable.location32)));


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        Toast.makeText(getApplicationContext(), "You Are On Live Reservation Now", Toast.LENGTH_LONG).show();
                                    }
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

        reference2 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Information");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    GaragesIDList.add(snapshot.getKey());
                }

                for (int i = 0; i < GaragesIDList.size(); i++) {

                    reference3 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Information").child(GaragesIDList.get(i));
                    reference3.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            GarageName = dataSnapshot.child("garageName").getValue().toString();
                            GarageXCoord = Double.valueOf(dataSnapshot.child("x_CoordGoogleMap").getValue().toString());
                            GarageYCoord = Double.valueOf(dataSnapshot.child("y_CoordGoogleMap").getValue().toString());

                            GarageXCoord = GarageXCoord - 0.0040673;
                            GarageYCoord = GarageYCoord + 0.0012305;

                            LatLng GarageLocation = new LatLng(GarageXCoord, GarageYCoord);
                            mMap.addMarker(new MarkerOptions().position(GarageLocation).title(GarageName).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.marker16)));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
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
}