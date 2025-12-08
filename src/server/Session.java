package server;

import model.DataModel;
import server.cookies.Cookie;

import java.util.*;

public class Session {
    private static Map<String, DataModel> userSession = new HashMap<>();

    private Session() {}

    private static String createSessionId() {
        return UUID.randomUUID().toString();
    }

    public static Cookie createSessionCookie(DataModel dataModel) {
        String sessionId = createSessionId();
        userSession.put(sessionId, dataModel);
        return Cookie.make("sessionId", sessionId, 600, true);
    }

    public static void remove(String sessionId) {
        userSession.remove(sessionId);
    }

    public static Map<String, DataModel> getSession() {
        return userSession;
    }
}