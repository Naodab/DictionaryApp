package com.midterm.testdictionary.viewmodel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.midterm.testdictionary.model.FavouriteWord;
import com.midterm.testdictionary.model.WordObjectBox;
import com.midterm.testdictionary.utils.CheckWordCallBack;
import com.midterm.testdictionary.utils.ErrorCallBack;
import com.midterm.testdictionary.utils.FetchWordsCallback;
import com.midterm.testdictionary.utils.SuccessCallBack;

import java.util.ArrayList;
import java.util.List;

public class FavouriteWordService {
    private static final String USER_FIELD = "users";
    private static final String FAVOURITE_WORDS_FIELD = "favouriteWords";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private static FavouriteWordService instance;
    private String currentUser;

    public static FavouriteWordService getInstance() {
        if (instance == null)
            instance = new FavouriteWordService();
        return instance;
    }

    private FavouriteWordService() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void getAll(FetchWordsCallback callback) {
        currentUser = mAuth.getCurrentUser().getEmail().split("\\.")[0];
        db.collection(USER_FIELD).document(currentUser).collection(FAVOURITE_WORDS_FIELD)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<FavouriteWord> words = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        words.add(document.toObject(FavouriteWord.class));
                    }
                    callback.onSuccess(words);
                } else {
                    callback.onFailure(task.getException());
                }
            }
        );
    }

    public void insertFavouriteWord(FavouriteWord word, FetchWordsCallback callback) {
        currentUser = mAuth.getCurrentUser().getEmail().split("\\.")[0];
        CollectionReference favouriteRef = db.collection(USER_FIELD)
                .document(currentUser)
                .collection(FAVOURITE_WORDS_FIELD);

        favouriteRef.document(word.getWord())
                .set(word)
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(e -> callback.onFailure(null));
    }

    public void deleteFavouriteWord(String word, SuccessCallBack callback,
                                    ErrorCallBack errorCallBack) {
        currentUser = mAuth.getCurrentUser().getEmail().split("\\.")[0];
        db.collection(USER_FIELD).document(currentUser).collection(FAVOURITE_WORDS_FIELD).document(word)
                .delete()
                .addOnCompleteListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> errorCallBack.onError());
    }

    public void checkIfWordExists(String word, CheckWordCallBack callBack) {
        currentUser = mAuth.getCurrentUser().getEmail().split("\\.")[0];
        db.collection(USER_FIELD).document(currentUser).collection(FAVOURITE_WORDS_FIELD)
            .document(word)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists())
                        callBack.onExists(true);
                    else
                        callBack.onExists(false);
                } else {
                    callBack.onExists(false);
                }
            });
    }
}
