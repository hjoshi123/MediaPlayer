package com.example.hemantj.mediaplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<SongInfo> songs = null;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private int position;
    int seekValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = (SeekBar) findViewById(R.id.seekbar);

        songs = getIntent().getParcelableArrayListExtra("songsList");
        position = getIntent().getIntExtra("position",0);

        SongInfo songInfo = songs.get(position);

        mediaPlayer = new MediaPlayer();
        try{
            mediaPlayer.setDataSource(songInfo.getSongPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration());
        }catch (IOException e){
            e.printStackTrace();
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekValue);
            }
        });

        SeekThread thread = new SeekThread();
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.all_songs,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.all_songs){
            startActivity(new Intent(this,AllSongs.class));
            return true;
        }

        return false;
    }

    private class SeekThread extends Thread{
        public void run(){
            while(true){
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mediaPlayer != null)
                            seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                });
            }
        }
    }
}
