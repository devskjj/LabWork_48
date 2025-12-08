import model.DataModel;
import server.ServerLogic;

import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        try {
            new ServerLogic("localhost", 9889).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
