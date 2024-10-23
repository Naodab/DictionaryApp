package com.midterm.testdictionary.view;

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
import com.midterm.testdictionary.databinding.FragmentSearchedWordBinding;

public class SearchedWordFragment extends Fragment {
    private FragmentSearchedWordBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchedWordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.backBtn.setOnClickListener(v -> {

        });

        binding.searchBtn.setOnClickListener(v -> {

        });

        binding.mic.setOnClickListener(v -> {

        });

        binding.relearn.setOnClickListener(v -> {

        });

        binding.practice.setOnClickListener(v -> {

        });

        binding.writingPractice.setOnClickListener(v -> {

        });

        binding.practice.setOnClickListener(v -> {

        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }



}
