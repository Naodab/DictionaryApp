package com.midterm.testdictionary.viewmodel;

import android.util.Log;
import android.widget.Toast;

import com.midterm.testdictionary.model.Definition;
import com.midterm.testdictionary.model.Meaning;
import com.midterm.testdictionary.model.ObjectBox;
import com.midterm.testdictionary.model.Phonetic;
import com.midterm.testdictionary.model.Word;
import com.midterm.testdictionary.model.WordObjectBox;
import com.midterm.testdictionary.model.WordObjectBox_;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.Query;

public class WordObjectBoxService {
    private Box<WordObjectBox> wordBox;

    public WordObjectBoxService(){
        wordBox = ObjectBox.get().boxFor(WordObjectBox.class);

        getWordBox();
    }

    public boolean insertWord(Word word){
        if(findWordBox(word)){
            return false;
        }

        WordObjectBox wordObjectBox = new WordObjectBox();
        wordObjectBox.setWord(word.getWord());
        wordObjectBox.setPhonetic(word.getPhonetic());
//        if(!word.getPhonetics().get(0).getAudio().equals(null) && !word.getPhonetics().get(0).getAudio().equals("")) wordObjectBox.setAudio(word.getPhonetics().get(0).getAudio());
//        else wordObjectBox.setAudio("");

        String audio = "";

        for (Phonetic phonetic: word.getPhonetics()) {
            Log.d("DEBUG", phonetic.getAudio());
            if(!phonetic.getAudio().equals("")){
                audio = phonetic.getAudio();
                break;
            }
        }

        String definitionReal = "";
        for (Meaning meaning : word.getMeanings()) {
            boolean isNotEmpty = false;
            for (Definition difinition : meaning.getDefinitions()) {
                if (!difinition.getDefinition().isEmpty()) {
                    isNotEmpty = true;
                    definitionReal = difinition.getDefinition();
                    break;
                }
            }
            if (isNotEmpty)
                break;
        }

        wordObjectBox.setDefinition(definitionReal);
        wordObjectBox.setAudio(audio);

        wordBox.put(wordObjectBox);
        return true;
    }

    public List<WordObjectBox> getWordBox() {
        List<WordObjectBox> words = wordBox.getAll();

        for (WordObjectBox word : words) {
            Log.d("DEBUG", word.getId() + " " + word.getWord());
        }

        return words;
    }

    public boolean deleteWordBox(WordObjectBox wordObjectBox){
        boolean isRemoved = wordBox.remove(wordObjectBox.getId());

        return isRemoved;
    }

    public boolean findWordBox(Word word){
        Query<WordObjectBox> query = wordBox
                .query(WordObjectBox_.word.equal(word.getWord()))
                .build();
        List<WordObjectBox> results = query.find();
        query.close();

        return results.size() > 0;
    }

    public long getSize(){
        return wordBox.count();
    }
}
