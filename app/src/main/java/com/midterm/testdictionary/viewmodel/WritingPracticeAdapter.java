package com.midterm.testdictionary.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.WritingItemBinding;

import java.util.ArrayList;
import java.util.List;

public class WritingPracticeAdapter extends RecyclerView.Adapter<WritingPracticeAdapter.ViewHolder> {
    private List<String> chars;
    private EditText input;

    public WritingPracticeAdapter(List<String> chars, EditText input) {
        this.chars = new ArrayList<>();
        this.chars.addAll(chars);
        this.input = input;
    }

    @NonNull
    @Override
    public WritingPracticeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WritingItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.writing_item,
                parent,
                false);
        return new WritingPracticeAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WritingPracticeAdapter.ViewHolder holder, int position) {
        String letter = chars.get(position);
        holder.bind(letter);
    }

    @Override
    public int getItemCount() {
        return chars.size();
    }

    public void setData(List<String> chars) {
        this.chars = new ArrayList<>();
        this.chars.addAll(chars);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        WritingItemBinding binding;

        public ViewHolder(WritingItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    String letter = chars.get(position);
                    chars.remove(position);
                    notifyItemRemoved(position);
                    input.setText(input.getText().toString() + letter);
                }
            });
        }

        public void bind(String letter) {
            binding.setLetter(letter);
        }
    }
}