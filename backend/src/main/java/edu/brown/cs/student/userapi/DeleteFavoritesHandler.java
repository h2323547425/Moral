package edu.brown.cs.student.userapi;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import edu.brown.cs.student.firebase.FirebaseService;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the DeleteFavoritesHandler class. It handles post requests to the ./favorites/delete
 * endpoint, adds a stock in the indicated user's favorite list.
 *
 * @author Hongyi Li
 */
public class DeleteFavoritesHandler implements Route {
  private final FirebaseService db;
  private final String collectionID;

  /**
   * This is the DeleteFavoritesHandler constructor, which takes in a FirebaseService
   * utilized to access or modify the firestore database.
   *
   * @param db - FirebaseService used to connect the database
   * @param collectionID -  String as the target collectionID in the database
   */
  public DeleteFavoritesHandler(FirebaseService db, String collectionID) {
    this.db = db;
    this.collectionID = collectionID;
  }

  /**
   * This is the override handle method. Takes in a request containing the username
   * and stock ticker, then delete this stock from the user's favorites list.
   *
   * @param request  HTTP request
   * @param response HTTP response
   * @return json message as the response
   * @throws JSONException caused by instantiating JSONObject from request
   */
  @Override
  public Object handle(Request request, Response response) throws JSONException {
    // get the information needed
    JSONObject requestBody = new JSONObject(request.body());
    String user = requestBody.getString("username");
    String ticker = requestBody.getString("stock");

    // get the favorite list using FirebaseService
    Map<String,Object> fields = this.db.getDocument(this.collectionID, user);
    // check if the favorites list exists
    if (fields == null || !fields.containsKey("favorites")) {
      fields = new HashMap<>();
      List<String> favorites = new ArrayList<>();
      fields.put("favorites",favorites);
      this.db.setDocument(this.collectionID,user,fields);
    }

    List<String> favorites = (List<String>) (fields.get("favorites"));
    // remove all stock if the stock argument is "ALL"
    if (ticker.equals("Clear all")) {
      favorites = new ArrayList<>();
      fields.put("favorites",favorites);
      this.db.setDocument(this.collectionID,user,fields);
      // return success message
      String message = "Successfully deleted all stocks from " + user + "'s favorites";
      Map<String,Object> res = ImmutableMap.of("message", message);
      Gson gson = new Gson();
      return gson.toJson(res);
    }

    // add the stock to the favorites list if not already
    if (favorites.contains(ticker)) {
      favorites.remove(ticker);
      this.db.setDocument(this.collectionID,user,fields);
      // return success message
      String message = "Successfully deleted " + ticker + " from " + user + "'s favorites";
      Map<String,Object> res = ImmutableMap.of("message", message);
      Gson gson = new Gson();
      return gson.toJson(res);
    }
    // return success message
    String message = "Cannot find " + ticker + " in " + user + "'s favorites";
    Map<String,Object> res = ImmutableMap.of("message", message);
    Gson gson = new Gson();
    return gson.toJson(res);
  }
}