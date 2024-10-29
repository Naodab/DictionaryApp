package com.midterm.testdictionary.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentLoginBinding;
import com.midterm.testdictionary.repository.CallRepository;
import com.permissionx.guolindev.PermissionX;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private CallRepository callRepository;

    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();

//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Navigation.findNavController(view).navigate(R.id.);
//        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnChooseRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.signupFragment);
            }
        });

        binding.enterBtn.setOnClickListener(v -> {
            String email = String.valueOf(binding.email.getText());
            String password = String.valueOf(binding.password.getText());

            if(TextUtils.isEmpty(email)){
                Toast.makeText(this.getContext(), "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(password)){
                Toast.makeText(this.getContext(), "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.progressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Account logged in", Toast.LENGTH_LONG).show();
                                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mainFragment);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("DEBUG", "signInWithEmail:failure", task.getException());
                                Toast.makeText(getContext(), "Sign in failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

        binding.skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mainFragment);
            }
        });

//        init();
    }

    void init() {
        callRepository = CallRepository.getInstance();
        binding.enterBtn.setOnClickListener(view -> {
            // login to firebase database
            PermissionX.init(this)
                    .permissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                    .request((allGranted, grantedList, deniedList)  -> {
                        if (allGranted) {
                            // sua tu username thanh email
                            callRepository.login(binding.email.getText().toString(), this.getContext(),
                                    () -> {
                                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mainFragment);
                                    },
                                    (errorMessage) -> {
                                        Log.e("Login Error", errorMessage);
                                        Toast.makeText(getContext(), "Đăng nhập thất bại: " + errorMessage, Toast.LENGTH_SHORT).show();
                                    }
                            );
                        } else {
                            Log.e("Permissions", "Quyền truy cập bị từ chối.");
                        }
                    });
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();

        return binding.getRoot();
    }
}