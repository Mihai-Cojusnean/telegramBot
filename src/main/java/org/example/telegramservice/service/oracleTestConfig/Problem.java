package org.example.telegramservice.service.oracleTestConfig;

import java.util.List;

public class Problem {
    private int prbNr;
    private String question;
    private String responses;
    List<String> response_id;
    private String answer;
    private String answerExplication;

    public int getPrbNr() {
        return prbNr;
    }

    public void setPrbNr(int prbNr) {
        this.prbNr = prbNr;
    }
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getResponses() {
        return responses;
    }

    public void setResponses(String responses) {
        this.responses = responses;
    }

    public String getAnswerExplication() {
        return answerExplication;
    }

    public void setAnswerExplication(String answerExplication) {
        this.answerExplication = answerExplication;
    }

    public List<String> getResponse_id() {
        return response_id;
    }

    public void setResponse_id(List<String> response_id) {
        this.response_id = response_id;
    }
}
