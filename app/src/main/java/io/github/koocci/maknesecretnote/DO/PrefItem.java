package io.github.koocci.maknesecretnote.DO;

/**
 * Created by gujinhyeon on 2018. 8. 28..
 */

public class PrefItem {
    private int pref_id;
    private String name;
    private int score;

    public PrefItem(int pref_id, String name, int score) {
        this.pref_id = pref_id;
        this.name = name;
        this.score = score;
    }

    public int getPref_id() {
        return pref_id;
    }

    public void setPref_id(int pref_id) {
        this.pref_id = pref_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
