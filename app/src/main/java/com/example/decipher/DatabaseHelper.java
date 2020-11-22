package com.example.decipher;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    DatabaseHelper(Context context){
        super(context,"Decipher.db", null,1);
    }
    public void onCreate (SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE USER(EMAIL TEXT PRIMARY KEY, Password TEXT,FirstName TEXT, LastName TEXT, NativeLanguage TEXT, OCR INTEGER,RegTrans INTEGER );");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS USER");
        onCreate(sqLiteDatabase);
    }
    public boolean registration(String email,String password,String firstName,String lastName,String nativeLanguage){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("EMAIL", email);
        cv.put("Password", password);
        cv.put("FirstName", firstName);
        cv.put("LastName", lastName);
        cv.put("NativeLanguage",nativeLanguage);
        cv.put("OCR",0);
        cv.put("RegTrans",0);
        long status = db.insert("USER",null,cv);
        if (status == -1)
            return false;
        else
            return true;
    }
    public boolean authenticator(String email,String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT EMAIL,Password FROM USER where EMAIL='"+email+"'AND Password='"+password+"';", null);
        if(cr.getCount()==1){
            return true;
        }
        return false;
    }
    public boolean checker(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT EMAIL,Password FROM USER where EMAIL='"+email+"';", null);
        if(cr.getCount()==1){
            return true;
        }
        return false;
    }
    public String retrieveName(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT FirstName FROM USER where EMAIL='"+email+"';", null);
        cr.moveToFirst();
        return cr.getString(cr.getColumnIndex("FirstName"));
    }
    public String profileName(String email){
        String name;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT FirstName,LastName FROM USER where EMAIL='"+email+"';", null);
        cr.moveToFirst();
        name = cr.getString(cr.getColumnIndex("FirstName"))+" "+cr.getString(cr.getColumnIndex("LastName"));
        return name;
    }
    public String getNative(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT NativeLanguage FROM USER where EMAIL='"+email+"';", null);
        cr.moveToFirst();
        return cr.getString(cr.getColumnIndex("NativeLanguage"));
    }
    public void regupdater(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE USER SET RegTrans = RegTrans+1 WHERE EMAIL='"+email+"';");

    }
    public void ocrupdater(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE USER SET OCR = OCR+1 WHERE EMAIL='"+email+"';");
    }
    public String regGetter(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT RegTrans FROM USER where EMAIL='"+email+"';", null);
        cr.moveToFirst();
        return cr.getString(cr.getColumnIndex("RegTrans"));
    }
    public String ocrGetter(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT OCR FROM USER where EMAIL='"+email+"';", null);
        cr.moveToFirst();
        return cr.getString(cr.getColumnIndex("OCR"));
    }

}
