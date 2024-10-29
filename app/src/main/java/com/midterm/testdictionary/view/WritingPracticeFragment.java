package com.midterm.testdictionary.view;

import androidx.fragment.app.Fragment;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.midterm.testdictionary.model.WordObjectBox;
import com.midterm.testdictionary.viewmodel.AudioService;
import com.midterm.testdictionary.viewmodel.WordObjectBoxService;
import com.midterm.testdictionary.viewmodel.WritingPracticeAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WritingPracticeFragment extends Fragment {
    public static final int NUMBER_QUESTIONS = 5;
    private FragmentWritingPracticeBinding binding;
    private WritingPracticeAdapter charsAdapter;
    private List<String> chars;
    private WordObjectBoxService wordObjectBoxService;
    private List<WordObjectBox> wordObjectBoxes;
    private AudioService audioService = new AudioService();
    private int currentIndex = 0;
    private boolean isNew = true;

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
        wordObjectBoxService = new WordObjectBoxService();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.progressLine.setMax(NUMBER_QUESTIONS);
        wordObjectBoxes = getRandom();
        performSearch(wordObjectBoxes.get(currentIndex));

        binding.closeBtn.setOnClickListener(this::showConfirmDialog);

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
                            performSearch(wordObjectBoxes.get(currentIndex));
                        }
                    } else {
                        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationX",
                                0f, 10f, -10f, 10f, -10f, 5f, -5f, 0);
                        animator.setDuration(500);
                        animator.start();
                        binding.inputText.setText("");
                        charsAdapter.setData(chars);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.volumnBtn.setOnClickListener(v ->{
            String audio = wordObjectBoxes.get(currentIndex).getAudio();
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

        FlexboxLayoutManager flexbox = new FlexboxLayoutManager(getContext());
        flexbox.setFlexDirection(FlexDirection.ROW);
        flexbox.setFlexWrap(FlexWrap.WRAP);
        flexbox.setJustifyContent(JustifyContent.CENTER);
        flexbox.setAlignItems(AlignItems.CENTER);
        binding.rvChars.setLayoutManager(flexbox);
    }

    public List<WordObjectBox> getRandom() {
        List<WordObjectBox> all = wordObjectBoxService.getWordBox();
        Collections.shuffle(all);
        return all.subList(0,  Math.min(NUMBER_QUESTIONS, all.size()));
    }

    public void performSearch(WordObjectBox word) {
        this.word = word.getWord();
        binding.tvResult.setText(word.getWord());
        binding.tvDefinition.setText(word.getDefinition());
        chars = Arrays.asList(word.getWord().split(""));
        Collections.shuffle(chars);
        if (isNew) {
            charsAdapter = new WritingPracticeAdapter(chars, binding.inputText);
            binding.rvChars.setAdapter(charsAdapter);
            isNew = false;
        } else {
            charsAdapter.setData(chars);
        }
    }

    public void showConfirmDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Navigation.findNavController(view)
                                .navigate(R.id.action_writingPracticeFragment_to_mainFragment);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                        .getColor(R.color.title_thame_color));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources()
                        .getColor(R.color.title_thame_color));
            }
        });
        dialog.show();
    }
}
