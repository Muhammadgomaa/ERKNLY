package com.example.erknly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

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

public class AddCar extends AppCompatActivity {

    DatabaseReference reference,reference1;
    FirebaseAuth auth;
    FirebaseUser user;
    EditText editText,editText1;
    Spinner spinner,spinner1;
    GoogleSignInClient mGoogleSignInClient;
    String cartype,carcolor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        Toast.makeText(getApplicationContext(), "Please Complete Required Information", Toast.LENGTH_LONG).show();


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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        reference= FirebaseDatabase.getInstance().getReference("Erknly Database").child("Car Driver Information").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name_firebase=dataSnapshot.child("name").getValue().toString();
                editText.setText(name_firebase);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }


    public void AddCar(View view) {

        try {

            String licenseholder=editText.getText().toString();
            String platenumber=editText1.getText().toString();

            if(licenseholder.equalsIgnoreCase("")){
                editText.setError("License Holder Name is Required");
            }
            else if (platenumber.equalsIgnoreCase("")){
                editText1.setError("Car Plate Number is Required");
            }
            else if (platenumber.length()>6){
                editText1.setError("Enter Valid Car Plate Number");
            }
            else if (cartype.equalsIgnoreCase("")){
                editText1.setError("Car Type is Required");
            }
            else if (carcolor.equalsIgnoreCase("")){
                editText1.setError("Car Color is Required");
            }
            else{

                reference= FirebaseDatabase.getInstance().getReference("Erknly Database").child("Car Information").child(user.getUid());

                Car car=new Car();
                car.setLicenseHolder(licenseholder);
                car.setPlateNumber(platenumber);
                car.setCarType(cartype);
                car.setCarColor(carcolor);
                reference.setValue(car);

                int Numbers=0;
                double Cost=0.00;
                long Time=0;

                reference1=FirebaseDatabase.getInstance().getReference("Erknly Database").child("Car Driver Status").child(user.getUid());

                Status status=new Status();

                status.setNumberofReservations(Numbers);
                status.setTotalCostofReservations(Cost);
                status.setTotalTimeofReservations(Time);

                reference1.setValue(status);

                Toast.makeText(getApplicationContext(), "Car Information is Added", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), CarDriverProfileMap.class);
                startActivity(intent);

            }
        }

        catch (Exception e){
            return;
        }

    }

}
