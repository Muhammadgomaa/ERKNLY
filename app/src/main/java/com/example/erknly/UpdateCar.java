package com.example.erknly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
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

public class UpdateCar extends AppCompatActivity {

    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    EditText editText,editText1;
    Spinner spinner,spinner1;
    GoogleSignInClient mGoogleSignInClient;
    String cartype,carcolor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_car);


        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        editText=findViewById(R.id.licenseholder);
        editText1=findViewById(R.id.platenumber);
        spinner=findViewById(R.id.cartype);
        spinner1=findViewById(R.id.carcolor);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cartype=parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carcolor=parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        reference= FirebaseDatabase.getInstance().getReference("Erknly Database").child("Car Information").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name_firebase=dataSnapshot.child("licenseHolder").getValue().toString();
                String platenumber_firebase=dataSnapshot.child("plateNumber").getValue().toString();

                editText.setText(name_firebase);
                editText1.setText(platenumber_firebase);
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
            Intent intent=new Intent(this,CarDriverProfileMap.class);
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

    public void UpdateCar(View view) {

        try {

            String licenseholder=editText.getText().toString();
            String platenumber=editText1.getText().toString();

            if(licenseholder.equalsIgnoreCase("")){
                editText.setError("License Holder Name is Required");
            }
            else if (platenumber.equalsIgnoreCase("")){
                editText1.setError("Car Plate Number is Required");
            }
            else if (platenumber.length()!=6){
                editText1.setError("Enter Valid Car Plate Number");
            }
            else if (cartype.equalsIgnoreCase("")){
                editText1.setError("Car Type is Required");
            }
            else if (carcolor.equalsIgnoreCase("")){
                editText1.setError("Car Color is Required");
            }
            else{

                Car car=new Car();
                car.setLicenseHolder(licenseholder);
                car.setPlateNumber(platenumber);
                car.setCarType(cartype);
                car.setCarColor(carcolor);
                reference.setValue(car);
                Toast.makeText(getApplicationContext(), "Car Information is Updated", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), CarDriverProfileMap.class);
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
