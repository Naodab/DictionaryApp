package com.midterm.testdictionary.view;

import androidx.fragment.app.Fragment;

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

import com.bumptech.glide.Glide;
import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentWritingPracticeBinding;

public class WritingPracticeFragment extends Fragment {
    private FragmentWritingPracticeBinding binding;

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
        binding = FragmentWritingPracticeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        populateWordGrid();

        binding.closeBtn.setOnClickListener(v -> {

        });

        binding.inputText.setOnClickListener(v ->{

        });

        binding.volumnBtn.setOnClickListener(v ->{

        });

        binding.nextBtn.setOnClickListener(v ->{

        });

        binding.clearBtn.setOnClickListener(v ->{

        });

    }
    private void populateWordGrid() {
        // Xóa các ô trước đó (nếu có)
        binding.wordGrid.removeAllViews();

        for (int i = 0; i < words.length; i++) {
            FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.grid_item, binding.wordGrid, false);

            TextView textView = frameLayout.findViewById(R.id.word_text);
            textView.setText(words[i]);

            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Khi nhấn vào từ, thêm từ đó vào TextView inputText
                    binding.inputText.setText(binding.inputText.getText().toString() + textView.getText().toString());
                }
            });

            binding.wordGrid.addView(frameLayout);
        }
    }



}
