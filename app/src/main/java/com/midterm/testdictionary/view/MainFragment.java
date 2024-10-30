package com.midterm.testdictionary.view;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentMainBinding;
import com.midterm.testdictionary.model.ObjectBox;
import com.midterm.testdictionary.model.Word;
import com.midterm.testdictionary.model.WordObjectBox;
import com.midterm.testdictionary.utils.NetworkUtil;
import com.midterm.testdictionary.viewmodel.MainItemAdapter;
import com.midterm.testdictionary.viewmodel.WordApiService;
import com.midterm.testdictionary.viewmodel.WordObjectBoxService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainFragment extends Fragment{
    private FragmentMainBinding binding;
    private ArrayList<String> itemList;
    private MainItemAdapter  itemsAdapter;

    private WordApiService apiService;
    private Word searchedWord;

    private WordObjectBoxService wordObjectBoxService;
    private WordObjectBox wordObjectBox;

    private FirebaseAuth mAuth;

    private GoogleSignInClient googleSignInClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiService = new WordApiService();
        wordObjectBoxService = new WordObjectBoxService();

        binding.rvItems.setLayoutManager(new GridLayoutManager(getContext(), 2));
        itemList = new ArrayList<>();
        itemsAdapter = new MainItemAdapter(itemList, this);
        binding.rvItems.setAdapter(itemsAdapter);
        setMainItem();
        configWordOfDay();

        // Thiết lập NavigationItemSelectedListener cho NavigationView
//        binding.navView.setNavigationItemSelectedListener(this);

        // Sự kiện nhấn nút Settings để mở/đóng Navigation Drawer
        binding.listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.mainFragment.isDrawerOpen(GravityCompat.START)) {
                    binding.mainFragment.openDrawer(GravityCompat.START);
                } else {
                    binding.mainFragment.closeDrawer(GravityCompat.START);
                }
            }
        });

        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String word = binding.search.getText().toString();
                setWordItems(word);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String word = binding.search.getText().toString();
                    performSearch(word);
                    return true;
                }
                return false;
            }
        });

        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_login){
                    Menu menu = binding.navView.getMenu();
                    MenuItem loginItem = menu.findItem(R.id.nav_login);

                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    if(currentUser != null && loginItem.getTitle().equals("Log out")) {
                        mAuth.signOut();

                        googleSignInClient.signOut();

                        Toast.makeText(getContext(), "Log out successfully", Toast.LENGTH_SHORT).show();
                        loginItem.setTitle("Log in");
                    }else if(currentUser == null && loginItem.getTitle().equals("Log in")){
                        Navigation.findNavController(view).navigate(R.id.loginFragment);
                    }else{
                        Toast.makeText(getContext(), "Log out error", Toast.LENGTH_SHORT).show();
                    }

                }

                return true;
            }
        });

        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser() != null){
                    Toast.makeText(view.getContext(), mAuth.getCurrentUser().getDisplayName() + " " + mAuth.getCurrentUser().getEmail(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        Menu menu = binding.navView.getMenu();
        MenuItem loginItem = menu.findItem(R.id.nav_login);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            loginItem.setTitle("Log out");
        }else{
            loginItem.setTitle("Log in");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_main, container, false);
        binding = FragmentMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        return view;
    }
    public void setMainItem(){
        itemList.add("Searched word");
        itemList.add("Favourite word");
        itemList.add("Text translation");
        itemList.add("Practice speaking");
        itemsAdapter.notifyDataSetChanged();
    }

    public Word performSearch(String word) {
        final Word[] result = {null};
        DisposableSingleObserver<List<Word>> disposableSingleObserver = apiService.getWordDefinition(word)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<List<Word>>() {
                @Override
                public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<Word> words) {
                    Log.d("DEBUG", "Success");
                    searchedWord = words.get(0);

                    if (searchedWord != null) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("word", searchedWord);
                        View view = getView();
                        if (view != null) {
                            wordObjectBoxService.insertWord(searchedWord);

                            Navigation.findNavController(view).navigate(R.id.detailFragment, bundle);
                        }

//                        Navigation.findNavController(view).navigate(R.id.detailFragment, bundle);
                    }
                }

                @Override
                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                    Log.d("DEBUG", "Fail" + e.getMessage());
                    e.printStackTrace();
                }
            });
        return result[0];
    }

    public void setWordItems(String word){
        DisposableSingleObserver<List<Word>> disposableSingleObserver = apiService.getWordDefinition(word)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<List<Word>>() {
                @Override
                public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<Word> words) {
                    Log.d("DEBUG", "Success");

                    for (Word word1 : words) {
                        Log.d("DEBUG", word1.getMeanings().get(0).getDefinitions().get(0).getDefinition());
                    }
                }

                @Override
                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                    Log.d("DEBUG", "Fail" + e.getMessage());
                    e.printStackTrace();
                }
            });
    }

    private void configWordOfDay() {
        List<WordObjectBox> words = wordObjectBoxService.getWordBox();
        if (words.size() > 0) {
            int randomIndex = (int)(Math.random() * words.size());
            wordObjectBox = words.get(randomIndex);
            binding.wodWord.setText(wordObjectBox.getWord());
            binding.wodDefinition.setText(wordObjectBox.getDefinition());
            binding.wodPhonetic.setText(wordObjectBox.getPhonetic());

            binding.wodLayout.setOnClickListener(v ->{
                if (NetworkUtil.isNetworkAvailable(v.getContext()))
                    performSearch(wordObjectBox.getWord());
                else
                    Toast.makeText(v.getContext(), "Please, connect to network.",
                            Toast.LENGTH_SHORT).show();
            });
        }
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.nav_menu:
//                Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.nav_settings:
//                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
//                break;
//            // Thêm các case khác nếu cần
//        }
//        // Đóng drawer sau khi chọn mục
//        drawerLayout.closeDrawer(GravityCompat.START);
//        return true;
//        return false;
//    }
}