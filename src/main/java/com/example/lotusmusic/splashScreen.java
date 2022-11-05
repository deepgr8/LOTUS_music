package com.example.lotusmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class splashScreen extends AppCompatActivity {
    TextView textView;
    LottieAnimationView lottie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        textView = findViewById(R.id.appname);
        lottie = findViewById(R.id.lottieAnimationView);
        textView.animate().translationY(-700).setDuration(1500).setStartDelay(0);
        Toast.makeText(this, "Developed By Deepu", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        } ,3000);
    }
}