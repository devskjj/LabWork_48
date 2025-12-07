package model;

import domain.Candidate;
import utility.JsonUtil;

import java.io.IOException;
import java.util.*;

public class DataModel {
    private List<Candidate> candidatesData;
    private int totalVotes;

    public DataModel() {
        this.candidatesData = new ArrayList<>();
        totalVotes = 0;
        loadData();
    }

    private void loadData() {
        try {
            DataModel data = JsonUtil.load("candidates.json");
            if (data != null) {
                this.candidatesData = data.getCandidatesData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sortByVotes() {
        candidatesData.sort(Comparator.comparing(Candidate::getVoteCount).reversed());
    }

    public int calculatePercentageByCandidateId(int candidateIndex) {
        if (totalVotes == 0) {
            return 0;
        }
        return (int) Math.floor((candidatesData.get(candidateIndex).getVoteCount() / (double) totalVotes) * 100);
    }

    public Map<Candidate, Integer> calculatePercentageForAllCandidates() {
        Map<Candidate, Integer> percentageMap = new HashMap<>();
        if (totalVotes == 0) {
            for (Candidate candidate : candidatesData) {
                percentageMap.put(candidate, 0);
            }
            return percentageMap;
        }

        for (Candidate candidate : candidatesData) {
            percentageMap.put(candidate, (int) Math.floor((candidate.getVoteCount() / (double) totalVotes) * 100));
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
        return totalVotes;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }
}
