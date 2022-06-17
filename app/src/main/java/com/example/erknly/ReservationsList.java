package com.example.erknly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import android.widget.ListView;
import android.widget.TextView;
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

import java.util.ArrayList;

public class ReservationsList extends AppCompatActivity {

    DatabaseReference reference,reference1,reference2,reference3,reference4;
    FirebaseAuth auth;
    FirebaseUser user;
    GoogleSignInClient mGoogleSignInClient;
    ListView reservationslist;
    MyAdapter adapter;
    ArrayList<String> CarDriversNameList = new ArrayList<>();
    ArrayList<String> CarNumbersList = new ArrayList<>();
    ArrayList<String> CarColorsList = new ArrayList<>();
    ArrayList<String> CarTypesList = new ArrayList<>();
    ArrayList<String> SectionsList = new ArrayList<>();
    ArrayList<String> SlotsList = new ArrayList<>();
    ArrayList<String> DateList = new ArrayList<>();
    ArrayList<String> TimeList = new ArrayList<>();
    ArrayList<String> EmailsList = new ArrayList<>();
    ArrayList<String> DriversList = new ArrayList<>();
    int Position,Available,Slots,Total;
    Double Rate,XCoord,YCoord;
    String Name,Address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations_list);


        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        reference = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Reservations Information").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    DriversList.add(snapshot.getKey());
                }

                for(int i=0;i<DriversList.size();i++){
                    reference1 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Reservations Information").child(user.getUid()).child(DriversList.get(i));
                    reference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            CarDriversNameList.add(dataSnapshot.child("name").getValue().toString());
                            CarNumbersList.add(dataSnapshot.child("carNumber").getValue().toString());
                            CarColorsList.add(dataSnapshot.child("carClor").getValue().toString());
                            CarTypesList.add(dataSnapshot.child("carType").getValue().toString());
                            SectionsList.add(dataSnapshot.child("section").getValue().toString());
                            SlotsList.add(dataSnapshot.child("slotNumber").getValue().toString());
                            DateList.add(dataSnapshot.child("date").getValue().toString());
                            TimeList.add(dataSnapshot.child("time").getValue().toString());

                            reservationslist=findViewById(R.id.list);
                            adapter=new MyAdapter(CarDriversNameList,CarNumbersList,CarColorsList,CarTypesList,SectionsList,SlotsList,DateList,TimeList);
                            reservationslist.setAdapter(adapter);

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

        reference2 = FirebaseDatabase.getInstance().getReference("Erknly Database").child("Garage Information").child(user.getUid());
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Rate= Double.parseDouble(dataSnapshot.child("ratePerHour").getValue().toString());
                Available=Integer.parseInt(dataSnapshot.child("numberofAvailable").getValue().toString());
                Address=dataSnapshot.child("address").getValue().toString();
                Name=dataSnapshot.child("garageName").getValue().toString();
                Slots= Integer.parseInt(dataSnapshot.child("numberofSlots").getValue().toString());
                XCoord= Double.valueOf(dataSnapshot.child("x_CoordGoogleMap").getValue().toString());
                YCoord= Double.valueOf(dataSnapshot.child("y_CoordGoogleMap").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference3= FirebaseDatabase.getInstance().getReference("Erknly Database").child("Total Reservations");
        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Total= Integer.parseInt(dataSnapshot.child("Total").getValue().toString());

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
                    reference4=FirebaseDatabase.getInstance().getReference("Erknly Database").child("Car Driver Information").child(DriversList.get(i));
                    reference4.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            EmailsList.add(dataSnapshot.child("email").getValue().toString());

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

    class MyAdapter extends BaseAdapter {

        ArrayList<String> DriverName;
        ArrayList<String> CarNumber;
        ArrayList<String> CarColor;
        ArrayList<String> CarType;
        ArrayList<String> Section;
        ArrayList<String> SlotNumber;
        ArrayList<String> Date;
        ArrayList<String> Time;



        MyAdapter(ArrayList<String>Name,ArrayList<String>Number,ArrayList<String>Color,ArrayList<String>Type,ArrayList<String>Section,ArrayList<String>Slot,ArrayList<String>Date,ArrayList<String>Time){
            this.DriverName=Name;
            this.CarNumber=Number;
            this.CarColor=Color;
            this.CarType=Type;
            this.Section=Section;
            this.SlotNumber=Slot;
            this.Date=Date;
            this.Time=Time;
        }


        @Override
        public int getCount() {
            return DriverName.size();
        }

        @Override
        public Object getItem(int position) {
            return DriverName.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater layoutInflater=getLayoutInflater();

            View view=layoutInflater.inflate(R.layout.row1,parent,false);

            TextView drivername=view.findViewById(R.id.name);
            TextView carnumber=view.findViewById(R.id.carnumber);
            TextView carcolor=view.findViewById(R.id.carcolor);
            TextView cartype=view.findViewById(R.id.cartype);
            TextView section=view.findViewById(R.id.section);
            TextView slotnumber=view.findViewById(R.id.slot);
            TextView date=view.findViewById(R.id.date);
            TextView time=view.findViewById(R.id.time);

            drivername.setText(DriverName.get(position));
            carnumber.setText(CarNumber.get(position));
            carcolor.setText(CarColor.get(position));
            cartype.setText(CarType.get(position));
            section.setText(Section.get(position));
            slotnumber.setText(SlotNumber.get(position));
            date.setText(Date.get(position));
            time.setText(Time.get(position));

            Button button=view.findViewById(R.id.calculate);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Position=position;
                    Intent intent=new Intent(getApplicationContext(),CollectFeesGarageOwner.class);
                    intent.putExtra("Pos",Position);
                    intent.putExtra("Available",Available);
                    intent.putExtra("Total",Total);
                    intent.putExtra("Name",Name);
                    intent.putExtra("Address",Address);
                    intent.putExtra("Rate",Rate);
                    intent.putExtra("Slots",Slots);
                    intent.putExtra("XCoord",XCoord);
                    intent.putExtra("YCoord",YCoord);
                    intent.putExtra("DriverName",CarDriversNameList.get(Position));
                    intent.putExtra("CarColor",CarColorsList.get(Position));
                    intent.putExtra("CarType",CarTypesList.get(Position));
                    intent.putExtra("CarNumber",CarNumbersList.get(Position));
                    intent.putExtra("Section",SectionsList.get(Position));
                    intent.putExtra("SlotNumber",SlotsList.get(Position));
                    intent.putExtra("Email",EmailsList.get(Position));
                    startActivity(intent);
                }
            });

            Button button1=view.findViewById(R.id.check);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Position=position;
                    Intent intent=new Intent(getApplicationContext(),CheckReservation.class);
                    intent.putExtra("Pos",Position);
                    intent.putExtra("Available",Available);
                    intent.putExtra("Name",Name);
                    intent.putExtra("Address",Address);
                    intent.putExtra("Rate",Rate);
                    intent.putExtra("Slots",Slots);
                    intent.putExtra("XCoord",XCoord);
                    intent.putExtra("YCoord",YCoord);
                    startActivity(intent);
                }
            });

            return view;
        }

    }

}