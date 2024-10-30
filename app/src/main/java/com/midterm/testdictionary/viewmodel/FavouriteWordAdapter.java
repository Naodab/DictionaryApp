package com.midterm.testdictionary.viewmodel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FavouriteWordItemBinding;
import com.midterm.testdictionary.model.FavouriteWord;
import com.midterm.testdictionary.model.Word;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavouriteWordAdapter extends RecyclerView.Adapter<FavouriteWordAdapter.ViewHolder> {
    List<FavouriteWord> data;
    WordApiService apiService;

    public FavouriteWordAdapter(List<FavouriteWord> data) {
        this.data = data;
        apiService = new WordApiService();
    }

    @NonNull
    @Override
    public FavouriteWordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FavouriteWordItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.favourite_word_item,
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteWordAdapter.ViewHolder holder, int position) {
        FavouriteWord word = data.get(position);
        holder.bind(word);
    }

    public void setData(List<FavouriteWord> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FavouriteWordItemBinding binding;

        public ViewHolder(FavouriteWordItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            this.itemView.setOnClickListener(
                    v -> performSearch(binding.getWord().getWord(), v)
            );
        }

        public void bind(FavouriteWord word) {
            binding.setWord(word);
            binding.executePendingBindings();
        }

        public void performSearch(String word, View view) {
            DisposableSingleObserver<List<Word>> disposableSingleObserver = apiService.getWordDefinition(word)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Word>>() {
                       @Override
                       public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<Word> words) {
                           Word searchedWord = words.get(0);

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
                           Toast.makeText(view.getContext(), "Sorry, we've not updated this word yet.",
                                   Toast.LENGTH_SHORT).show();
                       }
                   }
                );
        }
    }
}
