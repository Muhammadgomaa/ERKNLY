package com.example.erknly;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);

        Button changeLang = (Button)findViewById(R.id.changeMyLang);
        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show AlertDialog to display list of languages, one can be selected
                showChangeLanguageDialog();

            }
        });


    }

    private void showChangeLanguageDialog() {
        final String[] listItems = {"عربي", "English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle("Choose Language...");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0)
                {   //Arabic
                    setLocale("ar");
                    recreate();
                }
                    //English
                else if (i == 1)
                {
                    setLocale("en");
                    recreate();
                }
                //dismiss Alert dialog when language selected
                dialogInterface.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        //show Alert Dialog
        mDialog.show();

    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        // save data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();

    }
    //load language saved in shared preferences
    public void loadLocale(){
        SharedPreferences prefd = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefd.getString("My_Lang", "");
        setLocale(language);
    }

    public void Register(View view){

        Intent intent=new Intent(this,Main2Activity.class);
        startActivity(intent);

    }

    public void Login(View view){

        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);

    }


}
