package com.example.erknly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

public class CarDriverProfile extends AppCompatActivity {


    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference,reference1,reference2;
    GoogleSignInClient mGoogleSignInClient;
    ImageView imageView;
    TextView textView,textView1,textView2,textView3,textView4,textView5,textView6,textView7,textView8,textView9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_driver_profile);

        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();


        textView=findViewById(R.id.name);
        textView1=findViewById(R.id.phone);
        textView2=findViewById(R.id.email);
        textView3=findViewById(R.id.license);
        textView4=findViewById(R.id.cartype);
        textView5=findViewById(R.id.carcolor);
        textView6=findViewById(R.id.carnumber);
        textView7=findViewById(R.id.number);
        textView8=findViewById(R.id.cost);
        textView9=findViewById(R.id.time);

        imageView=findViewById(R.id.premium);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        reference=FirebaseDatabase.getInstance().getReference("Erknly Database").child("Car Driver Information").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                textView.setText(dataSnapshot.child("name").getValue().toString());
                textView1.setText(dataSnapshot.child("phone").getValue().toString());
                textView2.setText(dataSnapshot.child("email").getValue().toString());
                textView3.setText(dataSnapshot.child("license").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference1=FirebaseDatabase.getInstance().getReference("Erknly Database").child("Car Information").child(user.getUid());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                textView4.setText(dataSnapshot.child("carType").getValue().toString());
                textView5.setText(dataSnapshot.child("carColor").getValue().toString());
                textView6.setText(dataSnapshot.child("plateNumber").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference2=FirebaseDatabase.getInstance().getReference("Erknly Database").child("Car Driver Status").child(user.getUid());
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                textView7.setText(dataSnapshot.child("numberofReservations").getValue().toString());
                textView8.setText(dataSnapshot.child("totalCostofReservations").getValue().toString());
                textView9.setText(dataSnapshot.child("totalTimeofReservations").getValue().toString());

                int NumberofReservations= Integer.parseInt(dataSnapshot.child("numberofReservations").getValue().toString());

                if(NumberofReservations>5){
                    imageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
}