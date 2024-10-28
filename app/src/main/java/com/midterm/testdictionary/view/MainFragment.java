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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentMainBinding;
import com.midterm.testdictionary.model.ObjectBox;
import com.midterm.testdictionary.model.Word;
import com.midterm.testdictionary.model.WordObjectBox;
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

public class MainFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener{
    private FragmentMainBinding binding;
    private ArrayList<String> itemList;
    private MainItemAdapter  itemsAdapter;

    private WordApiService apiService;
    private Word searchedWord;

    private WordObjectBoxService wordObjectBoxService;

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
        itemsAdapter = new MainItemAdapter(itemList);
        binding.rvItems.setAdapter(itemsAdapter);
        setMainItem();

        // Thiết lập NavigationItemSelectedListener cho NavigationView
        binding.navView.setNavigationItemSelectedListener(this);

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

        binding.listItem.setOnClickListener(v ->{

        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_main, container, false);
        binding = FragmentMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }
    public void setMainItem(){
        itemList.add("Từ đã tra");
        itemList.add("Từ của bạn");
        itemList.add("Dịch văn bản");
        itemList.add("Thực hành Tiếng Anh");
//        itemList.add("Cài đặt");
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
        return false;
    }

//     Tạo và thêm SettingFragment đè lên MainFragment
//    private void showSettingFragment() {
//        SettingFragment settingFragment = new SettingFragment();
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//
//        transaction.replace(R.id.mainFragment, settingFragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
}