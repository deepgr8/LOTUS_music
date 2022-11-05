package com.example.lotusmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listview);
        Permission();

    }
    public void Permission(){
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        showsong();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
    }

    public ArrayList<File> findsong(File file) {
        ArrayList arrayList = new ArrayList();
        File [] files = file.listFiles();
        if(files != null) {
            for (File singlefile : files) {
                if (singlefile.isDirectory() && !singlefile.isHidden()) {
                    arrayList.addAll(findsong(singlefile));
                } else {
                    if (singlefile.getName().endsWith(".wav")) {
                        arrayList.add(singlefile);
                    } else if (singlefile.getName().endsWith(".mp3")) {
                        arrayList.add(singlefile);
                    }
                }

            }
        }
        return arrayList;
    }
    void showsong() {
        final ArrayList<File> mysongs = findsong(Environment.getExternalStorageDirectory());
        String[] items = new String[mysongs.size()];
        for (int i = 0; i < mysongs.size(); i++) {
            items[i] = mysongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }
//        ArrayAdapter cus = new ArrayAdapter(this, android.R.layout.simple_list_item_1,items);
        customadapter cus = new customadapter(this,R.layout.customlayout,items);
        listView.setAdapter(cus);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                String songname= (String) listView.getItemAtPosition(i);
                Intent dp = new Intent(getApplicationContext(),Playsong.class)
                        .putExtra("songs",mysongs)
                        .putExtra("songname",songname)
                        .putExtra("pos",i);
                startActivity(dp);
            }
        });
    }
//    public void play(View view){
//        Toast.makeText(this, "Hello Developer Button is clicked", Toast.LENGTH_SHORT).show();
//    }
}