package com.midterm.testdictionary.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentLoginBinding;
import com.midterm.testdictionary.repository.CallRepository;
import com.permissionx.guolindev.PermissionX;

public class LoginFragment extends Fragment {
    FragmentLoginBinding binding;
    CallRepository callRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    void init() {
        callRepository = CallRepository.getInstance();
        binding.enterBtn.setOnClickListener(view -> {
            // login to firebase database
            PermissionX.init(this)
                    .permissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                    .request((allGranted, grantedList, deniedList)  -> {
                        if (allGranted) {
                            callRepository.login(binding.username.getText().toString(), this.getContext(), () -> {
                                // If success then call
                                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mainFragment);
                            });
                        }
                    });
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}