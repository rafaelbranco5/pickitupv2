package com.example.pickitup;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

public class SQLite extends SQLiteOpenHelper{

    private static final String DB_NAME = "PIU";
    private static final int VERSION = 3;


    private static final String qrycreatest="create table stocks (\n" +
            "ref varchar(18) NOT NULL, design varchar(60) NOT NULL, familia varchar(18),\n" +
            "stock numeric(13, 3), epv1 numeric(19, 6), epv2 numeric(19, 6),\n" +
            "epv3 numeric(19, 6), epv4 numeric(19, 6), epv5 numeric(19, 6),\n" +
            "iva1incl bit, iva2incl bit, iva3incl bit,\n" +
            "iva4incl bit, iva5incl bit, taxa numeric(5, 2),\n" +
            "local varchar(20), unidade varchar(4), uni2 varchar(4),\n" +
            "imagem varchar(120), url varchar(100), peso numeric(11, 3),\n" +
            "massaliq numeric(18, 3), volume numeric(11, 3),\n" +
            "PRIMARY KEY (ref)\n" +
            ");";

    private static final String qrycreatesettings="create table opcoes (\n" +
            "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    server varchar(50),\n" +
            "    user varchar(30),\n" +
            "    bdname varchar(30),\n" +
            "    pass varchar(30));\n";


    private static final String qryimport="select \n" +
            "st.ref, st.design, st.familia, st.stock, \n" +
            "st.epv1, st.epv2, st.epv3, st.epv4, st.epv5, \n" +
            "st.iva1incl,st.iva2incl,st.iva3incl,st.iva4incl,st.iva5incl,taxasiva.taxa,\n" +
            "st.local,st.unidade,st.uni2,st.imagem,st.url,st.peso,st.massaliq\n" +
            "from st left join taxasiva on st.tabiva=taxasiva.codigo\n" +
            "where st.stns=0 and st.inactivo=0";

    private static final String qryupgrade1="DROP TABLE IF EXISTS STOCKS;";
    private static final String qryupgrade2="DROP TABLE IF EXISTS OPCOES;";

    //Default Contructor
    public SQLite(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(qrycreatest);
        db.execSQL(qrycreatesettings);
        insop(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(qryupgrade1);
        db.execSQL(qryupgrade2);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }


    private void insop(SQLiteDatabase bd){
        String qrycriasettings = "insert into opcoes (server,user,pass,bdname) values ('','','','');";
        bd.execSQL(qrycriasettings);
    }

    public void funcupdate(){

    }

    public void funcimport(){

    }
}
