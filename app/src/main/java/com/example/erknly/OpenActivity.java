package com.example.erknly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class OpenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);

        Thread thread=new Thread(){

            @Override
            public void run() {

                try {

                    sleep(3000);
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();


                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    }
}
