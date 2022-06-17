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

public class GarageOwnerProfile extends AppCompatActivity {

    DatabaseReference reference,reference1;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView textView,textView1,textView2,textView3,textView4,textView5,textView6,textView7,textView8;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_owner_profile);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        textView=findViewById(R.id.name);
        textView1=findViewById(R.id.phone);
        textView2=findViewById(R.id.email);
        textView3=findViewById(R.id.personalid);
        textView4=findViewById(R.id.garagename);
        textView5=findViewById(R.id.garageaddress);
        textView6=findViewById(R.id.garageslots);
        textView7=findViewById(R.id.garagerate);
        textView8=findViewById(R.id.avaliable);


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


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        reference= FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Information").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String Name=dataSnapshot.child("garageName").getValue().toString();
                String Address=dataSnapshot.child("address").getValue().toString();
                String Slots=dataSnapshot.child("numberofSlots").getValue().toString();
                String Rate=dataSnapshot.child("ratePerHour").getValue().toString();
                String Available=dataSnapshot.child("numberofAvailable").getValue().toString();

                textView4.setText( Name );
                textView5.setText(Address);
                textView6.setText(Slots);
                textView7.setText(Rate);
                textView8.setText(Available);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        reference1= FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Owner Information").child(user.getUid());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String Name=dataSnapshot.child("name").getValue().toString();
                String Phone=dataSnapshot.child("phone").getValue().toString();
                String E_mail=dataSnapshot.child("email").getValue().toString();
                String PID=dataSnapshot.child("personalID").getValue().toString();

                textView.setText( Name);
                textView1.setText(Phone);
                textView2.setText(E_mail);
                textView3.setText(PID);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    public void ShowReservations(View view) {

        Intent intent=new Intent(this,ReservationsList.class);
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

        if(id==R.id.home){
            Intent intent=new Intent(this,GarageOwnerProfile.class);
            startActivity(intent);
            return true;
        }

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
                    }
                });
    }

}
