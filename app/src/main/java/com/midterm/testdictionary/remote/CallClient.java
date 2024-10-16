package com.midterm.testdictionary.remote;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.midterm.testdictionary.utils.DataModel;
import com.midterm.testdictionary.utils.ErrorCallBack;
import com.midterm.testdictionary.utils.NewEventCallback;
import com.midterm.testdictionary.utils.SuccessCallBack;

import java.util.Objects;

public class CallClient {
    private final Gson gson = new Gson();
    private final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");
    private String currentUsername;
    private static final String LATEST_EVENT_FIELD_NAME = "latest_event";

    public void login(String username, SuccessCallBack callBack) {
        dbRef.child(username).setValue("").addOnCompleteListener(v -> {
            currentUsername = username;
            callBack.onSuccess();
        });
    }

    public void sendMessageToOtherUser(DataModel dataModel, ErrorCallBack errorCallBack) {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(dataModel.getTarget()).exists()) {
                    dbRef.child(dataModel.getTarget()).child(LATEST_EVENT_FIELD_NAME)
                            .setValue(gson.toJson(dataModel));
                } else {
                    errorCallBack.onError();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                errorCallBack.onError();
            }
        });
    }

    public void observeIncomingLatestEvent(NewEventCallback callBack){
        dbRef.child(currentUsername).child(LATEST_EVENT_FIELD_NAME).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try{
                            String data= Objects.requireNonNull(snapshot.getValue()).toString();
                            DataModel dataModel = gson.fromJson(data,DataModel.class);
                            callBack.onNewEventReceived(dataModel);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                }
        );


    }
}
