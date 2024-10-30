package com.midterm.testdictionary.view;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentYourWordBinding;
import com.midterm.testdictionary.model.WordObjectBox;
import com.midterm.testdictionary.viewmodel.FavouriteWordAdapter;

import java.util.ArrayList;
import java.util.List;

public class YourWordFragment extends Fragment {
    private FragmentYourWordBinding binding;
    private FavouriteWordAdapter adapter;
    private List<WordObjectBox> data;

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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO: load from firestore
        data = new ArrayList<>();
        adapter = new FavouriteWordAdapter(data);
        binding.rvFavouriteWords.setAdapter(adapter);
        binding.rvFavouriteWords.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.savedWord.setText("Size: " + data.size());

        binding.searchBtn.setOnClickListener(v -> {

        });
    }
}
