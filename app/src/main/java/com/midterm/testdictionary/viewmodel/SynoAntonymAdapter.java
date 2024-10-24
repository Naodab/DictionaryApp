package com.midterm.testdictionary.viewmodel;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.SynonymAntonymItemBinding;
import com.midterm.testdictionary.model.Word;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SynoAntonymAdapter extends RecyclerView.Adapter<SynoAntonymAdapter.ViewHolder> {
    private final List<String> words;
    private WordApiService apiService;

    public SynoAntonymAdapter(List<String> words) {
        this.words = words;
        apiService = new WordApiService();
    }

    @NonNull
    @Override
    public SynoAntonymAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SynonymAntonymItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.synonym_antonym_item,
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SynoAntonymAdapter.ViewHolder holder, int position) {
        String word = words.get(position);
        holder.bind(word);
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public SynonymAntonymItemBinding binding;

        public ViewHolder(SynonymAntonymItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // TODO: add event bundle to another detail word
            binding.synonymAntonymLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String word = words.get(getAdapterPosition());
                    performSearch(word, view);
                }
            });
        }

        public void bind(String word) {
            binding.setWord(word);
            binding.executePendingBindings();
        }

        public Word performSearch(String word, View view) {
            final Word[] result = {null};
            DisposableSingleObserver<List<Word>> disposableSingleObserver = apiService.getWordDefinition(word)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<Word>>() {
                        @Override
                        public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<Word> words) {
                            Word searchedWord = words.get(0);
                            Log.d("DEBUG", "Success " + searchedWord.getWord());

                            if (searchedWord != null) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("word", searchedWord);
                                if (view != null) {
                                    Navigation.findNavController(view).navigate(R.id.detailFragment, bundle);
                                }
                            }
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            Log.d("DEBUG", "Fail" + e.getMessage());
                            Toast.makeText(view.getContext(), "Sorry, we've not updated this word yet.", Toast.LENGTH_SHORT).show();
                        }
                    });
            return result[0];
        }
    }
}
