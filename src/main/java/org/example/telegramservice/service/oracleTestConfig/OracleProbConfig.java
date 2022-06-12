package org.example.telegramservice.service.oracleTestConfig;

import java.util.List;

public class OracleProbConfig {

    public OracleProbConfig() {}

    public OracleProbConfig(List<Problem> problems) {
        this.problems = problems;
    }

    private List<Problem> problems;

    public List<Problem> getProblems() {
        return problems;
    }

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }
}