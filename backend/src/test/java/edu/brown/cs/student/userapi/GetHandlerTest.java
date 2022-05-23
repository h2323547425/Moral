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
 * This class tests the functionality of GetFavoritesHandler by setting up the gui Spark
 * server and make HTTP get requests.
 */
public class GetHandlerTest {

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
   * test if the user api could correctly return the favorites list when trying to
   * access an existing user.
   */
  @Test
  public void testGetExistingList() throws IOException, InterruptedException {

    // construct a HTTP get request
    String reqUri = "http://localhost:4567/favorites";
    String username = "hli129";
    reqUri += "?username=" + username;
    // http://localhost:4567/favorites?username=<email>
    HttpRequest rqst = HttpRequest.newBuilder(URI.create(reqUri))
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

    // res.body()["favorites"]
    // check the map matches expectation
    assertEquals(1, resMap.size());
    List<String> favorites = (List<String>) resMap.get("favorites");
    assertEquals(3, favorites.size());
    assertEquals("a", favorites.get(0));
    assertEquals("b", favorites.get(1));
    assertEquals("c", favorites.get(2));

  }

  /**
   * test if the user api could correctly return empty favorites list when trying to
   * access an non existing user.
   */
  @Test
  public void testGetNonExisting() throws IOException, InterruptedException {

    // construct a HTTP get request
    String reqUri = "http://localhost:4567/favorites";
    String username = "bad_user@gmail.com";
    reqUri += "?username=" + username;
    HttpRequest rqst = HttpRequest.newBuilder(URI.create(reqUri))
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
    List<String> favorites = (List<String>) resMap.get("favorites");
    assertEquals(0, favorites.size());
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
