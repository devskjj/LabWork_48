package server;

import server.cookies.Cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Session {
    private static Map<String, List<DataModel>> userSession = new HashMap<>();

    private Session() {}

    private static String createSessionId() {
        return UUID.randomUUID().toString();
    }

    public static Cookie createSessionCookie() {
        String sessionId = createSessionId();
        userSession.put(sessionId, new ArrayList<DataModel>());
        return Cookie.make("sessionId", sessionId, -1, true);
    }

    private static void add(String name, DataModel dataModel) {
        userSession.computeIfAbsent(name, v -> new ArrayList<DataModel>())
                .add(dataModel);
    }

    public static void remove(String sessionId) {
        userSession.remove(sessionId);
    }

    public static Map<String, List<DataModel>> getSession() {
        return userSession;
    }
}