package com.example.erknly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CollectFeesGarageOwner extends AppCompatActivity {

    DatabaseReference reference,reference1,reference2,reference3,reference4,reference5,reference6,reference7,reference8,reference9,reference10;
    FirebaseAuth auth;
    FirebaseUser user;
    GoogleSignInClient mGoogleSignInClient;
    TextView textView,textView1,textView2,textView3;
    Calendar calendar;
    ArrayList<String> DriversList = new ArrayList<>();
    int Pos,Available,Slots,Total,GarageReservations,DriverReservations;
    Double Rate,XCoord,YCoord,Price,TotalCostofGarageReservations,TotalCostofDriverReservations;
    long Time,TotalTimeofGarageReservations,TotalTimeofDriverReservations;
    String Name,Address,Cardrivername,Cartype,Carnumber,Carcolor,Section,Email,Slotnumber;
    String startdate,starttime,enddate,endtime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_fees_garage_owner);


        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent intent=getIntent();
        Pos=intent.getIntExtra("Pos",0);

        Available=intent.getIntExtra("Available",0);
        Address=intent.getStringExtra("Address");
        Name=intent.getStringExtra("Name");
        Rate=intent.getDoubleExtra("Rate",0);
        Slots=intent.getIntExtra("Slots",0);
        XCoord=intent.getDoubleExtra("XCoord",0);
        YCoord=intent.getDoubleExtra("YCoord",0);

        Available=Available+1;

        Cardrivername=intent.getStringExtra("DriverName");
        Carcolor=intent.getStringExtra("CarColor");
        Cartype=intent.getStringExtra("CarType");
        Carnumber=intent.getStringExtra("CarNumber");
        Section=intent.getStringExtra("Section");
        Slotnumber=intent.getStringExtra("SlotNumber");
        Email=intent.getStringExtra("Email");

        Total=intent.getIntExtra("Total",0);

        textView=findViewById(R.id.startdate);
        textView1=findViewById(R.id.starttime);
        textView2=findViewById(R.id.enddate);
        textView3=findViewById(R.id.endtime);

        calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        enddate=simpleDateFormat.format(calendar.getTime());

        SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH);
        endtime=simpleDateFormat1.format(calendar.getTime());


        textView2.setText(enddate);
        textView3.setText(endtime);


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


        reference7=FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Status").child(user.getUid());
        reference7.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                GarageReservations= Integer.parseInt(dataSnapshot.child("numberofReservations").getValue().toString());
                TotalCostofGarageReservations= Double.valueOf(dataSnapshot.child("totalCostofReservations").getValue().toString());
                TotalTimeofGarageReservations= Long.parseLong(dataSnapshot.child("totalTimeofReservations").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Reservations Information").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    DriversList.add(snapshot.getKey());
                }


                for(int i=0;i<DriversList.size();i++){

                    reference1 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Reservations Information").child(user.getUid()).child(DriversList.get(Pos));
                    reference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            textView.setText(dataSnapshot.child("date").getValue().toString());
                            textView1.setText(dataSnapshot.child("time").getValue().toString());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

                    reference9=FirebaseDatabase.getInstance().getReference("Erknly Database").child("Car Driver Status").child(DriversList.get(Pos));
                    reference9.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            DriverReservations= Integer.parseInt(dataSnapshot.child("numberofReservations").getValue().toString());
                            TotalCostofDriverReservations= Double.valueOf(dataSnapshot.child("totalCostofReservations").getValue().toString());
                            TotalTimeofDriverReservations= Long.parseLong(dataSnapshot.child("totalTimeofReservations").getValue().toString());

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

    }

    public void CalculateFees(View view) {

        GarageReservations=GarageReservations+1;
        DriverReservations=DriverReservations+1;

        if(DriverReservations>5){

            reference = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Reservations Information").child(user.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        DriversList.add(snapshot.getKey());
                    }

                    for(int i=0;i<DriversList.size();i++){

                        reference1 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Reservations Information").child(user.getUid()).child(DriversList.get(Pos));
                        reference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                textView.setText(dataSnapshot.child("date").getValue().toString());
                                textView1.setText(dataSnapshot.child("time").getValue().toString());

                                startdate=textView.getText().toString();
                                starttime=textView1.getText().toString();

                                enddate=textView2.getText().toString();
                                endtime=textView3.getText().toString();


                                SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a",Locale.ENGLISH);

                                try {
                                    Date date=sdf.parse(startdate+" "+starttime);
                                    Date date1=sdf.parse(enddate+"  "+endtime);

                                    long diff=date1.getTime()-date.getTime();

                                    Time= diff/(60*1000);
                                    Price=(Time*(Rate/60))-(((Time*(Rate/60)))*0.1);

                                    Toast.makeText(getApplicationContext(),"The Reservation is Calculated and Car Exit From Garage"+"\n"+"The Price ="+Price+" L.E",Toast.LENGTH_LONG).show();

                                    reference3 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("History Information").child("Reservation Number :"+""+Total).child(DriversList.get(Pos));

                                    History history=new History();
                                    history.setGarageName(Name);
                                    history.setGarageAddress(Address);
                                    history.setStartDate(startdate);
                                    history.setStartTime(starttime);
                                    history.setEndDate(enddate);
                                    history.setEndTime(endtime);
                                    history.setCarDriverName(Cardrivername);
                                    history.setCarNumber(Carnumber);
                                    history.setCarType(Cartype);
                                    history.setCarColor(Carcolor);
                                    history.setSection(Section);
                                    history.setSlotNumber(Integer.parseInt(Slotnumber));
                                    history.setReservationTime(Time);
                                    history.setReservationCost(Price);

                                    reference3.setValue(history);

                                    reference8=FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Status").child(user.getUid());

                                    double Cost=TotalCostofGarageReservations+Price;
                                    long Timee=TotalTimeofGarageReservations+Time;

                                    Status status=new Status();
                                    status.setTotalCostofReservations(Cost);
                                    status.setTotalTimeofReservations(Timee);
                                    status.setNumberofReservations(GarageReservations);

                                    reference8.setValue(status);

                                    reference10 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Car Driver Status").child(DriversList.get(Pos));

                                    double Cost1 = TotalCostofDriverReservations +Price;
                                    long Timee1 = TotalTimeofDriverReservations + Time;

                                    Status status1 = new Status();
                                    status1.setTotalCostofReservations(Cost1);
                                    status1.setTotalTimeofReservations(Timee1);
                                    status1.setNumberofReservations(DriverReservations);

                                    reference10.setValue(status1);


                                    Intent intent=new Intent(Intent.ACTION_SENDTO);
                                    intent.setData(Uri.parse("mailto:"));
                                    intent.putExtra(Intent.EXTRA_EMAIL,new String[]{Email,user.getEmail()});
                                    intent.putExtra(Intent.EXTRA_SUBJECT,"Reservation Details For Mr/Mrs (Perimum User) :"+""+Cardrivername);
                                    intent.putExtra(Intent.EXTRA_TEXT, "Reservation Details: \n"+
                                            "__________________________________________ \n"+
                                            "Garage Name :"+history.getGarageName()+"\n"+
                                            "Garage Address :"+history.getGarageAddress()+"\n"+
                                            "Start Date :"+history.getStartDate()+"\n"+
                                            "Start Time :"+history.getStartTime()+"\n"+
                                            "End Date :"+history.getEndDate()+"\n"+
                                            "End Time :"+history.getEndTime()+"\n"+
                                            "Car Driver Name :"+history.getCarDriverName()+"\n"+
                                            "Car Number :"+history.getCarNumber()+"\n"+
                                            "Car Type :"+history.getCarType()+"\n"+
                                            "Car Color :"+history.getCarColor()+"\n"+
                                            "Section :"+history.getSection()+"\n"+
                                            "Slot Number :"+history.getSlotNumber()+"\n"+
                                            "Reservation Time (In Min.) :"+history.getReservationTime()+"\n"+
                                            "Reservation Cost (In L.E) :"+history.getReservationCost()+"\n"+
                                            "__________________________________________ \n"+
                                            "Thanks For Using Application \n"+
                                            "Perimum User \n"+
                                            "ERKNLY Support Team, \n"+
                                            "Best Regardes,"
                                    );
                                    if(intent.resolveActivity(getPackageManager())!=null){
                                        startActivity(intent);
                                    }


                                } catch (ParseException e) {
                                    e.printStackTrace();
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

        }

        else{

            reference = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Reservations Information").child(user.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        DriversList.add(snapshot.getKey());
                    }

                    for(int i=0;i<DriversList.size();i++){

                        reference1 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Reservations Information").child(user.getUid()).child(DriversList.get(Pos));
                        reference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                textView.setText(dataSnapshot.child("date").getValue().toString());
                                textView1.setText(dataSnapshot.child("time").getValue().toString());

                                startdate=textView.getText().toString();
                                starttime=textView1.getText().toString();

                                enddate=textView2.getText().toString();
                                endtime=textView3.getText().toString();


                                SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a",Locale.ENGLISH);

                                try {
                                    Date date=sdf.parse(startdate+" "+starttime);
                                    Date date1=sdf.parse(enddate+"  "+endtime);

                                    long diff=date1.getTime()-date.getTime();

                                    Time= diff/(60*1000);
                                    Price=Time*(Rate/60);

                                    Toast.makeText(getApplicationContext(),"The Reservation is Calculated and Car Exit From Garage"+"\n"+"The Price ="+Price+" L.E",Toast.LENGTH_LONG).show();

                                    reference3 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("History Information").child("Reservation Number :"+""+Total).child(DriversList.get(Pos));

                                    History history=new History();
                                    history.setGarageName(Name);
                                    history.setGarageAddress(Address);
                                    history.setStartDate(startdate);
                                    history.setStartTime(starttime);
                                    history.setEndDate(enddate);
                                    history.setEndTime(endtime);
                                    history.setCarDriverName(Cardrivername);
                                    history.setCarNumber(Carnumber);
                                    history.setCarType(Cartype);
                                    history.setCarColor(Carcolor);
                                    history.setSection(Section);
                                    history.setSlotNumber(Integer.parseInt(Slotnumber));
                                    history.setReservationTime(Time);
                                    history.setReservationCost(Price);

                                    reference3.setValue(history);


                                    reference8=FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Status").child(user.getUid());

                                    double Cost=TotalCostofGarageReservations+Price;
                                    long Timee=TotalTimeofGarageReservations+Time;

                                    Status status=new Status();
                                    status.setTotalCostofReservations(Cost);
                                    status.setTotalTimeofReservations(Timee);
                                    status.setNumberofReservations(GarageReservations);

                                    reference8.setValue(status);

                                    reference10 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Car Driver Status").child(DriversList.get(Pos));

                                    double Cost1 = TotalCostofDriverReservations +Price;
                                    long Timee1 = TotalTimeofDriverReservations + Time;

                                    Status status1 = new Status();
                                    status1.setTotalCostofReservations(Cost1);
                                    status1.setTotalTimeofReservations(Timee1);
                                    status1.setNumberofReservations(DriverReservations);

                                    reference10.setValue(status1);


                                    Intent intent=new Intent(Intent.ACTION_SENDTO);
                                    intent.setData(Uri.parse("mailto:"));
                                    intent.putExtra(Intent.EXTRA_EMAIL,new String[]{Email,user.getEmail()});
                                    intent.putExtra(Intent.EXTRA_SUBJECT,"Reservation Details For Mr/Mrs :"+""+Cardrivername);
                                    intent.putExtra(Intent.EXTRA_TEXT, "Reservation Details: \n"+
                                            "__________________________________________ \n"+
                                            "Garage Name :"+history.getGarageName()+"\n"+
                                            "Garage Address :"+history.getGarageAddress()+"\n"+
                                            "Start Date :"+history.getStartDate()+"\n"+
                                            "Start Time :"+history.getStartTime()+"\n"+
                                            "End Date :"+history.getEndDate()+"\n"+
                                            "End Time :"+history.getEndTime()+"\n"+
                                            "Car Driver Name :"+history.getCarDriverName()+"\n"+
                                            "Car Number :"+history.getCarNumber()+"\n"+
                                            "Car Type :"+history.getCarType()+"\n"+
                                            "Car Color :"+history.getCarColor()+"\n"+
                                            "Section :"+history.getSection()+"\n"+
                                            "Slot Number :"+history.getSlotNumber()+"\n"+
                                            "Reservation Time (In Min.) :"+history.getReservationTime()+"\n"+
                                            "Reservation Cost (In L.E) :"+history.getReservationCost()+"\n"+
                                            "__________________________________________ \n"+
                                            "Thanks For Using Application \n"+
                                            "ERKNLY Support Team, \n"+
                                            "Best Regardes,"
                                    );
                                    if(intent.resolveActivity(getPackageManager())!=null){
                                        startActivity(intent);
                                    }


                                } catch (ParseException e) {
                                    e.printStackTrace();
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

        }

        reference2= FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Information").child(user.getUid());
        Garage garage1=new Garage();
        garage1.setAddress(Address);
        garage1.setGarageName(Name);
        garage1.setNumberofSlots(Slots);
        garage1.setRatePerHour(Rate);
        garage1.setNumberofAvailable(Available);
        garage1.setX_CoordGoogleMap(XCoord);
        garage1.setY_CoordGoogleMap(YCoord);
        reference2.setValue(garage1);
        Intent intent = new Intent(getApplicationContext(), GarageOwnerProfile.class);
        startActivity(intent);


        reference4=FirebaseDatabase.getInstance().getReference("Erknly Database").child("Total Reservations").child("Total");
        Total=Total+1;
        reference4.setValue(Total);


    }

    public void Delete(View view){

        try{

            reference5 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Reservations Information").child(user.getUid());
            reference5.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        DriversList.add(snapshot.getKey());
                    }

                    for(int i=0;i<DriversList.size();i++){

                        reference6 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Reservations Information").child(user.getUid()).child(DriversList.get(Pos));
                        reference6.removeValue();
                        Toast.makeText(getApplicationContext(),"The Reservation Information is Deleted and Stored in History",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), GarageOwnerProfile.class);
                        startActivity(intent);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }
        catch (Exception e){
            return;
        }

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