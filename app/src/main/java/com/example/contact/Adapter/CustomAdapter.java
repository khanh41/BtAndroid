package com.example.contact.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.contact.Contact;
import com.example.contact.R;

import java.util.List;
import java.util.Locale;

public class CustomAdapter extends ArrayAdapter<Contact> {
    private  Context context;
    private  int resource;
    private List<Contact> listContact;
    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<Contact> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.listContact = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.contact,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tvId = convertView.findViewById(R.id.textId);
            viewHolder.tvName = convertView.findViewById(R.id.textName);
            viewHolder.tvPhone = convertView.findViewById(R.id.textPhone);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Contact contact = listContact.get(position);
        viewHolder.tvId.setText(contact.getmId()+"");
        viewHolder.tvName.setText(contact.getmName());
        viewHolder.tvPhone.setText(contact.getmPhone());
        return convertView;
    }

    public class ViewHolder{
        private TextView tvId;
        private TextView tvName;
        private TextView tvPhone;

    }
}
