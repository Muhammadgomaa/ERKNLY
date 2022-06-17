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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ForgetPassword extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference, reference1,reference2,reference3;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    ArrayList<String> DriverID = new ArrayList<>();
    ArrayList<String> DriverEmail = new ArrayList<>();
    ArrayList<String> OwnerID = new ArrayList<>();
    ArrayList<String> OwnerEmail = new ArrayList<>();
    EditText forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        forget = findViewById(R.id.reset);

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                }
                else {
                    // User is signed out
                }
                // ...
            }
        };

        reference = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Car Driver Information");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    DriverID.add(snapshot.getKey());
                }

                for(int i=0;i<DriverID.size();i++){

                    reference2=FirebaseDatabase.getInstance().getReference("Erknly Database").child("Car Driver Information").child(DriverID.get(i));
                    reference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            DriverEmail.add(dataSnapshot.child("email").getValue().toString());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        reference1 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Owner Information");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OwnerID.add(snapshot.getKey());
                }

                for(int i=0;i<OwnerID.size();i++){

                    reference3=FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Owner Information").child(OwnerID.get(i));
                    reference3.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            OwnerEmail.add(dataSnapshot.child("email").getValue().toString());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    public void Reset(View view) {


        try {

            final String email = forget.getText().toString();

            if (email.equalsIgnoreCase("")) {
                forget.setError("E-mail Address is Required");
            }
            else if (!email.contains("@")){
                forget.setError("Enter Valid E-mail Address");
            }
            else if (DriverEmail.contains(email) || OwnerEmail.contains(email)){

                auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(getApplicationContext(),"Reset Link Password Sent To Your E-Mail",Toast.LENGTH_LONG).show();

                        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getApplicationContext(),"Wrong!",Toast.LENGTH_LONG).show();

                    }
                });
            }

            else{

                Toast.makeText(getApplicationContext(),"This E-Mail Address Not Register On Application Yet!",Toast.LENGTH_LONG).show();

            }

        }
        catch (Exception e){
            return;
        }

    }

    public void Register(View view) {

        Toast.makeText(getApplicationContext(),"Register Now On ERKNLY Application",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(getApplicationContext(),Main2Activity.class);
        startActivity(intent);

    }
}