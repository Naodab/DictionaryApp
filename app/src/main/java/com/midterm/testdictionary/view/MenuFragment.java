package com.midterm.testdictionary.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.midterm.testdictionary.databinding.FragmentMenuBinding;

public class MenuFragment extends Fragment {
    private FragmentMenuBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Cài đặt chiều rộng và vị trí hiển thị
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.6);
        view.setLayoutParams(params);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.backBtn.setOnClickListener(v -> {

        });

        binding.documentTranslate.setOnClickListener(v -> {

        });

        binding.idiom.setOnClickListener(v -> {

        });

        binding.yourWord.setOnClickListener(v -> {

        });

        binding.recently.setOnClickListener(v -> {

        });

        binding.setting.setOnClickListener(v -> {

        });

        binding.login.setOnClickListener(v -> {

        });
    }

}
