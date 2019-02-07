package com.mheleon.mycapmquiz;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class CapmQA extends RealmObject {
    @PrimaryKey
    private long id;
    private long id_chapter;
    private String question;
    private String a;
    private String b;
    private String c;
    private String d;
    private String answer;

    public CapmQA(long id, long id_chapter, String question, String a, String b, String c, String d, String answer) {
        this.id = id;
        this.id_chapter = id_chapter;
        this.question = question;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.answer = answer;
    }

    public CapmQA() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId_chapter() {
        return id_chapter;
    }

    public void setId_chapter(long id_chapter) {
        this.id_chapter = id_chapter;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
