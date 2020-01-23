package com.example.pickitup;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Opcoes extends AppCompatActivity {

    private SQLite sqLite;
    private SQLiteDatabase bd;
    private ImageButton btnback;
    private Button btnsave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcoes);

        btnback = findViewById(R.id.btnopback);
        btnsave = findViewById(R.id.btnopsave);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vltmenu();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardasettings();
            }
        });

        loadsettings();

    }

    private void loadsettings(){
        sqLite = new SQLite(this);
        bd = sqLite.getWritableDatabase();
        Cursor condata = bd.query("opcoes",new String[]{"server","user","pass","bdname"},"id=(select max(id) from opcoes)",null,null,null,null);
        condata.moveToFirst();
        EditText user,pass,server,bdname;
        user = findViewById(R.id.etopuser);
        user.setText(condata.getString(1));
        pass = findViewById(R.id.etoppass);
        pass.setText(condata.getString(2));
        server = findViewById(R.id.etopserv);
        server.setText(condata.getString(0));
        bdname = findViewById(R.id.etopbd);
        bdname.setText(condata.getString(3));
    }

    private void guardasettings(){
        EditText etuser,etpass,etserv, etbd;
        String suser,spass,sserv,sbd;

        etuser = findViewById(R.id.etopuser);
        suser = etuser.getText().toString().trim();
        etpass = findViewById(R.id.etoppass);
        spass = etpass.getText().toString().trim();
        etserv = findViewById(R.id.etopserv);
        sserv = etserv.getText().toString().trim();
        etbd = findViewById(R.id.etopbd);
        sbd = etbd.getText().toString().trim();
        //conecta BD
        sqLite = new SQLite(this);
        bd = sqLite.getWritableDatabase();
        String updatestring = "update opcoes set server='"+sserv+"',user='"+suser+"', pass='"+spass+"',bdname='"+sbd+"' where id=1";
        try {
            bd.execSQL(updatestring);
        }catch (Exception ex)
        {
            Toast.makeText(this, "Error: "+ex.toString(), Toast.LENGTH_LONG);
        }finally {
            Toast.makeText(this, R.string.sucess, Toast.LENGTH_LONG).show();
            finish();
        }

    }

    private void vltmenu(){
        finish();
    }
}
