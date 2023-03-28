package com.example.zadanie3_5;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.os.Environment;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import java.io.IOException;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {


    private final String DATA_STREAM = "https://pub0302.101.ru:8443/stream/pro/aac/64/103";
    private final String DATA_SD = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/music.mp3";
    private String nameAudio = "";

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private Toast toast;
    private TextView textOut;
    private Switch switchLoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textOut = findViewById(R.id.textOut);
        switchLoop = findViewById(R.id.switchLoop);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        switchLoop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(mediaPlayer != null)
                    mediaPlayer.setLooping(isChecked);
            }
        });
    }

    public void onClickSource(View view) {

        releaseMediaPlayer();
        try {
            switch (view.getId()) {
            case R.id.btnStream:
                    toast = Toast.makeText(this, "D̴̢͚̦̓̈́̚A̴͓͉̒͒͜͝T̸͇̝͉̾̈́̐Ä̵̡͓͖́̽̐_̵̻͖̺̒̓S̴͔̙̾͆͌͜T̸̠͍̘́͊̕R̵̞̪͓̓͝͝E̸̘̻͉̓̽͘A̴̡͉̝̔͋̓M̸͔̫͓͌̈́͐ Что-то с трансляцией D̴̢͚̦̓̈́̚A̴͓͉̒͒͜͝T̸͇̝͉̾̈́̐Ä̵̡͓͖́̽̐_̵̻͖̺̒̓S̴͔̙̾͆͌͜T̸̠͍̘́͊̕R̵̞̪͓̓͝͝E̸̘̻͉̓̽͘A̴̡͉̝̔͋̓M̸͔̫͓͌̈́͐", Toast.LENGTH_SHORT);
                    toast.show();
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(DATA_STREAM);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setOnPreparedListener(this);
                    mediaPlayer.prepareAsync();
                    nameAudio = "РАДИО";
                    break;
                case R.id.btnRAW:
                    toast = Toast.makeText(this, "Запущен аудио-файл с памяти телефона", Toast.LENGTH_SHORT);
                    toast.show();
                    mediaPlayer = MediaPlayer.create(this, R.raw.flight_of_the_bumblebee);
                    nameAudio = "Полёт шмеля - Н.А.Римский-Корсаков";
                    break;
                case R.id.btnSD:
                    // код выполнения кнопки btnSD
                    toast = Toast.makeText(this, "Запущен аудио-файл с SD-карты", Toast.LENGTH_SHORT);
                    toast.show();
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(DATA_SD);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    break;
                case R.id.btnBack:
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000);
                    break;
                case R.id.btnForward:
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            toast = Toast.makeText(this, "Источник информации не найден", Toast.LENGTH_SHORT);
            toast.show();
        }

        if (mediaPlayer == null) return;

        mediaPlayer.setLooping(switchLoop.isChecked());
        mediaPlayer.setOnCompletionListener(this);
    }

    public void onClick(View view) {
        if (mediaPlayer == null) return;

        switch (view.getId()) {
            case R.id.btnResume:
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
                break;
            case R.id.btnPause:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                break;
            case R.id.btnStop:
                mediaPlayer.stop();
                break;
            case R.id.btnForward:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000);
                break;
            case R.id.btnBack:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000);
                break;
        }

        textOut.setText(nameAudio + "\n(проигрывание " + mediaPlayer.isPlaying() + ", время " + mediaPlayer.getCurrentPosition()
                + ",\nповтор " + mediaPlayer.isLooping() + ", громкость " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + ")");
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        toast = Toast.makeText(this, "Старт медиа-плейера", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        toast = Toast.makeText(this, "Отключение медиа-плейера", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}