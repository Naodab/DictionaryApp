package com.midterm.testdictionary.viewmodel;

import android.media.MediaPlayer;

import java.io.IOException;

public class AudioService {
    private MediaPlayer mediaPlayer;

    public void playAudio(String audioUrl) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });

            // Stop the audio automatically once it's completed
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopAudio();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    public String getAudio(String url){
        String[] parts = url.split("-");

        String result = parts[1].replace(".mp3", "");

        return (!result.equals("")) ? result.toUpperCase() : "";
    }
}
