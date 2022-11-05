package com.example.lotusmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chibde.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;

public class Playsong extends AppCompatActivity {
    TextView songname,start,end;
    ImageView next,back,play,pause,icon;
    SeekBar seekmusic;
    BarVisualizer visualizer;
    String sname;
    public static  final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    Thread updateseekbar;
    ArrayList<File> mysongs;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()== android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playsong);
        getSupportActionBar().hide();
        songname = findViewById(R.id.songname);
        start = findViewById(R.id.txtstart);
        end = findViewById(R.id.txtend);
        next = findViewById(R.id.next);
        back = findViewById(R.id.back);
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        seekmusic = findViewById(R.id.seekBar);
        icon =findViewById(R.id.iconn);
        play.setVisibility(View.INVISIBLE);


        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mysongs = (ArrayList) bundle.getParcelableArrayList("songs");
        String songName = i.getStringExtra("songname");
        position = bundle.getInt("pos",0);
        songname.setSelected(true);
        Uri uri = Uri.parse(mysongs.get(position).toString());
        sname = mysongs.get(position).getName();
        songname.setText(sname);
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        updateseekbar=new Thread(){
            @Override
            public void run() {
                int totalduration = mediaPlayer.getDuration();
                int currentpos = 0;
                while (currentpos<totalduration){
                    try {
                        sleep(500);
                        currentpos = mediaPlayer.getCurrentPosition();
                        seekmusic.setProgress(currentpos);
                    }
                    catch (InterruptedException | IllegalStateException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        seekmusic.setMax(mediaPlayer.getDuration());
        updateseekbar.start();
        seekmusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });
        String endtime  = createtime(mediaPlayer.getDuration());
        end.setText(endtime);
        final Handler handler = new Handler();
        final int delay = 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currenttime = createtime(mediaPlayer.getCurrentPosition());
                start.setText(currenttime);
                handler.postDelayed(this,delay);
            }
        },delay);

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    pause.setVisibility(View.INVISIBLE);
                    mediaPlayer.pause();
                    play.setVisibility(View.VISIBLE);
                }

            }

        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.setVisibility(View.INVISIBLE);
                mediaPlayer.start();
                pause.setVisibility(View.VISIBLE);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next.performClick();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,          Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(500);
                rotate.setInterpolator(new LinearInterpolator());
                icon.startAnimation(rotate);
                mediaPlayer.stop();;
                mediaPlayer.release();
                position = (position+1)%mysongs.size();
                Uri u = Uri.parse(mysongs.get(position).toString());
                mediaPlayer =MediaPlayer.create(getApplicationContext(),u);
                sname = mysongs.get(position).getName();
                songname.setText(sname);
                mediaPlayer.start();
                pause.setVisibility(View.VISIBLE);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RotateAnimation rotate = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f,          Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(500);
                rotate.setInterpolator(new LinearInterpolator());
                icon.startAnimation(rotate);
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position-1)<0)?(mysongs.size()-1):(position-1);
                Uri u = Uri.parse(mysongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sname = mysongs.get(position).getName();
                songname.setText(sname);
                mediaPlayer.start();
                pause.setVisibility(View.VISIBLE);
            }
        });

    }
    public String createtime(int duration){
        String time="";
        int min = duration/1000/60;
        int sec = duration/1000%60;
        time += min+":";
        if (sec<10){
            time += "0";
        }
        time+=sec;
        return time;
    }
}