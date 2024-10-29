package com.midterm.testdictionary.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.MeaningItemBinding;
import com.midterm.testdictionary.model.Meaning;

import java.util.List;

public class MeaningAdapter extends RecyclerView.Adapter<MeaningAdapter.ViewHolder> {
    private List<Meaning> meaningList;
    private boolean canPressAtSynonymOrAntonym = true;

    public MeaningAdapter(List<Meaning> meaningList) {
        this.meaningList = meaningList;
    }

    public MeaningAdapter(List<Meaning> meaningList, boolean canPressAtSynonymOrAntonym) {
        this.meaningList = meaningList;
        this.canPressAtSynonymOrAntonym = canPressAtSynonymOrAntonym;
    }

    @NonNull
    @Override
    public MeaningAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MeaningItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.meaning_item,
                parent,
                false);
        View viewRoot = binding.getRoot();
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MeaningAdapter.ViewHolder holder, int position) {
        Meaning meaning = meaningList.get(position);
        holder.bind(meaning);
    }

    public void setData(List<Meaning> meaningList) {
        this.meaningList = meaningList;
    }

    @Override
    public int getItemCount() {
        return this.meaningList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public MeaningItemBinding binding;

        public ViewHolder(MeaningItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Meaning meaning) {
            binding.setMeaning(meaning);
            binding.executePendingBindings();

            // Setup RecyclerView cho Definition
            DefinitionAdapter definitionAdapter = new DefinitionAdapter(meaning.getDefinitions());
            binding.definitionList.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
            binding.definitionList.setAdapter(definitionAdapter);

            if (meaning.getSynonyms() != null && meaning.getSynonyms().size() > 0) {
                SynoAntonymAdapter synonymAdapter = new SynoAntonymAdapter(meaning.getSynonyms(),
                        canPressAtSynonymOrAntonym);
                binding.rvSynonym.setAdapter(synonymAdapter);
                FlexboxLayoutManager flexbox = new FlexboxLayoutManager(this.itemView.getContext());
                flexbox.setFlexDirection(FlexDirection.ROW);
                flexbox.setFlexWrap(FlexWrap.WRAP);
                flexbox.setJustifyContent(JustifyContent.FLEX_START);
                flexbox.setAlignItems(AlignItems.CENTER);
                binding.rvSynonym.setLayoutManager(flexbox);
            } else {
                binding.lbSynonym.setVisibility(View.GONE);
                binding.rvSynonym.setVisibility(View.GONE);
            }

            if (meaning.getAntonyms() != null && meaning.getAntonyms().size() > 0) {
                SynoAntonymAdapter antonymAdapter = new SynoAntonymAdapter(meaning.getAntonyms(),
                        canPressAtSynonymOrAntonym);
                binding.rvAntonym.setAdapter(antonymAdapter);
                FlexboxLayoutManager flexbox = new FlexboxLayoutManager(this.itemView.getContext());
                flexbox.setFlexDirection(FlexDirection.ROW);
                flexbox.setFlexWrap(FlexWrap.WRAP);
                flexbox.setJustifyContent(JustifyContent.FLEX_START);
                flexbox.setAlignItems(AlignItems.CENTER);
                binding.rvAntonym.setLayoutManager(flexbox);
            } else {
                binding.lbAntonym.setVisibility(View.GONE);
                binding.rvAntonym.setVisibility(View.GONE);
            }
        }
    }
}
