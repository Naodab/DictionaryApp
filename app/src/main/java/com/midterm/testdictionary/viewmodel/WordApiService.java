package com.midterm.testdictionary.viewmodel;

import com.midterm.testdictionary.model.Word;
import com.midterm.testdictionary.model.WordApi;

import java.util.List;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WordApiService {
    private static final String BASE_URL="https://api.dictionaryapi.dev/api/v2/";
    private WordApi api;

    public WordApiService(){
        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(WordApi.class);
    }

    public Single<List<Word>> getWordDefinition(String word){
        return api.getWordDefinition(word);
    }
}
