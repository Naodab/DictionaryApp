package com.midterm.testdictionary.view;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.midterm.testdictionary.databinding.FragmentPracticeBinding;

public class PracticeFragment extends Fragment {
    private FragmentPracticeBinding binding;

    // Giả sử các từ cần hiển thị trên gridlayout
    private String[] words = {"l", "h", "e", "l", "o"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPracticeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.closeBtn.setOnClickListener(v -> {

        });

        binding.ket01.setOnClickListener(v ->{

        });

        binding.key02.setOnClickListener(v ->{

        });

        binding.key03.setOnClickListener(v ->{

        });

        binding.key04.setOnClickListener(v ->{

        });

    }
}
