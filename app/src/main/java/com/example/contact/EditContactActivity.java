package com.example.contact;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditContactActivity extends AppCompatActivity {
    Button btn_done,btn_cancel;
    EditText editName,editNumber;
    int resultCode=RESULT_OK;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        Intent intent = this.getIntent();
        editName = (EditText) findViewById(R.id.text_name);
        editNumber = (EditText) findViewById(R.id.text_number);

        Contact contact = (Contact) intent.getSerializableExtra("CONTACT");
        editName.setText(contact.getmName());
        editNumber.setText(contact.getmPhone());
        id = contact.getmId();

        btn_done = (Button) findViewById(R.id.btn_done);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backClicked(v);
            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
        }
    });

    }
    @Override
    public void finish(){
        Intent data = new Intent();
        Contact contact = new Contact(id,editName.getText().toString(),editNumber.getText().toString());
        data.putExtra("CONTACT",contact);
        this.setResult(resultCode,data);
        super.finish();
    }
    public void backClicked(View view) {
        this.onBackPressed();
    }
}
