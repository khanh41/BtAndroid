package com.example.contact.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.contact.Contact;

import java.util.ArrayList;
import java.util.List;

public class DBManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "contact_manager";
    private static final String TABLE_NAME = "contact";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PHONE = "phone";
    private static final int VERSION = 1;
    private Context context;
    private String SQLQuery = "CREATE TABLE " + TABLE_NAME + " (" +
            ID + " integer primary key, " +
            NAME + " TEXT, " +
            PHONE + " TEXT)";

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.d("pluto", "dc khong");
    }
    public void addContact(Contact contact){

        SQLiteDatabase db = new DBManager(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME,contact.getmName());
        values.put(PHONE,contact.getmPhone());
        db.insert(TABLE_NAME,null,values);
        db.close();
    }
    public List<Contact> getAllContact(){
        List<Contact> ListContact = new ArrayList<>();

        String selecQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selecQuery,null);
        if(cursor.moveToFirst()){
            do{
                Contact contact = new Contact();
                contact.setmId(cursor.getInt(0));
                contact.setmName(cursor.getString(1));
                contact.setmPhone(cursor.getString(2));
                ListContact.add(contact);
            }while (cursor.moveToNext());
        }
        db.close();
        return ListContact;
    }
    public int updateContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME,contact.getmName());
        values.put(PHONE,contact.getmPhone());
        int number = db.update(TABLE_NAME,values,ID+"=?",new String[]{String.valueOf(contact.getmId())});
        db.close();
        return number;
    }
    public int deleteContact(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,ID+"=?",new String[]{String.valueOf(id)});
    }
}
