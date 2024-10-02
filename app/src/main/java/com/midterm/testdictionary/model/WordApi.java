package com.midterm.testdictionary.model;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WordApi {
    @GET("entries/en/{word}")
    Single<List<Word>> getWordDefinition(@Path("word") String word);
}
