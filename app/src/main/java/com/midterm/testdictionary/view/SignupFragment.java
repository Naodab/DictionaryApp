package com.midterm.testdictionary.view;

import static org.webrtc.ContextUtils.getApplicationContext;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentSignupBinding;

public class SignupFragment extends Fragment {
    private FragmentSignupBinding binding;
    private FirebaseAuth mAuth;


    @Override
    public void onStart() {
        super.onStart();

//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.loginFragment);
            }
        });

        binding.google.setOnClickListener(v -> {

        });

        binding.facebook.setOnClickListener(v -> {

        });

        binding.twitter.setOnClickListener(v -> {

        });

        binding.signUp.setOnClickListener(v -> {
            String email = String.valueOf(binding.enterEmail.getText());
            String password = String.valueOf(binding.enterPassword.getText());

            if(TextUtils.isEmpty(email)){
                Toast.makeText(this.getContext(), "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(password)){
                Toast.makeText(this.getContext(), "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
//                                Toast.makeText(Register.this, "Account created", Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(getApplicationContext(), Login.class);
//                                startActivity(intent);
//                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("DEBUG", "createUserWithEmail:failure", task.getException());
//                                Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
    }



}
