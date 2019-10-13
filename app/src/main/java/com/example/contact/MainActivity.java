package com.example.contact;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import android.text.TextWatcher;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.contact.Adapter.CustomAdapter;
import com.example.contact.database.DBManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity implements View.OnClickListener {
    FloatingActionButton fab;
    public static final int MY_REQUEST_CODE = 100;
    SearchView text_search;
    private CustomAdapter customAdapter;
    private List<Contact> listContact;
    private ListView lvContact;
    DBManager dbManager = new DBManager(this);
    int positionClone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Widget();
        setAdapter();
    }

    public void Widget() {

        listContact = dbManager.getAllContact();
        lvContact = findViewById(R.id.list_View);
        text_search = findViewById(R.id.text_search);
        text_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                String newText = text.toLowerCase(Locale.getDefault());
                listContact.clear();
                if (newText.length() == 0) {
                    listContact.addAll(dbManager.getAllContact());
                } else {
                    for (Contact contact : dbManager.getAllContact()) {
                        if (contact.getmName().toString().toLowerCase(Locale.getDefault()).contains(newText)) {
                            listContact.add(contact);
                        }
                    }
                }

                customAdapter.notifyDataSetChanged();
                return false;
            }
        });
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionClone = position;
                show();
                Contact contact = listContact.get(position);
                editContact(contact);
            }
        });
        lvContact.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = listContact.get(position);
                int result = dbManager.deleteContact(contact.getmId());
                if (result > 0) {
                    Toast.makeText(MainActivity.this, "Delete success", Toast.LENGTH_SHORT).show();
                    listContact.clear();
                    listContact.addAll(dbManager.getAllContact());
                    customAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Delete fail", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Contact contact = (Contact) data.getSerializableExtra("CONTACT");
        if (requestCode == MY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            int temp = dbManager.updateContact(contact);
            listContact.clear();
            listContact.addAll(dbManager.getAllContact());
            customAdapter.notifyDataSetChanged();
        }
        if (requestCode == MY_REQUEST_CODE && resultCode == Activity.RESULT_CANCELED) {

            if (contact != null) {
                dbManager.addContact(contact);
                listContact.clear();
                listContact.addAll(dbManager.getAllContact());
                setAdapter();
            }
        }

    }

    private void setAdapter() {
        if (customAdapter == null) {
            customAdapter = new CustomAdapter(this, R.layout.contact, listContact);
            lvContact.setAdapter(customAdapter);
        } else {
            customAdapter.notifyDataSetChanged();
            lvContact.setSelection(customAdapter.getCount() - 1);
        }
    }

    public void editContact(Contact contact) {
        Intent intentEdit = new Intent(this, EditContactActivity.class);
        intentEdit.putExtra("CONTACT", contact);
        this.startActivityForResult(intentEdit, MY_REQUEST_CODE);
    }
;
    @Override
    public void onClick(View view) {
        Intent intentAdd = new Intent(this, AddContactActivity.class);
        this.startActivityForResult(intentAdd, MY_REQUEST_CODE);
    }
    void show(){
        ImageView button = findViewById(R.id.btn_call);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestPemissions(positionClone);
            }
        });
    }
    private void checkAndRequestPemissions(int position) {
        String dial = "tel:" + listContact.get(position).getmPhone();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(dial));
        String[] permissions = new String[]{Manifest.permission.CALL_PHONE};
        List<String> listPermissionNeeded = new ArrayList<>();
        for (String permission : listPermissionNeeded) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                ;
            listPermissionNeeded.add(permission);
        }
        if (!listPermissionNeeded.isEmpty())
            ActivityCompat.requestPermissions(this, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]), 1);
        startActivity(intent);
    }
}