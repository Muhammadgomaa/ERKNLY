package com.example.erknly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddGarage extends AppCompatActivity {

    DatabaseReference reference,reference1;
    FirebaseAuth auth;
    FirebaseUser user;
    EditText editText,editText1,editText2,editText3,editText4,editText5;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_garage);

        editText=findViewById(R.id.addname);
        editText1=findViewById(R.id.addaddress);
        editText2=findViewById(R.id.addslots);
        editText3=findViewById(R.id.addrate);
        editText4=findViewById(R.id.addxcoord);
        editText5=findViewById(R.id.addycoord);


        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        Toast.makeText(getApplicationContext(), "Please Complete Required Information", Toast.LENGTH_LONG).show();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }


    public void AddGarage(View view) {

        try {

             String garagename=editText.getText().toString();
             String garageaddress=editText1.getText().toString();
             int garageslots=Integer.parseInt(editText2.getText().toString());
            double garagerate=Double.parseDouble(editText3.getText().toString());
            double xcoord=Double.parseDouble(editText4.getText().toString());
            double ycoord=Double.parseDouble(editText5.getText().toString());

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
            else if (xcoord<0){
                editText4.setError("Enter Valid Number");
            }
            else if (ycoord<0){
                editText5.setError("Enter Valid Number");
            }

            else{

                reference= FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Information").child(user.getUid());

                Garage garage=new Garage();
                garage.setAddress(garageaddress);
                garage.setGarageName(garagename);
                garage.setNumberofSlots(garageslots);
                garage.setRatePerHour(garagerate);
                garage.setNumberofAvailable(garageslots);
                garage.setX_CoordGoogleMap(xcoord);
                garage.setY_CoordGoogleMap(ycoord);
                reference.setValue(garage);

                int Numbers=0;
                double Cost=0.00;
                long Time=0;

                reference1=FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Status").child(user.getUid());

                Status status=new Status();

                status.setNumberofReservations(Numbers);
                status.setTotalCostofReservations(Cost);
                status.setTotalTimeofReservations(Time);

                reference1.setValue(status);

                Toast.makeText(getApplicationContext(), "Garage Information is Added", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), GarageOwnerProfile.class);
                startActivity(intent);

            }
        }
        catch (Exception e){
            return;
        }
    }


}
