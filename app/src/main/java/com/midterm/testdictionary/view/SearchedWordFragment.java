package com.midterm.testdictionary.view;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentSearchedWordBinding;
import com.midterm.testdictionary.model.ObjectBox;
import com.midterm.testdictionary.model.Word;
import com.midterm.testdictionary.model.WordObjectBox;
import com.midterm.testdictionary.utils.NetworkUtil;
import com.midterm.testdictionary.viewmodel.SearchedWordAdapter;
import com.midterm.testdictionary.viewmodel.WordObjectBoxService;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;

public class SearchedWordFragment extends Fragment {
    private FragmentSearchedWordBinding binding;

    private ArrayList<WordObjectBox> wordObjectBoxesList;
    private SearchedWordAdapter searchedWordAdapter;

    private WordObjectBoxService wordObjectBoxService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchedWordBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();

        wordObjectBoxService = new WordObjectBoxService();

        binding.rvSearchedWord.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        wordObjectBoxesList = new ArrayList<>(wordObjectBoxService.getWordBox());
        searchedWordAdapter = new SearchedWordAdapter(wordObjectBoxesList);
        binding.rvSearchedWord.setAdapter(searchedWordAdapter);

        return viewRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.backArrow.setOnClickListener(v -> {
            int backStackCount = getFragmentManager().getBackStackEntryCount();
            Log.d("BackStack", "BackStack count: " + backStackCount);

            NavHostFragment.findNavController(getParentFragment()).popBackStack();
        });

        binding.backArrow.setOnClickListener(v -> {
            int backStackCount = getFragmentManager().getBackStackEntryCount();
            Log.d("BackStack", "BackStack count: " + backStackCount);

            NavHostFragment.findNavController(getParentFragment()).popBackStack();
        });

        binding.relearn.setOnClickListener(v -> {
            if (NetworkUtil.isNetworkAvailable(v.getContext())) {
                int size = wordObjectBoxesList.size();
                if (RelearnFragment.NUMBER_WORDS > size)
                    RelearnFragment.NUMBER_WORDS = size;
                Navigation.findNavController(v)
                        .navigate(R.id.action_searchedWordFragment_to_relearnFragment);
            } else {
                Toast.makeText(getContext(), "You've not connected to network yet.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        binding.practice.setOnClickListener(v -> {
            int leastWords = PracticeFragment.NUMBER_QUESTIONS;
            if (wordObjectBoxesList.size() >= leastWords)
                Navigation.findNavController(v)
                        .navigate(R.id.action_searchedWordFragment_to_practiceFragment);
            else
                Toast.makeText(getContext(), "You must search at least "
                        + leastWords + " words.", Toast.LENGTH_SHORT).show();
        });

        binding.writingPractice.setOnClickListener(v -> {
            int leastWords = WritingPracticeFragment.NUMBER_QUESTIONS;
            if (wordObjectBoxesList.size() >= leastWords)
                Navigation.findNavController(v)
                        .navigate(R.id.action_searchedWordFragment_to_writingPracticeFragment);
            else
                Toast.makeText(getContext(), "You must search at least "
                        + leastWords + " words.", Toast.LENGTH_SHORT).show();
        });
    }
}
