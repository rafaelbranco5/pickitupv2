package com.example.pickitup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLServer extends AppCompatActivity {

    private SQLite sqLite;
    private SQLiteDatabase bd;
    public String server,username,password,bdname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlserver);

        sqLite = new SQLite(this);
        bd = sqLite.getWritableDatabase();
        Cursor condata = bd.query("opcoes",new String[]{"server","user","pass","bdname"},"id=(select max(id) from opcoes)",null,null,null,null);
        condata.moveToFirst();
        server = condata.getString(0);
        username = condata.getString(1);
        password = condata.getString(2);
        bdname = condata.getString(3);

        AlertDialog.Builder conf = new AlertDialog.Builder(this);
        conf.setMessage(R.string.delbd)
                .setTitle(R.string.app_name);

        conf.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                con c = new con();
                c.doInBackground();
            }
        });
        conf.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog ask = conf.create();
        ask.show();

    }

    private void updateuistart(Integer i){
        ProgressBar pb = findViewById(R.id.sqlpb);
        pb.setMax(i);
    }
    private void updateui(Integer i, String s){
        ProgressBar pb = findViewById(R.id.sqlpb);
        TextView tv = findViewById(R.id.sqltxt);
        tv.setText(s);
        pb.setProgress(i);
    }

    private void killclass(){
        finish();
    }

    private class con extends AsyncTask<String, String, Void> {


        String qryimport="select \n" +
                "st.ref, st.design, st.familia, st.stock, \n" +
                "st.epv1, st.epv2, st.epv3, st.epv4, st.epv5, \n" +
                "st.iva1incl,st.iva2incl,st.iva3incl,st.iva4incl,st.iva5incl,taxasiva.taxa,\n" +
                "st.local,st.unidade,st.uni2,st.imagem,st.url,st.peso,st.massaliq\n" +
                "from st left join taxasiva on st.tabiva=taxasiva.codigo\n" +
                "where st.stns=0 and st.inactivo=0";



        @Override
        protected Void doInBackground(String... strings) {
            try{
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);



                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();

                Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://"+server+";DatabaseName="+bdname+";user=" + username + ";password=" + password);

                Statement stmt = DbConn.createStatement();
                ResultSet qryresult = stmt.executeQuery(qryimport);
                ResultSet qrynrows = stmt.executeQuery("select count(ststamp) from st where st.stns=0 and st.inactivo=0");
                updateuistart(qrynrows.getInt(0));
                bd.execSQL("delete from STOCKS where 1=1");
                ProgressBar pb = findViewById(R.id.sqlpb);
                TextView tv = findViewById(R.id.sqltxt);
                Integer x=qrynrows.getInt(0);

                Integer i=0;
                do{
                    Integer temp= i+1;
                    String texto = R.string.sql + temp.toString() + R.string.de + x.toString();
                    updateui(temp, texto);
                    String ins = "INSERT into stocks (ref,design,familia,stock,epv1,epv2,epv3,epv4,epv5,iva1incl,iva2incl,iva3incl,iva4incl,iva5incl,taxa,local,unidade,uni2,imagem,url,peso,massaliq,volume) values (\n" +
                            "'"+ qryresult.getString(0) +"','"+qryresult.getString(1)+"',\n" +
                            "'"+qryresult.getString(2)+"',"+qryresult.getDouble(3)+",\n" +
                            qryresult.getDouble(4)+","+qryresult.getDouble(5)+",\n" +
                            qryresult.getDouble(6)+","+qryresult.getDouble(7)+",\n" +
                            qryresult.getDouble(8)+","+qryresult.getBoolean(9)+",\n" +
                            qryresult.getBoolean(10)+","+qryresult.getBoolean(11)+",\n" +
                            qryresult.getBoolean(12)+","+qryresult.getBoolean(13)+",\n" +
                            qryresult.getDouble(14)+",'"+qryresult.getString(15)+"',\n"+
                            "'"+qryresult.getString(16)+"','"+qryresult.getString(17)+"',\n"+
                            "'"+qryresult.getString(18)+"','"+qryresult.getString(19)+"',\n"+
                            qryresult.getDouble(20)+","+qryresult.getDouble(21)+",0.00)";
                    bd.execSQL(ins);
                    qryresult.next();
                }while(qryresult.next());

            }catch (Exception ex){
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("text lable", ex.toString());
                clipboard.setPrimaryClip(clip);
            }finally {
                killclass();
                return null;
            }
        }
    }





}
