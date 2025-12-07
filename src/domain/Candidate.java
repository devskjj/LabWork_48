package domain;

public class Candidate {
    private final String name;
    private final String photo;
    private transient int voteCount;

    public Candidate(String name, String photo) {
        this.name = name;
        this.photo = photo;
        this.voteCount = 0;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
