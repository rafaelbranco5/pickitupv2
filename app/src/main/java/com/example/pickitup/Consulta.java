package com.example.pickitup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import android.widget.Toast;

public class Consulta extends AppCompatActivity {

    private SurfaceView scanner;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private String barcodeData;
    EditText ref, quantidade, nova_q;
    Button actualiza;
    private SQLite sqLite;
    private SQLiteDatabase bd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);


        sqLite = new SQLite(this);
        bd = sqLite.getWritableDatabase();
        scanner = findViewById(R.id.surfaceView);
        ref = findViewById(R.id.etxt_ref);
        actualiza = findViewById(R.id.btn_update);
        nova_q = findViewById(R.id.etxt_nova_q);
        detetarCodigo();

        actualiza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bd.execSQL("UPDATE stocks SET stock="+ nova_q.getText() +" WHERE ref LIKE '%" + ref.toString() +"%';");
                    Toast.makeText(getApplicationContext(), "Valor actualizado com sucesso.", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Ocurreu um erro." + e, Toast.LENGTH_SHORT).show();
                }finally {
                    volta_menu();
                }

            }
        });
    }
    private void load(String s){
        sqLite = new SQLite(this);
        bd = sqLite.getWritableDatabase();
        Cursor cursor=bd.rawQuery("select stock from stocks where ref like '%?%'", new String[]{"s"});
        Double d = cursor.getDouble(0);
        quantidade.setText(d+"");
    }

    private void volta_menu(){
        Intent i = new Intent(this, Menu.class);
        startActivity(i);
    }

    private void detetarCodigo(){
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(1920, 1080).setAutoFocusEnabled(true).build();
        scanner.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try{
                    if (ActivityCompat.checkSelfPermission(Consulta.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        cameraSource.start(scanner.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(Consulta.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Ocorreu um erro. ID: "+ e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0){
                    ref.post(new Runnable() {
                        @Override
                        public void run() {
                            if (barcodes.valueAt(0).email != null){
                                ref.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                                ref.setText(barcodeData);
                            } else {
                                barcodeData = barcodes.valueAt(0).displayValue;
                                ref.setText(barcodeData);
                               // load(ref.toString());


                            /*    Cursor con = bd.rawQuery("SELECT stock FROM stocks WHERE ref like '%?%'", new String[]{ref.toString()});
                                con.moveToFirst();
                                String teste = con.getString(1);
                                quantidade.setText(teste); */
                            }
                        }
                    });
 /*                   quantidade.post(new Runnable() {
                        @Override
                        public void run() {
                            if (barcodes.valueAt(0).email != null){
                                quantidade.removeCallbacks(null);
                                Cursor con = bd.query("stocks", new String[]{"ref", "stock"}, "ref="+ref, null, null, null, null);
                                con.moveToFirst();
                                barcodeData = con.getString(1);
                                quantidade.setText(barcodeData);
                            } else {
                                Cursor con = bd.query("stocks", new String[]{"ref", "stock"}, "ref="+ref.getText(), null, null, null, null);
                                con.moveToFirst();
                                String teste = con.getString(1);
                                quantidade.setText(teste);
                            }
                        }
                    }); */
                }
            }
        });
    }
}
