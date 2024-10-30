package com.midterm.testdictionary.viewmodel;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.SearchedWordItemBinding;
import com.midterm.testdictionary.model.Meaning;
import com.midterm.testdictionary.model.ObjectBox;
import com.midterm.testdictionary.model.Word;
import com.midterm.testdictionary.model.WordObjectBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;

public class SearchedWordAdapter extends RecyclerView.Adapter<SearchedWordAdapter.ViewHolder> {
    private ArrayList<WordObjectBox> wordObjectBoxesList;
    private WordObjectBoxService wordObjectBoxService;
    private AudioService audioService;

    public SearchedWordAdapter(ArrayList<WordObjectBox> wordObjectBoxesList){
        this.wordObjectBoxesList = wordObjectBoxesList;

        wordObjectBoxService = new WordObjectBoxService();

        audioService = new AudioService();
    }
    @NonNull
    @Override
    public SearchedWordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchedWordItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.searched_word_item,
                parent,
                false);
        View viewRoot = binding.getRoot();

        return new ViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull SearchedWordAdapter.ViewHolder holder, int position) {
        WordObjectBox word = wordObjectBoxesList.get(position);
        holder.bind(word);

        holder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wordObjectBoxService.deleteWordBox(wordObjectBoxesList.get(holder.getAdapterPosition()))){
                    Log.d("DEBUG", wordObjectBoxesList.get(holder.getAdapterPosition()).getId() + " " + wordObjectBoxesList.get(holder.getAdapterPosition()).getWord());
                    wordObjectBoxesList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                }
            }
        });

        holder.binding.listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!wordObjectBoxesList.get(holder.getAdapterPosition()).getAudio().equals("")){
                    Log.d("DEBUG", wordObjectBoxesList.get(holder.getAdapterPosition()).getAudio());
                    audioService.playAudio(wordObjectBoxesList.get(holder.getAdapterPosition()).getAudio());
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return this.wordObjectBoxesList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public SearchedWordItemBinding binding;

        public ViewHolder(SearchedWordItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(WordObjectBox word) {
            binding.setWord(word);
            binding.executePendingBindings();
        }
    }
}
