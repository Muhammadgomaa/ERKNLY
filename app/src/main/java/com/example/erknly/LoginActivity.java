package com.example.erknly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

    EditText editText, editText1;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference, reference1;
    ArrayList<String> DriverID = new ArrayList<>();
    ArrayList<String> OwnerID = new ArrayList<>();
    ArrayList<String> DriverCheckID = new ArrayList<>();
    ArrayList<String> OwnerCheckID = new ArrayList<>();
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount firebaseAuthWithGoogle;
    ApiException apiException;
    AuthCredential authCredential;
    GoogleAuthProvider getIdToken;
    CallbackManager mCallbackManager;
    AccessToken token;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    TextView textView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText = findViewById(R.id.email);
        editText1 = findViewById(R.id.password);


        textView=findViewById(R.id.forget);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),ForgetPassword.class);
                startActivity(intent);

            }
        });


        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


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

    public void LoginUser(View view) {

        try {

            final String email = editText.getText().toString();
            final String password = editText1.getText().toString();

            if (email.equalsIgnoreCase("")) {
                editText.setError("E-Mail is Required");
            } else if (password.equalsIgnoreCase("")) {
                editText1.setError("Password is Required");
            } else {

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful() && (DriverID.contains(user.getUid()))) {

                            reference = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Car Information");
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                                        DriverCheckID.add(snapshot.getKey());

                                        if(DriverCheckID.contains(user.getUid())){
                                            Toast.makeText(getApplicationContext(), "Welcome Car Driver", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), CarDriverProfileMap.class);
                                            startActivity(intent);
                                        }
                                        else{

                                            Intent intent = new Intent(getApplicationContext(), AddCar.class);
                                            startActivity(intent);

                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });

                        } else if (task.isSuccessful() &&  (OwnerID.contains(user.getUid()))) {
                            reference1 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Information");
                            reference1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        OwnerCheckID.add(snapshot.getKey());


                                        if(OwnerCheckID.contains(user.getUid())){
                                            Toast.makeText(getApplicationContext(), "Welcome Garage Owner", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), GarageOwnerProfile.class);
                                            startActivity(intent);
                                        }
                                        else{

                                            Intent intent = new Intent(getApplicationContext(), AddGarage.class);
                                            startActivity(intent);

                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        } else {

                            Toast.makeText(getApplicationContext(), "Wrong Email or Password TRY AGAIN!", Toast.LENGTH_LONG).show();

                        }

                    }
                });
            }

        } catch (Exception e) {
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
                        if (task.isSuccessful() && (DriverID.contains(user.getUid()))) {

                            reference = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Car Information");
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        DriverCheckID.add(snapshot.getKey());

                                        if(DriverCheckID.contains(user.getUid())){
                                            Toast.makeText(getApplicationContext(), "Welcome Car Driver", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), CarDriverProfileMap.class);
                                            startActivity(intent);
                                        }
                                        else{

                                            Intent intent = new Intent(getApplicationContext(), AddCar.class);
                                            startActivity(intent);

                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                        else if (task.isSuccessful() && (OwnerID.contains(user.getUid()))) {

                            reference1 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Information");
                            reference1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        OwnerCheckID.add(snapshot.getKey());


                                        if(OwnerCheckID.contains(user.getUid())){
                                            Toast.makeText(getApplicationContext(), "Welcome Garage Owner", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), GarageOwnerProfile.class);
                                            startActivity(intent);
                                        }
                                        else{

                                            Intent intent = new Intent(getApplicationContext(), AddGarage.class);
                                            startActivity(intent);

                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Wrong Email or Password TRY AGAIN!", Toast.LENGTH_LONG).show();
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
                        if (task.isSuccessful() && (DriverID.contains(user.getUid()))) {

                            reference = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Car Information");
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        DriverCheckID.add(snapshot.getKey());

                                        if(DriverCheckID.contains(user.getUid())){
                                            Toast.makeText(getApplicationContext(), "Welcome Car Driver", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), CarDriverProfileMap.class);
                                            startActivity(intent);
                                        }
                                        else{

                                            Intent intent = new Intent(getApplicationContext(), AddCar.class);
                                            startActivity(intent);

                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                        else if (task.isSuccessful() && (OwnerID.contains(user.getUid()))) {

                            reference1 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Information");
                            reference1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        OwnerCheckID.add(snapshot.getKey());


                                        if(OwnerCheckID.contains(user.getUid())){
                                            Toast.makeText(getApplicationContext(), "Welcome Garage Owner", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), GarageOwnerProfile.class);
                                            startActivity(intent);
                                        }
                                        else{

                                            Intent intent = new Intent(getApplicationContext(), AddGarage.class);
                                            startActivity(intent);

                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Wrong Email or Password TRY AGAIN!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}