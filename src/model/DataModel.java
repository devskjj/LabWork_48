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

    public int calculatePercentageByCandidateId(int candidateIndex) {
        if (getTotalVotes() == 0) {
            return 0;
        }
        return (int) Math.floor((candidatesData.get(candidateIndex).getVoteCount() / (double) getTotalVotes()) * 100);
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
