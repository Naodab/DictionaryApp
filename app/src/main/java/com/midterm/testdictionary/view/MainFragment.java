package com.midterm.testdictionary.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentMainBinding;
import com.midterm.testdictionary.model.Word;
import com.midterm.testdictionary.viewmodel.MainItemAdapter;
import com.midterm.testdictionary.viewmodel.WordApiService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    private ArrayList<String> itemList;
    private MainItemAdapter  itemsAdapter;

    private WordApiService apiService;

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

        binding.rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        itemList = new ArrayList<>();
        itemsAdapter = new MainItemAdapter(itemList);
        binding.rvItems.setAdapter(itemsAdapter);
        setMainItem();

        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String word = binding.search.getText().toString();
//                Log.d("DEBUG", word);
                setWordItems(word);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
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
        itemList.add("Cài đặt");
        itemsAdapter.notifyDataSetChanged();
    }
    public void setWordItems(String word){
        apiService.getWordDefinition(word)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Word>>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<Word> words) {
                        Log.d("DEBUG", "Success");
//
//                        dogsList.addAll(dogBreeds);
//                        dogsAdapter.notifyDataSetChanged();
                        for(Word word1: words){
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
}