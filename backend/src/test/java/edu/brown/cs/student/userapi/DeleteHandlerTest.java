package edu.brown.cs.student.userapi;

import com.google.gson.Gson;
import edu.brown.cs.student.main.Main;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the functionality of DeleteFavoritesHandler by setting up the gui Spark
 * server and make HTTP post requests.
 */
public class DeleteHandlerTest {

  /**
   * Run the Spark server using Main.
   */
  @BeforeClass
  public static void setup() {
    try {
      String[] args = new String[]{"--gui", "--test"};
      Main.main(args);
      Thread.sleep(500);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * test if the user api could correctly deletes value from a valid favorites list that
   * contains the value
   */
  @Test
  public void testDeleteToValidList() throws IOException, InterruptedException {

    // construct a HTTP post request
    String reqUri = "http://localhost:4567/favorites/delete";
    String username = "hli129";
    String stockTicker = "STOCK";
    HttpRequest rqst = HttpRequest.newBuilder(URI.create(reqUri))
        .POST(HttpRequest.BodyPublishers.ofString(
            "{\"username\":\"" + username + "\", \"stock\":\"" + stockTicker + "\"}"))
        .header("Access-Control-Allow-Origin", "*").build();

    // construct a HttpClient
    HttpClient.Builder cBuilder = HttpClient.newBuilder();
    cBuilder.version(HttpClient.Version.HTTP_2);
    cBuilder.connectTimeout(Duration.ofSeconds(60));
    HttpClient client = cBuilder.build();

    // make a HTTP request and parse the response
    HttpResponse<String> apiResponse = client.send(rqst, HttpResponse.BodyHandlers.ofString());

    Gson parser = new Gson();
    Map resMap = parser.fromJson(apiResponse.body(), Map.class);

    // check the map matches expectation
    assertEquals(1, resMap.size());
    String message = (String) resMap.get("message");
    assertEquals("Successfully deleted STOCK from hli129's favorites", message);

  }

  /**
   * test if the user api could correctly deletes all values from a valid favorites list that
   * contains the value
   */
  @Test
  public void testDeleteAllToValidList() throws IOException, InterruptedException {

    // construct a HTTP post request
    String reqUri = "http://localhost:4567/favorites/delete";
    String username = "delete-all";
    String stockTicker = "Clear all";
    HttpRequest rqst = HttpRequest.newBuilder(URI.create(reqUri))
            .POST(HttpRequest.BodyPublishers.ofString(
                    "{\"username\":\"" + username + "\", \"stock\":\"" + stockTicker + "\"}"))
            .header("Access-Control-Allow-Origin", "*").build();

    // construct a HttpClient
    HttpClient.Builder cBuilder = HttpClient.newBuilder();
    cBuilder.version(HttpClient.Version.HTTP_2);
    cBuilder.connectTimeout(Duration.ofSeconds(60));
    HttpClient client = cBuilder.build();

    // make a HTTP request and parse the response
    HttpResponse<String> apiResponse = client.send(rqst, HttpResponse.BodyHandlers.ofString());

    Gson parser = new Gson();
    Map resMap = parser.fromJson(apiResponse.body(), Map.class);

    // check the map matches expectation
    assertEquals(1, resMap.size());
    String message = (String) resMap.get("message");
    assertEquals("Successfully deleted all stocks from delete-all's favorites", message);

    // construct get request to check the list
    reqUri = "http://localhost:4567/favorites";
    reqUri += "?username=" + username;
    // http://localhost:4567/favorites?username=<email>
    rqst = HttpRequest.newBuilder(URI.create(reqUri))
            .header("Access-Control-Allow-Origin", "*").build();

    apiResponse = client.send(rqst, HttpResponse.BodyHandlers.ofString());
    resMap = parser.fromJson(apiResponse.body(), Map.class);
    List<String> favorites = (List<String>) resMap.get("favorites");
    assertEquals(0, favorites.size());

    // restore the values
    // construct a HTTP post request
    reqUri = "http://localhost:4567/favorites/add";
    stockTicker = "1";
    rqst = HttpRequest.newBuilder(URI.create(reqUri))
            .POST(HttpRequest.BodyPublishers.ofString(
                    "{\"username\":\"" + username + "\", \"stock\":\"" + stockTicker + "\"}"))
            .header("Access-Control-Allow-Origin", "*").build();
    client.send(rqst, HttpResponse.BodyHandlers.ofString());
    // construct a HTTP post request
    stockTicker = "2";
    rqst = HttpRequest.newBuilder(URI.create(reqUri))
            .POST(HttpRequest.BodyPublishers.ofString(
                    "{\"username\":\"" + username + "\", \"stock\":\"" + stockTicker + "\"}"))
            .header("Access-Control-Allow-Origin", "*").build();
    client.send(rqst, HttpResponse.BodyHandlers.ofString());
    // construct a HTTP post request
    stockTicker = "3";
    rqst = HttpRequest.newBuilder(URI.create(reqUri))
            .POST(HttpRequest.BodyPublishers.ofString(
                    "{\"username\":\"" + username + "\", \"stock\":\"" + stockTicker + "\"}"))
            .header("Access-Control-Allow-Origin", "*").build();
    client.send(rqst, HttpResponse.BodyHandlers.ofString());

  }

  /**
   * test if the user api could correctly deletes value from a valid favorites list that
   * do not contain the value
   */
  @Test
  public void testDeleteFromListNotContain() throws IOException, InterruptedException {
    // construct a HTTP post request
    String reqUri = "http://localhost:4567/favorites/delete";
    String username = "hli129";
    String stockTicker = "WEIRD_STUFF";
    HttpRequest rqst = HttpRequest.newBuilder(URI.create(reqUri))
        .POST(HttpRequest.BodyPublishers.ofString(
            "{\"username\":\"" + username + "\", \"stock\":\"" + stockTicker + "\"}"))
        .header("Access-Control-Allow-Origin", "*").build();

    // construct a HttpClient
    HttpClient.Builder cBuilder = HttpClient.newBuilder();
    cBuilder.version(HttpClient.Version.HTTP_2);
    cBuilder.connectTimeout(Duration.ofSeconds(60));
    HttpClient client = cBuilder.build();

    // make a HTTP request and parse the response
    HttpResponse<String> apiResponse = client.send(rqst, HttpResponse.BodyHandlers.ofString());

    Gson parser = new Gson();
    Map resMap = parser.fromJson(apiResponse.body(), Map.class);

    // check the map matches expectation
    assertEquals(1, resMap.size());
    String message = (String) resMap.get("message");
    assertEquals("Cannot find WEIRD_STUFF in hli129's favorites", message);

  }

  /**
   * Close the Spark server and firebase connection after using Main.
   */
  @AfterClass
  public static void end() throws InterruptedException {
    Main.close();
    Thread.sleep(500);
  }
}
