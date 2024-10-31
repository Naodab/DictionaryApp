package com.midterm.testdictionary.view;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentYourWordBinding;
import com.midterm.testdictionary.model.FavouriteWord;
import com.midterm.testdictionary.model.WordObjectBox;
import com.midterm.testdictionary.utils.FetchWordsCallback;
import com.midterm.testdictionary.viewmodel.FavouriteWordAdapter;
import com.midterm.testdictionary.viewmodel.FavouriteWordService;

import java.util.ArrayList;
import java.util.List;

public class YourWordFragment extends Fragment {
    private FragmentYourWordBinding binding;
    private FavouriteWordService favouriteWordService;
    private FavouriteWordAdapter adapter;
    private List<FavouriteWord> data;
    private List<FavouriteWord> tmp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentYourWordBinding.inflate(inflater, container, false);
        favouriteWordService = FavouriteWordService.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        favouriteWordService.getAll(new FetchWordsCallback() {
            @Override
            public void onSuccess(List<FavouriteWord> favouriteWords) {
                data = favouriteWords;
                if (data == null)
                    data = new ArrayList<>();
                tmp = new ArrayList<>();
                tmp.addAll(data);
                adapter = new FavouriteWordAdapter(tmp);
                binding.rvFavouriteWords.setAdapter(adapter);
                binding.savedWord.setText("Size: " + data.size());
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("FAVOURITE WORD", "Can't get favourite words");
            }
        });
        binding.rvFavouriteWords.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.back.setOnClickListener(v -> Navigation.findNavController(getView())
                .navigate(R.id.action_yourWordFragment_to_mainFragment));

        binding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = binding.inputSearch.getText().toString();
                tmp = new ArrayList<>();
                if (text != null) {
                    for (FavouriteWord word : data) {
                        if (word.getWord().contains(text)) {
                            Log.d("DEBUG", word.getWord());
                            tmp.add(word);
                        }
                    }
                } else {
                    tmp.addAll(data);
                }
                adapter.setData(tmp);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
}
