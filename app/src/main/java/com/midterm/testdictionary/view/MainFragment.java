package com.midterm.testdictionary.view;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.midterm.testdictionary.R;
import com.midterm.testdictionary.databinding.FragmentMainBinding;
import com.midterm.testdictionary.model.ObjectBox;
import com.midterm.testdictionary.model.Word;
import com.midterm.testdictionary.model.WordObjectBox;
import com.midterm.testdictionary.viewmodel.MainItemAdapter;
import com.midterm.testdictionary.viewmodel.WordApiService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.objectbox.Box;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    private ArrayList<String> itemList;
    private MainItemAdapter  itemsAdapter;

    private WordApiService apiService;
    private Word searchedWord;

    private Box<WordObjectBox> wordBox;

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiService = new WordApiService();

        wordBox = ObjectBox.get().boxFor(WordObjectBox.class);

        getWordBox();

        binding.rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        itemList = new ArrayList<>();
        itemsAdapter = new MainItemAdapter(itemList);
        binding.rvItems.setAdapter(itemsAdapter);
        setMainItem();

        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String word = binding.search.getText().toString();
                setWordItems(word);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String word = binding.search.getText().toString();
                    performSearch(word);
                    return true;
                }
                return false;
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_main, container, false);
        binding = FragmentMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }
    public void setMainItem(){
        itemList.add("Từ đã tra");
        itemList.add("Từ của bạn");
        itemList.add("Dịch văn bản");
        itemList.add("Thực hành Tiếng Anh");
        itemList.add("Cài đặt");
        itemsAdapter.notifyDataSetChanged();
    }

    public Word performSearch(String word) {
        final Word[] result = {null};
        DisposableSingleObserver<List<Word>> disposableSingleObserver = apiService.getWordDefinition(word)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<List<Word>>() {
                @Override
                public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<Word> words) {
                    Log.d("DEBUG", "Success");
                    searchedWord = words.get(0);
                    playAudio(searchedWord.getPhonetics().get(0).getAudio());

                    if (searchedWord != null) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("word", searchedWord);
                        View view = getView();
                        if (view != null) {
//                            insertWord(searchedWord);

                            Navigation.findNavController(view).navigate(R.id.detailFragment, bundle);
                        }



//                        Navigation.findNavController(view).navigate(R.id.detailFragment, bundle);
                    }
                }

                @Override
                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                    Log.d("DEBUG", "Fail" + e.getMessage());
                    e.printStackTrace();
                }
            });
        return result[0];
    }

    public void setWordItems(String word){
        DisposableSingleObserver<List<Word>> disposableSingleObserver = apiService.getWordDefinition(word)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<List<Word>>() {
                @Override
                public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<Word> words) {
                    Log.d("DEBUG", "Success");

                    for (Word word1 : words) {
                        Log.d("DEBUG", word1.getMeanings().get(0).getDefinitions().get(0).getDefinition());
                    }
                }

                @Override
                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                    Log.d("DEBUG", "Fail" + e.getMessage());
                    e.printStackTrace();
                }
            });
    }

    public void insertWord(Word word){
        WordObjectBox wordObjectBox = new WordObjectBox();
        wordObjectBox.setWord(word.getWord());

        wordBox.put(wordObjectBox);
    }

    public void getWordBox(){
        List<WordObjectBox> words = wordBox.getAll();

        for (WordObjectBox word: words) {
            Log.d("DEBUG", word.getWord());
        }
    }

    private void playAudio(String audioUrl) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });

            // Stop the audio automatically once it's completed
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopAudio();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}