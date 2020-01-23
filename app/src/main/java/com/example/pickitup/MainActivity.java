package com.example.pickitup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.imglogo);
        Splash();
    }

    private void Splash(){

        Handler logoh  = new Handler();
        Handler chg  = new Handler();

        logoh.postDelayed(new Runnable() {
            public void run() {
                img.setVisibility(View.VISIBLE);
            }
        }, 1000);
        chg.postDelayed(new Runnable() {
            public void run() {
                change();
            }
        }, 3000);

    }

    private void change(){
        Intent i = new Intent(this, Menu.class);
        startActivity(i);
        finish();
    }
}
