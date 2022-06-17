package org.example.telegramservice.service.ChConfig;

import java.util.List;

public class Question{

    String id;
    String question;
    List<String> rsp_id;
    List<String> answer;
    String explication;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getRsp_id() {
        return rsp_id;
    }

    public void setRsp_id(List<String> rsp_id) {
        this.rsp_id = rsp_id;
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

    public String getExplication() {
        return explication;
    }

    public void setExplication(String explication) {
        this.explication = explication;
    }
}