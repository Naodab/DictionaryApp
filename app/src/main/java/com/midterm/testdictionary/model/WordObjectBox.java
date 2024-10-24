package com.midterm.testdictionary.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class WordObjectBox {
    @Id
    private long id;
    private String word;
    private String phonetic;
    private String audio;

    public WordObjectBox() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}
