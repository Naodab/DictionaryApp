package com.midterm.testdictionary.view;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.midterm.testdictionary.model.Phonetic;
import com.midterm.testdictionary.model.Word;
import com.midterm.testdictionary.model.WordObjectBox;
import com.midterm.testdictionary.viewmodel.AudioService;
import com.midterm.testdictionary.viewmodel.WordApiService;
import com.midterm.testdictionary.viewmodel.WritingPracticeAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.objectbox.Box;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WritingPracticeFragment extends Fragment {
    public static final int NUMBER_QUESTIONS = 5;
    private FragmentWritingPracticeBinding binding;
    private WritingPracticeAdapter charsAdapter;
    private List<String> chars;
    private Box<WordObjectBox> wordBox;
    private List<WordObjectBox> wordObjectBoxes;
    private WordApiService apiService;
    private Word currentWord;
    private AudioService audioService = new AudioService();
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

        binding.closeBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_writingPracticeFragment_to_mainFragment);
        });

        binding.back.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_writingPracticeFragment_to_mainFragment);
        });

        binding.retryButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_writingPracticeFragment_self);
        });

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
                            binding.write.setVisibility(View.GONE);
                            binding.layoutCongratulation.setVisibility(View.VISIBLE);
                        } else {
                            binding.tvResult.setVisibility(View.GONE);
                            binding.inputText.setText("");
                            binding.progressLine.setProgress(currentIndex);
                            word = wordObjectBoxes.get(currentIndex).getWord();
                            performSearch(word);
                        }
                    } else {
                        binding.inputText.setText("");
                        charsAdapter.setData(chars);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.volumnBtn.setOnClickListener(v ->{
            String audio = "";
            for (Phonetic phonetic : currentWord.getPhonetics()) {
                if (!phonetic.getAudio().isEmpty()) {
                    audio = phonetic.getAudio();
                    break;
                }
            }
            if (audio != null)
                audioService.playAudio(audio);
            else
                Toast.makeText(getContext(), "Sorry, we've not updated audio of this word yet",
                        Toast.LENGTH_SHORT).show();
        });

        binding.resultBtn.setOnClickListener(v -> {
            binding.tvResult.setVisibility(View.VISIBLE);
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

    public void performSearch(String word) {
        DisposableSingleObserver<List<Word>> disposableSingleObserver = apiService.getWordDefinition(word)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<List<Word>>() {
                @Override
                public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<Word> words) {
                    currentWord = words.get(0);
                    binding.tvResult.setText(currentWord.getWord().toUpperCase());
                    chars = Arrays.asList(word.split(""));
                    Collections.shuffle(chars);
                    charsAdapter.setData(chars);
                    binding.tvDefinition.setText(currentWord
                            .getMeanings().get(0)
                            .getDefinitions().get(0)
                            .getDefinition());
                }

                @Override
                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {}
            });
    }
}
