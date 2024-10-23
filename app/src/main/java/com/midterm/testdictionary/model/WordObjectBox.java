package com.midterm.testdictionary.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class WordObjectBox {
    @Id
    private long id;
    private String word;

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
}
