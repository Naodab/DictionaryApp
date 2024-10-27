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
import com.midterm.testdictionary.databinding.FragmentRelearnBinding;

public class RelearnFragment extends Fragment {
    private FragmentRelearnBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRelearnBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.closeBtn.setOnClickListener(v -> {

        });

        binding.listenBtn.setOnClickListener(v -> {

        });

        binding.playStopBtn.setOnClickListener(v -> {

        });

        binding.settingBtn.setOnClickListener(v -> {

        });

        binding.rvItems.setLayoutManager(new LinearLayoutManager(getContext()));

    }



}
