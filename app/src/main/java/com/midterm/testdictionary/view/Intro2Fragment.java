package com.midterm.testdictionary.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentIntro2Binding;

public class Intro2Fragment extends Fragment {
    private FragmentIntro2Binding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentIntro2Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.back.setOnClickListener(v -> Navigation.findNavController(getView())
                .navigate(R.id.action_intro2Fragment_to_intro1Fragment)
        );

        binding.next.setOnClickListener(v -> Navigation.findNavController(getView())
                .navigate(R.id.action_intro2Fragment_to_intro3Fragment)
        );

        binding.skip.setOnClickListener(v -> Navigation.findNavController(getView())
                .navigate(R.id.action_intro2Fragment_to_mainFragment));
    }
}
