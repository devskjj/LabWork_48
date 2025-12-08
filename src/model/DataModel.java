package model;

import domain.Candidate;
import utility.JsonContainer;
import utility.JsonUtil;

import java.io.IOException;
import java.util.*;

public class DataModel {
    private List<Candidate> candidatesData;

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
                e.printStackTrace();
            }
        }
    }

    public void sortByVotes() {
        candidatesData.sort(Comparator.comparing(Candidate::getVoteCount).reversed());
    }

    public int calculatePercentageByCandidateId(String candidateId) {
        if (getTotalVotes() == 0) {
            return 0;
        }
        Optional<Candidate> candidate = candidatesData.stream().filter(c -> c.getId().equals(candidateId)).findFirst();
        if (candidate.isPresent()) {
            return (int) Math.floor((candidate.get().getVoteCount() / (double) getTotalVotes()) * 100);
        }
        return 0;
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

    public Candidate getCandidateById(int candidateIndex) {
        if (candidateIndex > 0 && candidateIndex <= candidatesData.size()) {
            return candidatesData.get(candidateIndex - 1);
        } else {
            return candidatesData.getFirst();
        }
    }

    public List<Candidate> getCandidatesData() {
        return candidatesData;
    }

    public int getTotalVotes() {
        return candidatesData.stream().mapToInt(Candidate::getVoteCount).sum();
    }
}
