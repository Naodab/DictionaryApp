package com.midterm.testdictionary.view;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.midterm.testdictionary.DataBinderMapperImpl;
import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentRelearnBinding;
import com.midterm.testdictionary.model.Meaning;
import com.midterm.testdictionary.model.Word;
import com.midterm.testdictionary.model.WordObjectBox;
import com.midterm.testdictionary.viewmodel.AudioService;
import com.midterm.testdictionary.viewmodel.MeaningAdapter;
import com.midterm.testdictionary.viewmodel.WordApiService;
import com.midterm.testdictionary.viewmodel.WordObjectBoxService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RelearnFragment extends Fragment {
    public static int NUMBER_WORDS = 10;
    private FragmentRelearnBinding binding;
    private WordApiService apiService;
    private AudioService audioService;
    private WordObjectBoxService wordObjectBoxService;
    private List<WordObjectBox> wordObjectBoxes;

    private List<Meaning> meaningList;
    private MeaningAdapter meaningAdapter;
    private Word word;
    private int currentIndex = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.fragment_relearn, null, false);

        audioService = new AudioService();
        wordObjectBoxService = new WordObjectBoxService();
        wordObjectBoxes = getRandom();
        apiService = new WordApiService();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.progressLine.setMax(NUMBER_WORDS);
        binding.progressLine.setProgress(0);
        binding.countWords.setText(currentIndex + "/" + NUMBER_WORDS);

        meaningAdapter = new MeaningAdapter(new ArrayList<>(), false);
        binding.rvMeaning.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvMeaning.setAdapter(meaningAdapter);
        performWord(wordObjectBoxes.get(currentIndex).getWord());

        binding.closeBtn.setOnClickListener(this::showConfirmDialog);

        binding.audioBtn.setOnClickListener(v ->
            audioService.playAudio(wordObjectBoxes.get(currentIndex).getAudio())
        );

        binding.nextBtn.setOnClickListener(v -> {
            currentIndex++;
            binding.progressLine.setProgress(currentIndex);
            binding.countWords.setText(currentIndex + "/" + NUMBER_WORDS);
            if (currentIndex == NUMBER_WORDS) {
                binding.nextBtn.setVisibility(View.GONE);
                binding.finishBtn.setVisibility(View.VISIBLE);
            } else {
                performWord(wordObjectBoxes.get(currentIndex).getWord());
            }
        });

        binding.finishBtn.setOnClickListener(v -> {
            binding.progressLine.setProgress(NUMBER_WORDS);
            binding.layoutCongratulation.setVisibility(View.VISIBLE);
        });

        binding.retryButton.setOnClickListener(v -> {
            binding.layoutCongratulation.setVisibility(View.GONE);
            binding.finishBtn.setVisibility(View.GONE);
            binding.nextBtn.setVisibility(View.VISIBLE);
            wordObjectBoxes = getRandom();
            currentIndex = 0;
            binding.progressLine.setProgress(0);
            performWord(wordObjectBoxes.get(currentIndex).getWord());
        });

        binding.back.setOnClickListener(v -> {
            Navigation.findNavController(getView())
                    .navigate(R.id.action_relearnFragment_to_mainFragment);
        });
    }

    private List<WordObjectBox> getRandom() {
        List<WordObjectBox> all = wordObjectBoxService.getWordBox();
        Collections.shuffle(all);
        return all.subList(0, Math.min(NUMBER_WORDS, all.size()));
    }

    private void performWord(String wordName) {
        DisposableSingleObserver<List<Word>> disposableSingleObserver = apiService.getWordDefinition(wordName)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<List<Word>>() {
                @Override
                public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<Word> words) {
                    word = words.get(0);
                    binding.setWord(word);
                    meaningList = word.getMeanings();
                    meaningAdapter.setData(meaningList);
                    meaningAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {}
            }
        );
    }

    public void showConfirmDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Navigation.findNavController(view)
                                .navigate(R.id.action_relearnFragment_to_mainFragment);
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