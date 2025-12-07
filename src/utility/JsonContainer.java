package utility;

import domain.Candidate;

import java.util.List;

public class JsonContainer {
    private List<Candidate> candidates;

    public JsonContainer(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }
}
