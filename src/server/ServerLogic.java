package server;

import com.sun.net.httpserver.HttpExchange;
import domain.Candidate;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import model.DataModel;
import server.cookies.Cookie;
import server.enums.ContentType;
import server.enums.ResponseCodes;
import utility.Utils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ServerLogic extends BasicServer {
    private final static Configuration freemarker = initFreeMarker();

    public ServerLogic(String host, int port) throws IOException {
        super(host, port);
        registerGet("/", this::candidatesHandler);
        registerGet("/votes", this::votesHandler);
        registerPost("/thankyou", this::thankyouFormHandler);
        registerGet("/thankyou", this::thankyouHandler);
    }

    private void candidatesHandler(HttpExchange exchange) {
        DataModel existingDataModel = returnExistingDataModel(exchange);
        HashMap<String, Object> candidates = new HashMap<>();
        candidates.put("candidates", Objects.requireNonNull(existingDataModel).getCandidatesData());
        renderTemplate(exchange, "candidates.html", candidates);
    }

    private void thankyouFormHandler(HttpExchange exchange) {
        DataModel existingDataModel = returnExistingDataModel(exchange);
        Map<String, String> postBody = parsePostBody(exchange);
        String candidateId = postBody.get("candidateId");
        Candidate candidate = Objects.requireNonNull(existingDataModel).getCandidatesData().stream()
                .filter(c -> c.getId().equals(candidateId))
                .findFirst()
                .orElse(null);
        if (candidate == null) {
            sendError(exchange, ResponseCodes.NOT_FOUND, "Выбранный кандидат не найден.");
            return;
        }
        candidate.setVoteCount(candidate.getVoteCount() + 1);
        existingDataModel.setLastVotedCandidate(candidate);
        redirect303(exchange, "/thankyou");
    }

    private void thankyouHandler(HttpExchange exchange) {
        DataModel existingDataModel = returnExistingDataModel(exchange);
        Candidate lastVotedCandidate = Objects.requireNonNull(existingDataModel).getLastVotedCandidate();
        HashMap<String, Object> candidate = new HashMap<>();
        candidate.put("candidate", lastVotedCandidate);
        candidate.put("percentage", existingDataModel.calculatePercentageByCandidateId(lastVotedCandidate.getId()));
        renderTemplate(exchange, "thankyou.html", candidate);
    }

    private void votesHandler(HttpExchange exchange) {
        DataModel existingDataModel = returnExistingDataModel(exchange);
        HashMap<String, Object> candidates = new HashMap<>();
        List<Candidate> sortedCandidates = Objects.requireNonNull(existingDataModel).getCandidatesData().stream()
                .sorted(Comparator.comparing(Candidate::getVoteCount).reversed())
                .collect(Collectors.toList());
        candidates.put("candidates", sortedCandidates);
        candidates.put("percentage", existingDataModel.calculatePercentageForAllCandidates());
        renderTemplate(exchange, "votes.html", candidates);
    }

    private Map<String, String> parsePostBody(HttpExchange exchange) {
        String raw = getRequestBody(exchange);
        return Utils.parseUrlEncoded(raw, "&");
    }

    protected void redirect303(HttpExchange exchange, String path) {
        try {
            exchange.getResponseHeaders().add("Location", path);
            exchange.sendResponseHeaders(303, 0);
            exchange.getResponseBody().close();
        } catch (IOException e) {
            System.out.println("Ошибка " + e.getMessage());
        }
    }

    private static Configuration initFreeMarker() {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
            cfg.setDirectoryForTemplateLoading(new File("data"));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setFallbackOnNullLoopVariable(false);
            return cfg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void renderTemplate(HttpExchange exchange, String templateFile, Object dataModel) {
        try {
            Template temp = freemarker.getTemplate(templateFile);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            try (OutputStreamWriter writer = new OutputStreamWriter(stream)) {
                temp.process(dataModel, writer);
                writer.flush();

                var data = stream.toByteArray();
                sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, data);
            }
        } catch (IOException | TemplateException e) {
            System.out.println("Ошибка " + e.getMessage());
        }
    }

    private DataModel returnExistingDataModel(HttpExchange exchange) {
        String cookie = getCookie(exchange);
        String sessionId = Cookie.parse(cookie).get("sessionId");
        if (sessionId == null || sessionId.isEmpty()) {
            DataModel candidates = new DataModel();
            Cookie setCookie = Session.createSessionCookie(candidates);
            setCookie(exchange, setCookie);
            redirect303(exchange, "/");
            return null;
        }
        return Session.getSession().get(sessionId);
    }
}
