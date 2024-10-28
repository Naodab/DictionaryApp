package com.midterm.testdictionary.view;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentWritingPracticeBinding;
import com.midterm.testdictionary.model.ObjectBox;
import com.midterm.testdictionary.model.Word;
import com.midterm.testdictionary.model.WordObjectBox;
import com.midterm.testdictionary.viewmodel.WordApiService;
import com.midterm.testdictionary.viewmodel.WritingPracticeAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import io.objectbox.Box;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WritingPracticeFragment extends Fragment {
    private static final int NUMBER_QUESTIONS = 5;
    private FragmentWritingPracticeBinding binding;
    private WritingPracticeAdapter charsAdapter;
    private List<String> chars;
    private Box<WordObjectBox> wordBox;
    private List<WordObjectBox> wordObjectBoxes;
    private WordApiService apiService;
    private int currentIndex = 0;

    private String word;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentWritingPracticeBinding.inflate(inflater, container, false);
        wordBox = ObjectBox.get().boxFor(WordObjectBox.class);
        apiService = new WordApiService();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.progressLine.setMax(NUMBER_QUESTIONS);
        wordObjectBoxes = getRandom();
        word = wordObjectBoxes.get(currentIndex).getWord();
        performSearch(word);

        //TODO: back to main fragment
        binding.closeBtn.setOnClickListener(v -> {});

        binding.inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String tmp = binding.inputText.getText().toString();
                if (tmp.length() == word.length()) {
                    if (tmp.equals(word)) {
                        currentIndex++;
                        if (currentIndex == NUMBER_QUESTIONS) {
                            //TODO: congratulation
                        } else {
                            binding.progressLine.setProgress(currentIndex);
                            word = wordObjectBoxes.get(currentIndex).getWord();
                            chars = Arrays.asList(word.split(""));
                            Collections.shuffle(chars);
                            charsAdapter.setData(chars);
                        }
                    } else {
                        binding.inputText.setText("");
                        Log.d("CHARS", "onTextChanged: " + chars.toString());
                        charsAdapter.setData(chars);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.volumnBtn.setOnClickListener(v ->{

        });

        binding.nextBtn.setOnClickListener(v ->{

        });

        binding.clearBtn.setOnClickListener(v ->{
            binding.inputText.setText("");
            charsAdapter.setData(chars);
        });

        chars = Arrays.asList(word.split(""));
        Collections.shuffle(chars);
        charsAdapter = new WritingPracticeAdapter(chars, binding.inputText);
        binding.rvChars.setAdapter(charsAdapter);

        FlexboxLayoutManager flexbox = new FlexboxLayoutManager(getContext());
        flexbox.setFlexDirection(FlexDirection.ROW);
        flexbox.setFlexWrap(FlexWrap.WRAP);
        flexbox.setJustifyContent(JustifyContent.CENTER);
        flexbox.setAlignItems(AlignItems.CENTER);
        binding.rvChars.setLayoutManager(flexbox);
    }

    public List<WordObjectBox> getRandom() {
        List<WordObjectBox> all = wordBox.getAll();
        Collections.shuffle(all);
        return all.subList(0,  Math.min(NUMBER_QUESTIONS, all.size()));
    }

    public Word performSearch(String word) {
        final Word[] result = {null};
        DisposableSingleObserver<List<Word>> disposableSingleObserver = apiService.getWordDefinition(word)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Word>>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<Word> words) {
                        result[0] = words.get(0);
                        binding.setWord(result[0]);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {}
                });
        return result[0];
    }
}
