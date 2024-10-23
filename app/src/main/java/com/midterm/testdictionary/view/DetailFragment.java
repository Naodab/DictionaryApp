package com.midterm.testdictionary.view;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentDetailBinding;
import com.midterm.testdictionary.model.Meaning;
import com.midterm.testdictionary.model.Phonetic;
import com.midterm.testdictionary.model.Word;
import com.midterm.testdictionary.viewmodel.AudioAdapter;
import com.midterm.testdictionary.viewmodel.MeaningAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {
    private Word word;
    public FragmentDetailBinding binding;
    private List<Meaning> meaningList;
    private MeaningAdapter meaningAdapter;
    private ArrayList<Phonetic>  phoneticList;
    private AudioAdapter audioAdapter;

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

        binding.audioRv.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        phoneticList = new ArrayList<>();
        for (Phonetic phonetic: word.getPhonetics()) {
            Log.d("DEBUG", phonetic.getAudio());
            if(!phonetic.getAudio().equals("")) phoneticList.add(phonetic);
        }
        audioAdapter = new AudioAdapter(phoneticList);
        binding.audioRv.setAdapter(audioAdapter);

        return viewRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int backStackCount = getFragmentManager().getBackStackEntryCount();
                Log.d("BackStack", "BackStack count: " + backStackCount);

                NavHostFragment.findNavController(getParentFragment()).popBackStack();
            }
        });
    }
}