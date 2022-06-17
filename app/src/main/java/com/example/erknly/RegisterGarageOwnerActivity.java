package com.example.erknly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RegisterGarageOwnerActivity extends AppCompatActivity {

    EditText editText,editText1,editText2,editText3,editText4;
    FirebaseAuth auth;
    DatabaseReference reference,reference1;
    FirebaseUser user;
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager mCallbackManager;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    ArrayList<String> OwnersID=new ArrayList<>();
    ArrayList<String>Emails=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_garage_owner);

        editText = findViewById(R.id.email);
        editText1 = findViewById(R.id.password);
        editText2 = findViewById(R.id.phonenumber);
        editText3 = findViewById(R.id.name);
        editText4 = findViewById(R.id.personalid);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());


        auth = FirebaseAuth.getInstance();
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




        SignInButton signInButton = findViewById(R.id.googlebutton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.facebookbutton);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "User is Cancel Application", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null) {
            auth.removeAuthStateListener(firebaseAuthListener);
        }
    }


    public void CreateGarageOwnerUser(View view){

        try {

            final String email=editText.getText().toString();
            final String password=editText1.getText().toString();
            final String phone= editText2.getText().toString();
            final String name=editText3.getText().toString();
            final String personalid=editText4.getText().toString();


            if(Check()==true){
                editText.setError("E-Mail is Already Exist");
            }
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
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            user=auth.getCurrentUser();

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

                        else {

                            Toast.makeText(getApplicationContext(), "Somthing Wrong TRY AGAIN!", Toast.LENGTH_LONG).show();

                        }

                    }
                });
            }

        }
        catch (Exception e){
            return;
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {


        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = auth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), GoogleRegisterGarageOwnerActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(), "Please Complete Required Information", Toast.LENGTH_LONG).show();

                        } else {

                            Toast.makeText(getApplicationContext(), "Somthing Wrong TRY AGAIN!", Toast.LENGTH_LONG).show();

                        }

                    }
                });
    }



    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = auth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), FacebookRegisterGarageOwnerActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(), "Please Complete Required Information", Toast.LENGTH_LONG).show();

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Somthing Wrong TRY AGAIN!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public boolean Check(){

        final String email=editText.getText().toString();
        final boolean[] x = {true};
        reference1 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Owner Information");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    OwnersID.add(snapshot.getKey());
                }

                for(int i=0;i<OwnersID.size();i++){
                    reference1 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Owner Information").child(OwnersID.get(i));
                    reference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Emails.add(dataSnapshot.child("email").getValue().toString());

                            if(Emails.contains(email)){
                                x[0] =true;
                            }

                            else{
                                x[0]=false;
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        if(x[0]==true)
            return true;

        return false;
    }

}
