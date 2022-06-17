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

public class UpdateGarage extends AppCompatActivity {

    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    EditText editText,editText1,editText2,editText3,editText4,editText5,editText6;
    GoogleSignInClient mGoogleSignInClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_garage);

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


        editText=findViewById(R.id.garagename);
        editText1=findViewById(R.id.garageaddress);
        editText2=findViewById(R.id.garageslots);
        editText3=findViewById(R.id.garagerate);
        editText4=findViewById(R.id.garageavailable);
        editText5=findViewById(R.id.xcoord);
        editText6=findViewById(R.id.ycoord);

        reference= FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Information").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                 String garagename_firebase=dataSnapshot.child("garageName").getValue().toString();
                 String garageaddress_firebase=dataSnapshot.child("address").getValue().toString();
                 String garageslots_firebase=dataSnapshot.child("numberofSlots").getValue().toString();
                 String garagerate_firebase=dataSnapshot.child("ratePerHour").getValue().toString();
                 String garageavailable_firebase=dataSnapshot.child("numberofAvailable").getValue().toString();
                 String garagexcoord_firebase=dataSnapshot.child("x_CoordGoogleMap").getValue().toString();
                 String garageycoord_firebase=dataSnapshot.child("y_CoordGoogleMap").getValue().toString();

                editText.setText(garagename_firebase);
                editText1.setText(garageaddress_firebase);
                editText2.setText(garageslots_firebase);
                editText3.setText(garagerate_firebase);
                editText4.setText(garageavailable_firebase);
                editText5.setText(garagexcoord_firebase);
                editText6.setText(garageycoord_firebase);

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

    public void UpdateGarage(View view) {


        try {

            final String garagename=editText.getText().toString();
            final String garageaddress=editText1.getText().toString();
            final int garageslots=Integer.parseInt(editText2.getText().toString());
            final double garagerate=Double.parseDouble(editText3.getText().toString());
            final int garageavailable=Integer.parseInt(editText4.getText().toString());
            final double xcoord=Double.parseDouble(editText5.getText().toString());
            final double ycoord=Double.parseDouble(editText6.getText().toString());

            if(garagename.equalsIgnoreCase("")){
                editText.setError("Garage Name is Required");
            }
            else if (garageaddress.equalsIgnoreCase("")){
                editText1.setError("Garage Address is Required");
            }
            else if (garageslots<0){
                editText2.setError("Enter Valid Number");
            }
            else if (garagerate<0){
                editText3.setError("Enter Valid Number");
            }
            else if (garageavailable<0){
                editText4.setError("Enter Valid Number");
            }
            else if (xcoord<0){
                editText5.setError("Enter Valid Number");
            }
            else if (ycoord<0){
                editText6.setError("Enter Valid Number");
            }

            else{

                Garage garage=new Garage();
                garage.setAddress(garageaddress);
                garage.setGarageName(garagename);
                garage.setNumberofSlots(garageslots);
                garage.setRatePerHour(garagerate);
                garage.setNumberofAvailable(garageavailable);
                garage.setX_CoordGoogleMap(xcoord);
                garage.setY_CoordGoogleMap(ycoord);

                reference.setValue(garage);

                Toast.makeText(getApplicationContext(), "Garage Information is Updated", Toast.LENGTH_LONG).show();
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
                    }
                });
    }
}
