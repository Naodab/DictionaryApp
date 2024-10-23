package com.midterm.testdictionary.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentSettingBinding;

public class SettingFragment extends Fragment {
    private FragmentSettingBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.backBtn.setOnClickListener(v -> {

        });

        binding.wordReminder.setOnClickListener(v -> {

        });

        binding.folderReminder.setOnClickListener(v -> {

        });

        binding.timePerDay.setOnClickListener(v -> {

        });

        binding.startTime.setOnClickListener(v -> {

        });

        binding.endTime.setOnClickListener(v -> {

        });

        binding.wordDisplayFrequency.setOnClickListener(v -> {

        });

        binding.sendErrorEmail.setOnClickListener(v -> {

        });

        binding.askedQuestions.setOnClickListener(v -> {

        });

        binding.sendErrorEmail.setOnClickListener(v -> {

        });

        binding.pronunSpeed.setOnClickListener(v -> {

        });

        binding.defaultSound.setOnClickListener(v -> {

        });

        binding.automaticPronun.setOnClickListener(v -> {

        });

        binding.showMeaning.setOnClickListener(v -> {

        });
    }



}
