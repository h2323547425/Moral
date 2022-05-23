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
 * This class tests the functionality of AddFavoritesHandler by setting up the gui Spark
 * server and make HTTP post requests.
 */
public class AddHandlerTest {

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
   * test if the user api could correctly adds value to a valid favorites list that
   * not yet contain the value
   */
  @Test
  public void testAddToValidList() throws IOException, InterruptedException {

    // construct a HTTP post request
    String reqUri = "http://localhost:4567/favorites/add";
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
    assertEquals("Successfully added STOCK to hli129's favorites", message);

  }

  /**
   * test if the user api could correctly adds value to a valid favorites list that
   * already contains the value
   */
  @Test
  public void testAddToListAlreadyExisted() throws IOException, InterruptedException {
    // construct a HTTP post request
    String reqUri = "http://localhost:4567/favorites/add";
    String username = "hli129";
    String stockTicker = "a";
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
    assertEquals("a is already in hli129's favorites", message);
  }

  /**
   * Close the Spark server and firebase connection after using Main.
   */
  @AfterClass
  public static void end() throws InterruptedException {
    System.out.println("HIIII close");
    Main.close();
    Thread.sleep(500);
  }

}
