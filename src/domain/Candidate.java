package domain;

public class Candidate {
    private transient String id;
    private final String name;
    private final String photo;
    private transient int voteCount;

    public Candidate(String name, String photo) {
        this.id = "";
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Candidate{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
