package com.midterm.testdictionary.model;

public class FavouriteWord {
    private String word;
    private String phonetic;
    private String definition;

    public FavouriteWord() {}

    public FavouriteWord(String word, String phonetic, String definition) {
        this.word = word;
        this.phonetic = phonetic;
        this.definition = definition;
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

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
