package com.example.pickitup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Menu extends AppCompatActivity {
    ImageButton btnop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnop= findViewById(R.id.btnsettings);

        btnop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abreop();
            }
        });
    }



    private void abreop(){
        Intent i = new Intent(this, Opcoes.class);
        startActivity(i);
    }
}
