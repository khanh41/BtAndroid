package com.example.contact;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contact.Adapter.CustomAdapter;
import com.example.contact.database.DBManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    FloatingActionButton fab;
    public static final int MY_REQUEST_CODE = 100;
    SearchView text_search;
    private CustomAdapter customAdapter;
    private List<Contact> listContact;
    private ListView lvContact;

    DBManager dbManager = new DBManager(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Widget();
        setAdapter();
    }
    public void Widget(){
        listContact = dbManager.getAllContact();
        lvContact = findViewById(R.id.list_View);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = listContact.get(position);
                editContact(contact);
            }
        });
        lvContact.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = listContact.get(position);
                Log.d("pluto",contact.getmName());
                int result = dbManager.deleteContact(contact.getmId());
                if(result>0){
                    Toast.makeText(MainActivity.this, "Delete success", Toast.LENGTH_SHORT).show();
                    listContact.clear();
                    listContact.addAll(dbManager.getAllContact());
                    customAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(MainActivity.this, "Delete fail", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        Contact contact = (Contact) data.getSerializableExtra("CONTACT");
        Log.d("pluto",contact.getmName()+" "+contact.getmId());
        if(requestCode== MY_REQUEST_CODE && resultCode==Activity.RESULT_OK){
                int temp = dbManager.updateContact(contact);
                listContact.clear();
                listContact.addAll(dbManager.getAllContact());
                customAdapter.notifyDataSetChanged();
        }
        if(requestCode== MY_REQUEST_CODE && resultCode==Activity.RESULT_CANCELED) {

            if (contact != null) {
                dbManager.addContact(contact);
                listContact.clear();
                listContact.addAll(dbManager.getAllContact());
                setAdapter();
            }
        }
    }

       private void setAdapter(){
        if(customAdapter==null){
            customAdapter = new CustomAdapter(this,R.layout.contact,listContact);
            lvContact.setAdapter(customAdapter);
        }else{
            customAdapter.notifyDataSetChanged();
            lvContact.setSelection(customAdapter.getCount()-1);
        }
       }
       public void editContact(Contact contact){
           Intent intentEdit = new Intent(this,EditContactActivity.class);
                intentEdit.putExtra("CONTACT",contact);
                this.startActivityForResult(intentEdit, MY_REQUEST_CODE);
       }
       @Override
       public void onClick(View view) {
        Intent intentAdd = new Intent(this,AddContactActivity.class);
        this.startActivityForResult(intentAdd, MY_REQUEST_CODE);
    }
}