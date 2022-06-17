package com.example.erknly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

public class UpdateGarageOwner extends AppCompatActivity {

    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    EditText editText,editText1,editText2,editText3,editText4;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_garage_owner);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();


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

        reference= FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Owner Information").child(user.getUid());

        editText=findViewById(R.id.email);
        editText2=findViewById(R.id.phonenumber);
        editText3=findViewById(R.id.name);
        editText4=findViewById(R.id.personalid);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String email_firebase=dataSnapshot.child("email").getValue().toString();
                String name_firebase=dataSnapshot.child("name").getValue().toString();
                String phone_firebase=dataSnapshot.child("phone").getValue().toString();
                String personalid_firebase=dataSnapshot.child("personalID").getValue().toString();


                editText.setText(email_firebase);
                editText2.setText(phone_firebase);
                editText3.setText(name_firebase);
                editText4.setText(personalid_firebase);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
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


    public void UpdateGarageOwnerUser(View view) {

        try {

            String email=editText.getText().toString();
            String phone=editText2.getText().toString();
            String name=editText3.getText().toString();
            String personalid=editText4.getText().toString();


            if(email.equalsIgnoreCase("")){
                editText.setError("E-Mail is Required");
            }
            else if(phone.equalsIgnoreCase("")){
                editText2.setError("Phone Number is Required");
            }
            else if(name.equalsIgnoreCase("")){
                editText3.setError("Name is Required");
            }
            else if(personalid.equalsIgnoreCase("")){
                editText4.setError("Personal ID is Required");
            }
            else if (!email.contains("@")){
                editText.setError("Please Enter Valid E-Mail Address");
            }
            else if (phone.length()<10){
                editText2.setError("Please Enter Valid Phone Number");
            }
            else if (personalid.length()<14){
                editText4.setError("Please Enter Valid License ID");
            }
            else {

                GarageOwner garageOwner=new GarageOwner();
                garageOwner.setEmail(email);
                garageOwner.setPhone(phone);
                garageOwner.setName(name);
                garageOwner.setPersonalID(personalid);
                reference.setValue(garageOwner);
                Toast.makeText(getApplicationContext(), "Garage Owner User is Updated", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), GarageOwnerProfile.class);
                startActivity(intent);

            }
        }
        catch (Exception e){
            return;
        }
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
