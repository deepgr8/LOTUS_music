package com.example.lotusmusic;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class customadapter extends ArrayAdapter {

    private String[] arr;
    public customadapter(@NonNull Context context, int resource, @NonNull String[] arr){
        super(context, resource, arr);
        this.arr = arr;
    }

    @Nullable
    @Override
    public String getItem(int position){
        return arr[position];
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.customlayout,parent, false);
        TextView t =convertView.findViewById(R.id.songview);
        t.setText(getItem(position));
        return convertView;
    }
}