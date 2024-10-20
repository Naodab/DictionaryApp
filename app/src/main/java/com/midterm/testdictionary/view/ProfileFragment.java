package com.midterm.testdictionary.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.Glide;
import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentProfileBinding;
import com.midterm.testdictionary.model.ObjectBox;
import com.midterm.testdictionary.model.WordObjectBox;
import com.midterm.testdictionary.viewmodel.MainItemAdapter;
import com.midterm.testdictionary.viewmodel.WordApiService;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadAvatarImage();

        binding.avatarImage.setOnClickListener(v -> {

        });

        binding.viewNumberWord.setOnClickListener(v -> {

        });

        binding.viewFolder.setOnClickListener(v -> {

        });

        binding.backBtn.setOnClickListener(v -> {

        });

        binding.backupBtn.setOnClickListener(v -> {

        });

        binding.downloadBtn.setOnClickListener(v -> {

        });

        binding.logoutBtn.setOnClickListener(v -> {

        });

        updateUserInfo();
    }

    private void updateUserInfo() {
        binding.nameTv.setText("Nguyễn Văn A");
        binding.emailTv.setText("nguyenvana@gmail.com");
        binding.loginFromTv.setText("Google");

        binding.wordNumber.setText("3");
        binding.folderNumber.setText("3");
    }

    // Cắt ảnh thành hình tròn
    private void loadAvatarImage() {

        Glide.with(this)
                .load(R.drawable.avatar_profile)
                .circleCrop()
                .into(binding.avatarImage);
    }

}

