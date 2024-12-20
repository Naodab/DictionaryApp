package com.midterm.testdictionary.view;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentLoginBinding;
import com.midterm.testdictionary.repository.CallRepository;
import com.permissionx.guolindev.PermissionX;

import java.util.Arrays;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private CallRepository callRepository;

    private FirebaseAuth mAuth;

    private GoogleSignInClient googleSignInClient;

    private CallbackManager mCallbackManager;

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_mainFragment);
        }
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

        binding.google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

//        init();
    }

    private void signIn(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
        signInIntent.putExtra("prompt", "select_account");
        activityResultLauncher.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuth(account.getIdToken());
                } catch (ApiException e) {
                    Toast.makeText(requireContext(), "API Exception: " + e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    });

    private void firebaseAuth(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        binding.progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        binding.progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Account logged in", Toast.LENGTH_LONG).show();
                            Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_mainFragment);
                        }else{
                            Log.w("DEBUG", "signInWithGoogle:failure", task.getException());
                            Toast.makeText(getContext(), "Sign in failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void init() {
        callRepository = CallRepository.getInstance();
        binding.enterBtn.setOnClickListener(view -> {
            PermissionX.init(this)
                .permissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                .request((allGranted, grantedList, deniedList)  -> {
                    if (allGranted) {
                        // sua tu username thanh email
                        callRepository.login(binding.email.getText().toString(), this.getContext(),
                            () -> {
                                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mainFragment);
                            }
                        );
                    } else {
                        Log.e("Permissions", "Quyền truy cập bị từ chối.");
                    }
                }
            );
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        FacebookSdk.sdkInitialize(getContext());
        AppEventsLogger.activateApp(getActivity());

        mCallbackManager = CallbackManager.Factory.create();
//        binding.facebook.setReadPermissions("email", "public_profile");
//        binding.facebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Log.d("DEBUG", "facebook:onSuccess:" + loginResult);
//                handleFacebookAccessToken(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel() {
//                Log.d("DEBUG", "facebook:onCancel");
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.d("DEBUG", "facebook:onError", error);
//            }
//        });

        binding.facebook.setOnClickListener(v -> {
            LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("email", "public_profile"));
            // Đăng ký callback cho việc đăng nhập Facebook
            LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d("DEBUG", "facebook:onSuccess:" + loginResult);
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getContext(), "Login cancelled", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException exception) {
                    Toast.makeText(getContext(), "Login error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        return binding.getRoot();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        binding.progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        binding.progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Account logged in", Toast.LENGTH_LONG).show();
                            Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_mainFragment);
                        } else {
                            Log.w("DEBUG", "signInWithGoogle:failure", task.getException());
                            Toast.makeText(getContext(), "Sign in failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}