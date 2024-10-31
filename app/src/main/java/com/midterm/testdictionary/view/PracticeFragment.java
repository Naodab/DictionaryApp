package com.midterm.testdictionary.view;

import androidx.fragment.app.Fragment;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentPracticeBinding;
import com.midterm.testdictionary.model.WordObjectBox;
import com.midterm.testdictionary.viewmodel.WordObjectBoxService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PracticeFragment extends Fragment {
    public static final int NUMBER_QUESTIONS = 5;
    private FragmentPracticeBinding binding;
    private WordObjectBoxService wordObjectBoxService;
    private List<WordObjectBox> wordObjectBoxes;
    private View[] options;
    private TextView[] labels;
    private int size;
    private int currentIndex = 0;
    private int currentAnswer = 0;
    private int[] index;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPracticeBinding.inflate(inflater, container, false);
        wordObjectBoxService = new WordObjectBoxService();
        wordObjectBoxes = wordObjectBoxService.getWordBox();
        Collections.shuffle(wordObjectBoxes);
        options = new View[4];
        labels = new TextView[4];
        index = new int[4];
        size = wordObjectBoxes.size();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.progressLine.setMax(NUMBER_QUESTIONS);

        options[0] = binding.key01;
        options[1] = binding.key02;
        options[2] = binding.key03;
        options[3] = binding.key04;

        labels[0] = binding.tv01;
        labels[1] = binding.tv02;
        labels[2] = binding.tv03;
        labels[3] = binding.tv04;
        performQuestion();

        for (int i = 0; i < 4; i++) {
            int finalI = i;
            options[i].setOnClickListener(v -> {
                if (currentAnswer == finalI) {
                    options[finalI].setBackgroundResource(R.drawable.right_option);
                    binding.progressLine.setProgress(currentIndex);
                    binding.nextBtn.setTextColor(Color.parseColor("#45B8E9"));
                } else {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(options[finalI], "translationX",
                            0f, 10f, -10f, 10f, -10f, 5f, -5f, 0);
                    animator.setDuration(500);
                    animator.start();
                    options[finalI].setBackgroundResource(R.drawable.wrong_option);
                }
            });
        }

        binding.closeBtn.setOnClickListener(this::showConfirmDialog);
        binding.nextBtn.setOnClickListener(v -> {
            binding.nextBtn.setTextColor(Color.TRANSPARENT);
            Arrays.stream(options).forEach(option ->
                    option.setBackgroundResource(R.drawable.rectangle_1219_shape));
            performQuestion();
        });

        binding.retryButton.setOnClickListener(v -> {
            binding.layoutCongratulation.setVisibility(View.GONE);
            Collections.shuffle(wordObjectBoxes);
            currentIndex = 0;
            performQuestion();
        });

        binding.back.setOnClickListener(v -> Navigation.findNavController(getView())
                .navigate(R.id.action_practiceFragment_to_mainFragment));
    }

    private void performQuestion() {
        if (currentIndex == NUMBER_QUESTIONS) {
            binding.layoutCongratulation.setVisibility(View.VISIBLE);
        } else {
            Arrays.stream(index).forEach(i -> i = -1);
            calculateOptions();
        }
        currentIndex++;
    }

    private boolean existIndex(int i) {
        for (int j : index) {
            if (j == i)
                return true;
        }
        return false;
    }

    private void calculateOptions() {
        currentAnswer = (int)(Math.random() * 4);
        index[currentAnswer] = currentIndex;
        int i = 0;
        while (i < 4) {
            if (i != currentAnswer) {
                int emptyIndex = calculateIndexForOption();
                index[i] = emptyIndex;
            }
            i++;
        }
        i = 0;
        for (i = 0; i < 4; i++) {
            labels[i].setText(wordObjectBoxes.get(index[i]).getWord());
            if (index[i] == currentIndex) {
                binding.question.setText(wordObjectBoxes.get(index[i]).getDefinition());
            }
        }
    }

    private int calculateIndexForOption() {
        int crIndex = 0;
        do {
            crIndex = (int)(Math.random() * size);
        } while (existIndex(crIndex));
        return crIndex;
    }

    public void showConfirmDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Navigation.findNavController(view)
                            .navigate(R.id.action_practiceFragment_to_mainFragment);
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
