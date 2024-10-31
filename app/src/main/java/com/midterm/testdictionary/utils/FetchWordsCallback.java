package com.midterm.testdictionary.utils;

import com.midterm.testdictionary.model.FavouriteWord;

import java.util.List;

public interface FetchWordsCallback {
    void onSuccess(List<FavouriteWord> favouriteWords);
    void onFailure(Exception e);
}
