package com.example.musicplayassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextView playerPosition, playerDuration;
    SeekBar seekBar;
    ImageView playButton, pauseButton, rewindButton, forwardButton;

    MediaPlayer  mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerPosition = findViewById(R.id.playerPosition);
        playerDuration = findViewById(R.id.playerDuration);

        seekBar = findViewById(R.id.seekbar);

        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        rewindButton = findViewById(R.id.rewindButton);
        forwardButton = findViewById(R.id.forwardButton);


        // init media player
        mediaPlayer = MediaPlayer.create(this, R.raw.pasoori);

        runnable =  new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);

            }
        };

        int duration = mediaPlayer.getDuration();

        String sDuration = convertFormat(duration);

        playerDuration.setText(sDuration);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);

                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                handler.postDelayed(runnable, 0);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.GONE);

                mediaPlayer.pause();

                handler.removeCallbacks(runnable);
            }
        });

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mediaPlayer.getCurrentPosition();

                int duration = mediaPlayer.getDuration();

                if (mediaPlayer.isPlaying() && duration != currentPosition){
                    currentPosition = currentPosition +5000;
                    playerPosition.setText(convertFormat(currentPosition));
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });

        rewindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mediaPlayer.getCurrentPosition();

                if (mediaPlayer.isPlaying() && currentPosition > 5000){
                    currentPosition = currentPosition - 5000;
                    playerPosition.setText(convertFormat(currentPosition));
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mediaPlayer.seekTo(progress);
                }

                playerPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }


        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                pauseButton.setVisibility(View.GONE);
                playButton.setVisibility(View.VISIBLE);
                mediaPlayer.seekTo(0);
            }
        });

    }

    private String convertFormat(int duration){
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration),
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toSeconds(duration)));
    }
}