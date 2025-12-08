import domain.Candidate;
import model.DataModel;
import server.ServerLogic;

import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        try {
            DataModel dataModel = new DataModel();
            dataModel.getCandidatesData().stream().forEach(System.out::println);
            for (Candidate candidate : dataModel.getCandidatesData()) {
                System.out.println(candidate.getId() + " " + candidate.getName() + " " + candidate.getPhoto());
            }
            new ServerLogic("localhost", 9889, dataModel).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
