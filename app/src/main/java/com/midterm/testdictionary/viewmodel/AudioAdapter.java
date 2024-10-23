package com.midterm.testdictionary.viewmodel;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.AudioItemBinding;
import com.midterm.testdictionary.databinding.MeaningItemBinding;
import com.midterm.testdictionary.model.Phonetic;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder>  {
    private ArrayList<Phonetic> audiosList;
    private MediaPlayer mediaPlayer;

    public AudioAdapter(ArrayList<Phonetic> audiosList){
        this.audiosList = audiosList;
    }
    @NonNull
    @Override
    public AudioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AudioItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.audio_item,
                parent,
                false);
        View viewRoot = binding.getRoot();
        return new AudioAdapter.ViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull AudioAdapter.ViewHolder holder, int position) {
        holder.binding.setPhonetic(audiosList.get(position));
        holder.binding.executePendingBindings();

        holder.binding.tvVolume.setText(getAudio(audiosList.get(position).getAudio()));

        holder.binding.btnVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("DEBUG", (String) holder.binding.btnVolume.getTag());
//                playAudio(holder.binding.btnVolume.getTag().toString());
                if (holder.binding.btnVolume.getTag() != null) {
                    String tag = holder.binding.btnVolume.getTag().toString();
                    Log.d("DEBUG", tag);
                    playAudio(tag);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return this.audiosList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public AudioItemBinding binding;

        public ViewHolder(AudioItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    private void playAudio(String audioUrl) {
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
    private String getAudio(String url){
        String[] parts = url.split("-");

        String result = parts[1].replace(".mp3", "");

        return (!result.equals("")) ? result.toUpperCase() : "";
    }
}
