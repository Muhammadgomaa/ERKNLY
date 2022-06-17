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

public class MakeReservation extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference,reference1;
    GoogleSignInClient mGoogleSignInClient;
    TextView textView4,textView5,textView6,textView7,textView8;
    ArrayList<String> OwnersList = new ArrayList<>();
    int Pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_reservation);


        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        textView4=findViewById(R.id.garagename);
        textView5=findViewById(R.id.garageaddress);
        textView6=findViewById(R.id.garageslots);
        textView7=findViewById(R.id.garagerate);
        textView8=findViewById(R.id.garageavailable);


        Intent intent=getIntent();
        Pos=intent.getIntExtra("Position",0);


        reference = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Information");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    OwnersList.add(snapshot.getKey());
                }

                reference1 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Information").child(OwnersList.get(Pos));
                reference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String garagename_firebase=dataSnapshot.child("garageName").getValue().toString();
                        String garageaddress_firebase=dataSnapshot.child("address").getValue().toString();
                        String garageslots_firebase=dataSnapshot.child("numberofSlots").getValue().toString();
                        String garagerate_firebase=dataSnapshot.child("ratePerHour").getValue().toString();
                        String garageavailable_firebase=dataSnapshot.child("numberofAvailable").getValue().toString();


                        textView4.setText(garagename_firebase);
                        textView5.setText(garageaddress_firebase);
                        textView6.setText(garageslots_firebase);
                        textView7.setText(garagerate_firebase);
                        textView8.setText(garageavailable_firebase);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


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
    }

    public void MakeReservation(View view){

        Intent intent=new Intent(getApplicationContext(),CompleteReservation.class);
        intent.putExtra("Position",Pos);
        startActivity(intent);

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
                    }
                });
    }
}