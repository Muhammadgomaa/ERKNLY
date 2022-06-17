package com.example.erknly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FacebookRegisterGarageOwnerActivity extends AppCompatActivity {

    EditText editText,editText1,editText2,editText3,editText4;
    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_register_garage_owner);

        editText=findViewById(R.id.email);
        editText1=findViewById(R.id.password);
        editText2=findViewById(R.id.phonenumber);
        editText3=findViewById(R.id.name);
        editText4=findViewById(R.id.personalid);

        auth=FirebaseAuth.getInstance();

        user=auth.getCurrentUser();

        final String email=user.getEmail();
        final String phone= user.getPhoneNumber();
        final String name=user.getDisplayName();

        editText.setText(email);
        editText2.setText(phone);
        editText3.setText(name);
    }

    public void CreateGarageOwnerUser(View view) {

        try {

            final String email=editText.getText().toString();
            final String password=editText1.getText().toString();
            final String phone= editText2.getText().toString();
            final String name=editText3.getText().toString();
            final String personalid=editText4.getText().toString();



            if(email.equalsIgnoreCase("")){
                editText.setError("E-Mail is Required");
            }
            else if(password.equalsIgnoreCase("")){
                editText1.setError("Password is Required");
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
            else if (password.length()<6){
                editText1.setError("Please Enter Strong Password");
            }
            else if (phone.length()!=11){
                editText2.setError("Please Enter Valid Phone Number");
            }
            else if (personalid.length()!=14){
                editText4.setError("Please Enter Valid Personal ID");
            }

            else{

                reference= FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Owner Information").child(user.getUid());

                GarageOwner garageOwner=new GarageOwner();
                garageOwner.setEmail(email);
                garageOwner.setPhone(phone);
                garageOwner.setName(name);
                garageOwner.setPersonalID(personalid);
                reference.setValue(garageOwner);
                Toast.makeText(getApplicationContext(), "Garage Owner User is Created", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }

        }
        catch (Exception e){
            return;
        }

    }

}
