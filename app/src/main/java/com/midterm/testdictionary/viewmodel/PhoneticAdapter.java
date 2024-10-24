package com.midterm.testdictionary.viewmodel;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.PhoneticItemBinding;
import com.midterm.testdictionary.model.Phonetic;

import java.util.ArrayList;
import java.util.List;

public class PhoneticAdapter extends RecyclerView.Adapter<PhoneticAdapter.ViewHolder> {
    private final List<Phonetic> phonetics;

    public PhoneticAdapter(List<Phonetic> phonetics) {
        this.phonetics = new ArrayList<>();
        for (Phonetic phonetic : phonetics) {
            if (phonetic.getText() != null) {
                this.phonetics.add(phonetic);
            }
        }
    }

    @NonNull
    @Override
    public PhoneticAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PhoneticItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.phonetic_item,
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneticAdapter.ViewHolder holder, int position) {
        Phonetic phonetic = phonetics.get(position);
        holder.bind(phonetic);
    }

    @Override
    public int getItemCount() {
        return phonetics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public PhoneticItemBinding binding;

        public ViewHolder(PhoneticItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Phonetic phonetic) {
            binding.setPhonetic(phonetic);
            binding.executePendingBindings();
        }
    }
}
