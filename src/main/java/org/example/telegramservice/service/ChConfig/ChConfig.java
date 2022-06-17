package org.example.telegramservice.service.ChConfig;

import java.util.List;

public class ChConfig {

    String chapter;
    List<Question> questions;


    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}