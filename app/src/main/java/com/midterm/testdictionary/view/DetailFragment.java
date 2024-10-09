package com.midterm.testdictionary.view;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentDetailBinding;
import com.midterm.testdictionary.model.Meaning;
import com.midterm.testdictionary.model.Word;
import com.midterm.testdictionary.viewmodel.MeaningAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {
    private Word word;
    private FragmentDetailBinding binding;
    private List<Meaning> meaningList;
    private MeaningAdapter meaningAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.d("DetailFragment", "Success");
            word = (Word)getArguments().getSerializable("word");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.fragment_detail, null, false);
        View viewRoot = binding.getRoot();
        binding.setWord(word);
        binding.meaningList.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        meaningList = new ArrayList<>(word.getMeanings());
        meaningAdapter = new MeaningAdapter(meaningList);
        binding.meaningList.setAdapter(meaningAdapter);
        return viewRoot;
    }
}