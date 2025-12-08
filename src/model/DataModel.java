package model;

import domain.Candidate;
import utility.JsonContainer;
import utility.JsonUtil;

import java.io.IOException;
import java.util.*;

public class DataModel {
    private List<Candidate> candidatesData;
    private Candidate lastVotedCandidate;

    public DataModel() {
        this.candidatesData = new ArrayList<>();
        loadData();
        int id = 1000;
        for (Candidate candidate : this.candidatesData) {
            candidate.setId(String.valueOf(id++));
        }
    }

    public void loadData() {
        if (candidatesData.isEmpty()) {
            try {
                JsonContainer data = JsonUtil.load("candidates.json");
                if (data != null) {
                    this.candidatesData = data.getCandidates();
                }
            } catch (IOException e) {
                System.out.println("Ошибка " + e.getMessage());
            }
        }
    }

    public int calculatePercentageByCandidateId(String candidateId) {
        if (getTotalVotes() == 0) {
            return 0;
        }
        Optional<Candidate> candidate = candidatesData.stream().filter(c -> c.getId().equals(candidateId)).findFirst();
        return candidate.map(value -> (int) Math.floor((value.getVoteCount() / (double) getTotalVotes()) * 100)).orElse(0);
    }

    public Map<String, Integer> calculatePercentageForAllCandidates() {
        Map<String, Integer> percentageMap = new HashMap<>();
        if (getTotalVotes() == 0) {
            for (Candidate candidate : candidatesData) {
                percentageMap.put(candidate.getName(), 0);
            }
            return percentageMap;
        }
        for (Candidate candidate : candidatesData) {
            percentageMap.put(candidate.getName(), (int) Math.floor((candidate.getVoteCount() / (double) getTotalVotes()) * 100));
        }
        return percentageMap;
    }

    public List<Candidate> getCandidatesData() {
        try {
            if (candidatesData == null) {
                throw new NullPointerException("Модель пустая");
            }
            return candidatesData;
        } catch (NullPointerException e) {
            System.err.println("Ошибка: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public int getTotalVotes() {
        return candidatesData.stream().mapToInt(Candidate::getVoteCount).sum();
    }

    public Candidate getLastVotedCandidate() {
        return lastVotedCandidate;
    }

    public void setLastVotedCandidate(Candidate lastVotedCandidate) {
        this.lastVotedCandidate = lastVotedCandidate;
    }
}
