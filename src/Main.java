import model.DataModel;
import server.ServerLogic;

import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        try {
            DataModel dataModel = new DataModel();
            dataModel.getCandidatesData().stream().forEach(System.out::println);
            new ServerLogic("localhost", 9889, dataModel).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
