package com.midterm.testdictionary.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentWordOfTheDayBinding;

public class WordOfTheDayFragment extends Fragment {
    private FragmentWordOfTheDayBinding binding;

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
        binding = FragmentWordOfTheDayBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        populateWordGrid();

        binding.backBtn.setOnClickListener(v -> {

        });

        binding.searchBtn.setOnClickListener(v -> {

        });

        binding.saveBtn.setOnClickListener(v -> {

        });

        binding.meaningList.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    private void populateWordGrid() {
        // Xóa các ô trước đó (nếu có)
        binding.wordGrid.removeAllViews();

        for (int i = 0; i < words.length; i++) {
            FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.synonyms_item, binding.wordGrid, false);

            TextView textView = frameLayout.findViewById(R.id.word_text);
            textView.setText(words[i]);

            binding.wordGrid.addView(frameLayout);
        }
    }
}
