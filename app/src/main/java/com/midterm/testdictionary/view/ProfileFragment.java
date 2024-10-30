package com.midterm.testdictionary.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentProfileBinding;
import com.midterm.testdictionary.viewmodel.WordObjectBoxService;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private FirebaseAuth mAuth;

    private WordObjectBoxService wordObjectBoxService;

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

        mAuth = FirebaseAuth.getInstance();

        wordObjectBoxService = new WordObjectBoxService();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        loadAvatarImage();

        binding.viewNumberWord.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.searchedWordFragment);
        });

        binding.backBtn.setOnClickListener(v -> {
            // TODO: check if heart is clicked and existence of this word in firestore
            int backStackCount = getFragmentManager().getBackStackEntryCount();
            Log.d("BackStack", "BackStack count: " + backStackCount);

            NavHostFragment.findNavController(getParentFragment()).popBackStack();
        });

        updateUserInfo();
    }

    private void updateUserInfo() {
        String name = (mAuth.getCurrentUser().getDisplayName() != null && !mAuth.getCurrentUser().getDisplayName().equals("")) ? mAuth.getCurrentUser().getDisplayName() : "Anonymous";

        binding.nameTv.setText(name);
        binding.emailTv.setText(mAuth.getCurrentUser().getEmail());

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            for (UserInfo info : user.getProviderData()) {
                if (info.getProviderId().equals("google.com")) {
                    binding.loginFromTv.setText("Google");
                } else if (info.getProviderId().equals("password")) {
                    binding.loginFromTv.setText("Username/password");
                }
            }
        }

        binding.wordNumber.setText(String.valueOf(wordObjectBoxService.getSize()));
    }

    // Cắt ảnh thành hình tròn
//    private void loadAvatarImage() {
//
//        Glide.with(this)
//                .load(R.drawable.icon_profile)
//                .circleCrop()
//                .into(binding.avatarImage);
//    }
}

