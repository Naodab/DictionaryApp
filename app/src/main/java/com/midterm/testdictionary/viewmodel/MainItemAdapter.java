package com.midterm.testdictionary.viewmodel;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.midterm.testdictionary.R;
import com.midterm.testdictionary.repository.CallRepository;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
//        Picasso.get().load(dogsList.get(position).getUrl()).resize(200, 200).into(holder.ivAvatar);
        holder.tvContent.setText(itemsList.get(position));
//
//        holder.heart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (holder.heart.getTag() != null && holder.heart.getTag().equals("filled")) {
//                    holder.heart.setImageResource(R.drawable.heart);
//                    holder.heart.setTag("unfilled");
//                } else {
//                    // If the heart is unfilled, switch to filled
//                    holder.heart.setImageResource(R.drawable.clicked_heart);
//                    holder.heart.setTag("filled");
//                }
////                Log.d("heart", holder.heart.getTag().toString());
//
////                holder.heart.setImageResource(R.drawable.clicked_heart);
//
//                int width = holder.heart.getLayoutParams().width;
//                int height = holder.heart.getLayoutParams().height;
//
//                holder.heart.getLayoutParams().width = width;
//                holder.heart.getLayoutParams().height = height;
//                holder.heart.requestLayout();
//
////                holder.heart.getLayoutParams().width = 25;
////                holder.heart.getLayoutParams().height = 25;
//            }
//        });
    }
    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvContent;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            tvContent = (TextView) view.findViewById(R.id.tv_content);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tvContent.getText().toString().equals("Thực hành Tiếng Anh")) {
                        Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_callFragment);
                    }
                }
            });

//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    DogBreed dog = dogsList.get(getAdapterPosition());
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("dogBreed", dog);
//                    Navigation.findNavController(view).navigate(R.id.detailsFragment, bundle);
//                }
//            });
        }
    }
}
