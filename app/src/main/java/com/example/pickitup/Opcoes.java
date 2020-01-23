package com.example.pickitup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Opcoes extends AppCompatActivity {

    ImageButton btnback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcoes);

        btnback = findViewById(R.id.btnopback);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vltmenu();
            }
        });

    }



    private void vltmenu(){
        finish();
    }
}
