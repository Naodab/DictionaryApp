package com.midterm.testdictionary.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.DefinitionItemBinding;
import com.midterm.testdictionary.model.Definition;

import java.util.List;

public class DefinitionAdapter extends RecyclerView.Adapter<DefinitionAdapter.ViewHolder>{
    private List<Definition> diDefinitionList;

    public DefinitionAdapter(List<Definition> diDefinitionList) {
        this.diDefinitionList = diDefinitionList;
    }

    @NonNull
    @Override
    public DefinitionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DefinitionItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.definition_item,
                parent,
                false);
        View view = binding.getRoot();
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DefinitionAdapter.ViewHolder holder, int position) {
        Definition definition = diDefinitionList.get(position);
        holder.binding.setDefinition(definition);
    }

    @Override
    public int getItemCount() {
        return diDefinitionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public DefinitionItemBinding binding;

        public ViewHolder(DefinitionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
