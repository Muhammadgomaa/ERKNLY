package com.example.erknly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.Spinner;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Locale;

public class CompleteReservation extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference,reference1,reference2,reference3,reference4,reference5;
    GoogleSignInClient mGoogleSignInClient;
    Spinner spinner,spinner1;
    TextView textView,textView1;
    String Section,Slot,Name,Plate,Color,Type;
    ArrayList<String> OwnersList = new ArrayList<>();
    ArrayList<String> DriversList = new ArrayList<>();
    ArrayList<String> Sections = new ArrayList();
    ArrayList<String> Slots = new ArrayList<>();
    Calendar calendar;
    boolean Reserved=true;
    int Pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_reservation);

        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        Intent intent=getIntent();
        Pos=intent.getIntExtra("Position",0);

        textView=findViewById(R.id.date);
        textView1=findViewById(R.id.time);
        spinner=findViewById(R.id.section);
        spinner1=findViewById(R.id.slot);

        calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String date=simpleDateFormat.format(calendar.getTime());

        SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH);
        String time=simpleDateFormat1.format(calendar.getTime());

        textView.setText(date);
        textView1.setText(time);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Section=parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Slot=parent.getSelectedItem().toString();
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

        BottomNavigationView navigationView=findViewById(R.id.nav_bottom);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id =menuItem.getItemId();

                if(id==R.id.map){
                    Intent intent=new Intent(getApplicationContext(),CarDriverProfileMap.class);
                    startActivity(intent);
                    return true;
                }

                if(id==R.id.list){
                    Intent intent=new Intent(getApplicationContext(),CarDriverProfileList.class);
                    startActivity(intent);
                    return true;
                }

                return false;
            }
        });


    }

    /*public boolean CheckSlot(){

        reference3 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Owner Information");
        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OwnersList.add(snapshot.getKey());
                }

                    reference4 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Reservations Information").child(OwnersList.get(Pos));
                    reference4.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            DriversList.add(snapshot.getKey());
                        }

                        for (int j = 0; j < DriversList.size(); j++) {

                            reference5 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Reservations Information").child(OwnersList.get(Pos)).child(DriversList.get(j));
                            reference5.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    Slots.add(dataSnapshot.child("slotNumber").getValue().toString());
                                    Sections.add(dataSnapshot.child("section").getValue().toString());

                                    spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                            Slot = parent.getSelectedItem().toString();

                                            if (
                                                    (Slots.contains(Slot) && Sections.contains("A")) ||
                                                    (Slots.contains(Slot) && Sections.contains("B")) ||
                                                    (Slots.contains(Slot) && Sections.contains("C")) ||
                                                    (Slots.contains(Slot) && Sections.contains("D"))) {

                                                Reserved=true;

                                            } else {


                                                Reserved=false;

                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(Reserved==true)
            return true;

        return false;

    }
*/

    public void CompleteReservation(View view) {

        try {


            final String date = textView.getText().toString();
            final String time = textView1.getText().toString();


            reference = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Information");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        OwnersList.add(snapshot.getKey());
                    }


                    reference1 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Reservations Information").child(OwnersList.get(Pos)).child(user.getUid());
                    reference2 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Car Information").child(user.getUid());
                    reference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Name = dataSnapshot.child("licenseHolder").getValue().toString();
                            Plate = dataSnapshot.child("plateNumber").getValue().toString();
                            Color = dataSnapshot.child("carColor").getValue().toString();
                            Type = dataSnapshot.child("carType").getValue().toString();

                            Reservation reservation = new Reservation();
                            reservation.setDate(date);
                            reservation.setTime(time);
                            reservation.setSection(Section);
                            reservation.setSlotNumber(Slot);
                            reservation.setName(Name);
                            reservation.setCarNumber(Plate);
                            reservation.setCarClor(Color);
                            reservation.setCarType(Type);

                            reference1.setValue(reservation);
                            Toast.makeText(getApplicationContext(), "Your Reservation is Completed", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), CarDriverProfileMap.class);
                            startActivity(intent);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception e){
            return;
        }

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
                    }
                });
    }

}