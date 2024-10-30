package com.midterm.testdictionary.remote;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.midterm.testdictionary.utils.DataModel;
import com.midterm.testdictionary.utils.DataModelType;
import com.midterm.testdictionary.utils.ErrorCallBack;
import com.midterm.testdictionary.utils.NewEventCallback;
import com.midterm.testdictionary.utils.SuccessCallBack;

import java.util.Objects;

public class CallClient {
    private final Gson gson = new Gson();
    private final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
    private DatabaseReference roomsRef = FirebaseDatabase.getInstance().getReference("rooms");
    private String currentUsername;
    private static final String LATEST_EVENT_FIELD_NAME = "latest_event";
    private String currentRoomId;

    public void login(String username, SuccessCallBack callBack) {
        Log.d("Login", username);
        usersRef.child(username).setValue("").addOnCompleteListener(task -> {
            currentUsername = username;
            callBack.onSuccess();
        });
    }
    public void scanRoomAndJoin(SuccessCallBack successCallBack, ErrorCallBack errorCallBack) {
        roomsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean roomFound = false;
                for (DataSnapshot roomSnapshot : snapshot.getChildren()) {
                    String roomId = roomSnapshot.getKey();
                    DataSnapshot offerSnapshot = roomSnapshot.child("offer");
                    DataSnapshot answerSnapshot = roomSnapshot.child("answer");

                    if (offerSnapshot.exists() && !answerSnapshot.exists()) {
                        joinRoom(roomId);
                        roomFound = true;
                        break;
                    }
                }
                if (!roomFound) {
                    createRoom();
                }
                successCallBack.onSuccess();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                errorCallBack.onError();
            }
        });
    }

    public void joinRoom(String roomId) {
        currentRoomId = roomId;
        roomsRef.child(roomId).child("answer").setValue(currentUsername);
        roomsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(roomId).child("offer").exists()) {
                    String target = snapshot.child(roomId).child("offer").getValue(String.class);
                    usersRef.child(currentUsername).child(LATEST_EVENT_FIELD_NAME)
                            .setValue(gson.toJson(new DataModel
                                    (currentUsername, target, null, DataModelType.StartCall)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createRoom() {
        currentRoomId = roomsRef.push().getKey();
        roomsRef.child(currentRoomId).child("offer").setValue(currentUsername);
    }

    public void sendMessageToOtherUser(DataModel dataModel, ErrorCallBack errorCallBack){
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(dataModel.getTarget()).exists()){
                    //send the signal to other user
                    usersRef.child(dataModel.getTarget()).child(LATEST_EVENT_FIELD_NAME)
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
        usersRef.child(currentUsername).child(LATEST_EVENT_FIELD_NAME).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    String data= Objects.requireNonNull(snapshot.getValue()).toString();
                    DataModel dataModel = gson.fromJson(data,DataModel.class);
                    callBack.onNewEventReceived(dataModel);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
