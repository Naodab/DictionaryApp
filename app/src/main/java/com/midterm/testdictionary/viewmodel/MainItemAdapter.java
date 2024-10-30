package com.midterm.testdictionary.viewmodel;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.midterm.testdictionary.R;
import com.midterm.testdictionary.model.ObjectBox;
import com.midterm.testdictionary.model.Word;
import com.midterm.testdictionary.model.WordObjectBox;
import com.midterm.testdictionary.repository.CallRepository;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;

public class MainItemAdapter extends RecyclerView.Adapter<MainItemAdapter.ViewHolder>{
    private ArrayList<String> itemsList;
    private CallRepository callRepository = CallRepository.getInstance();

    public MainItemAdapter(ArrayList<String> itemsList){
        this.itemsList = itemsList;
    }
    @NonNull
    @Override
    public MainItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_rows_item, parent, false);

        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MainItemAdapter.ViewHolder holder, int position) {
        holder.tvContent.setText(itemsList.get(position));

    }
    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvContent;
        private FirebaseAuth mAuth;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            mAuth = FirebaseAuth.getInstance();
            tvContent = (TextView) view.findViewById(R.id.tv_content);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    if(getAdapterPosition() == 0){
                        Navigation.findNavController(view).navigate(R.id.searchedWordFragment);
                    }else if(getAdapterPosition() == 1 || getAdapterPosition() == 3){
                        if(currentUser == null){
                            Toast.makeText(view.getContext(), "You must login", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(view).navigate(R.id.loginFragment);

                            return;
                        }
                        if (getAdapterPosition() == 3) {
                            // TODO: login to call repository
                            // Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_callFragment);
                        } else if (getAdapterPosition() == 1) {

                        }
                    }else if(getAdapterPosition() == 2){
                        // TODO: text translate
                    }
                }
            });
        }
    }
}
