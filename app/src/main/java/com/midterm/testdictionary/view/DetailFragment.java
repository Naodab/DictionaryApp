package com.midterm.testdictionary.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentDetailBinding;
import com.midterm.testdictionary.model.Meaning;
import com.midterm.testdictionary.model.Phonetic;
import com.midterm.testdictionary.model.Word;
import com.midterm.testdictionary.viewmodel.AudioAdapter;
import com.midterm.testdictionary.viewmodel.MeaningAdapter;
import com.midterm.testdictionary.viewmodel.PhoneticAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {
    private Word word;
    public FragmentDetailBinding binding;
    private List<Meaning> meaningList;
    private MeaningAdapter meaningAdapter;
    private ArrayList<Phonetic>  phoneticList;
    private AudioAdapter audioAdapter;
    private PhoneticAdapter phoneticAdapter;

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

        phoneticAdapter = new PhoneticAdapter(phoneticList);
        binding.rvPhonetics.setAdapter(phoneticAdapter);
        FlexboxLayoutManager flexbox = new FlexboxLayoutManager(getContext());
        flexbox.setFlexDirection(FlexDirection.ROW);
        flexbox.setFlexWrap(FlexWrap.WRAP);
        flexbox.setJustifyContent(JustifyContent.FLEX_START);
        flexbox.setAlignItems(AlignItems.CENTER);
        binding.rvPhonetics.setLayoutManager(flexbox);
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