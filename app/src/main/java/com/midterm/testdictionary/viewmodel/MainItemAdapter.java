package com.midterm.testdictionary.viewmodel;

import android.Manifest;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.midterm.testdictionary.R;
import com.midterm.testdictionary.repository.CallRepository;
import com.permissionx.guolindev.PermissionX;

import java.util.ArrayList;

public class MainItemAdapter extends RecyclerView.Adapter<MainItemAdapter.ViewHolder>{
    private ArrayList<String> itemsList;
    private CallRepository callRepository;
    private Fragment fragment;

    public MainItemAdapter(ArrayList<String> itemsList, Fragment fragment){
        this.itemsList = itemsList;
        this.fragment = fragment;
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
                    } else if(getAdapterPosition() == 1 || getAdapterPosition() == 2){
                        if(currentUser == null){
                            Toast.makeText(view.getContext(), "You must login", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(view).navigate(R.id.loginFragment);

                            return;
                        }
                        if (getAdapterPosition() == 2) {
                            callAssignment(currentUser.getEmail().split("\\.")[0]);
                        } else if (getAdapterPosition() == 1) {
                            Navigation.findNavController(view)
                                    .navigate(R.id.action_mainFragment_to_yourWordFragment);
                        }
                    }
                }
            });
        }

        private void callAssignment(String username) {
            callRepository = CallRepository.getInstance();
            PermissionX.init(fragment)
                .permissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                .request((allGranted, grantedList, deniedList)  -> {
                        if (allGranted) {
                            // sua tu username thanh email
                            callRepository.login(username, fragment.getContext(),
                                    () -> {
                                        Navigation.findNavController(this.itemView).navigate(R.id.action_mainFragment_to_callFragment);
                                    }
                            );
                        } else {
                            Log.e("Permissions", "Quyền truy cập bị từ chối.");
                        }
                    }
                );
        }
    }
}
