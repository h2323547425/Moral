package edu.brown.cs.student.main;


import edu.brown.cs.student.userapi.UserApiSetup;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.Response;
import spark.Request;
import spark.Route;
import spark.Spark;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static spark.Spark.stop;

/**
 * The Main class of our project. This is where execution begins.
 */

public final class Main {

  private static final int DEFAULT_PORT = 4567;
  private static UserApiSetup userApi;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) throws IOException, SQLException {
    new Main(args).run();
  }

  private String[] args;
  private Map<String, Map<String, String>> stockInfo;
  private Map<String, GenericStock> stockMap;

  private Main(String[] args) {
    this.args = args;
  }

  private RecommenderSystem recSys;

  private void run() throws SQLException, IOException {
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(DEFAULT_PORT);
    parser.accepts("test");

    OptionSet options = parser.parse(args);

    if (!options.has("test")) {
      this.recSys = new RecommenderSystem();
      Map<String, GenericStock> map = this.recSys.symbolToInfo();
      WebScraping scraper = new WebScraping();
      this.stockInfo = scraper.getGrowthRates(map);
      System.out.println(this.stockInfo);
      this.stockMap = scraper.getCleanedList();

      Map<String, Double> normalizeValues =
              this.recSys.generateScore(this.stockMap, 1.0, 1.0,
                      1.0, 1.0, 1.0);

      for (String name : normalizeValues.keySet()) {
        this.stockInfo.get(name).replace("Score", normalizeValues.get(name).toString());
        System.out.println(name + " " + this.stockInfo.get(name).get("Score"));
      }
    }

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

  }


  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.exception(Exception.class, new ExceptionPrinter());
    Spark.externalStaticFileLocation("src/main/resources/static");

    Spark.options("/*", (request, response) -> {
      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }

      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");

      if (accessControlRequestMethod != null) {
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }

      return "OK";
    });

    Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

    // Put Routes Here
    // Spark.get("/table", new TableHandler());
    Spark.get("/stockinfo", new StockInfoHandler(this.stockInfo));
    Spark.get("/stocklist", new stockListHandler(this.stockInfo));
    Spark.get("/stockscores", new StockScoreHandler(this.stockInfo, this.recSys, this.stockMap));

    try {
      // initialize the 3 routes for access, add, and delete to favorite list
      userApi = new UserApiSetup(
          "../config/secret/serviceAccountKey.json", "userstest");
    } catch (IOException e) {
      System.out.println("ERROR: The User API isn't setup correctly.");
    }

    Spark.init();
  }


  /**
   * Handler that sends stock info in a map to the frontend.
   * Format of information. Map<String, Map<String, String>
   * <p>
   * Example: {Tesla, {{price: 100}, {market cap 200B}}}
   */
  private static class StockInfoHandler implements Route {

    private final Map<String, Map<String, String>> stockInfo;
    private final Gson gsonObject = new Gson();

    public StockInfoHandler(Map<String, Map<String, String>> stockInfo) {
      this.stockInfo = stockInfo;

    }

    /**
     * @param req spark request
     * @param res spark response
     * @return a Json file representing entire database
     */
    @Override
    public String handle(Request req, Response res) throws SQLException {
      return gsonObject.toJson(this.stockInfo);
    }
  }

  /**
   * Handler that sends stock rankings to the frontend.
   */
  private static class StockScoreHandler implements Route {

    private final Gson gsonObject = new Gson();
    private final Map<String, Map<String, String>> stockInfo;
    private final RecommenderSystem recSystem;
    private final Map<String, GenericStock> stockMap;

    public StockScoreHandler(Map<String, Map<String, String>> stockInfo, RecommenderSystem recSys,
                             Map<String, GenericStock> stockMap) {
      this.stockInfo = stockInfo;
      this.recSystem = recSys;
      this.stockMap = stockMap;
    }

    /**
     * @param req spark request
     * @param res spark response
     * @return a Json file representing entire database
     */
    @Override
    public String handle(Request req, Response res)  {
      Set<String> params = req.queryParams();
      String filterValues = req.queryParams(params.iterator().next());
      String[] parsedFilterValues = filterValues.split(",");
      Double[] castFilterValues = new Double[5];
      for (int i = 0; i < parsedFilterValues.length; i++) {
        double curr = Double.parseDouble(parsedFilterValues[i]);
        castFilterValues[i] = curr;
      }

      Map<String, Double> normalizeValues =
          this.recSystem.generateScore(this.stockMap, castFilterValues[0], castFilterValues[1],
              castFilterValues[2], castFilterValues[3], castFilterValues[4]);

      for (String name : normalizeValues.keySet()) {
        this.stockInfo.get(name).replace("Score", normalizeValues.get(name).toString());
        System.out.println(name + " " + this.stockInfo.get(name).get("Score"));
      }

      return gsonObject.toJson(normalizeValues);
    }
  }


  /**
   * Handler that sends list of stocks names to frontend.
   */
  private static class stockListHandler implements Route {

    private final Map<String, Map<String, String>> stockInfo;
    private final Gson gsonObject = new Gson();

    public stockListHandler(Map<String, Map<String, String>> stockInfo) {
      this.stockInfo = stockInfo;

    }

    /**
     * @param req spark request
     * @param res spark response
     * @return a Json file representing entire database
     */
    @Override
    public String handle(Request req, Response res) throws SQLException {
      Set<String> stockNames = this.stockInfo.keySet();
      List<String> stockList = new ArrayList<>(stockNames);
      return gsonObject.toJson(stockList);
    }
  }


  /**
   * Display an error page when an exception occurs in the server.
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, spark.Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

  /**
   * Close the user api and the Spark server after the server is no longer in use.
   * Mainly used for testing purposes.
   */
  public static void close() {
    stop();
    userApi.close();
  }

}
