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
 * This is the AddFavoritesHandler class. It handles post requests to the ./favorites/add
 * endpoint, adds a stock in the indicated user's favorite list.
 *
 * @author Hongyi Li
 */
public class AddFavoritesHandler implements Route {
  private final FirebaseService db;
  private final String collectionID;

  /**
   * This is the AddFavoritesHandler constructor, which takes in a FirebaseService utilized to
   * access or modify the firestore database.
   *
   * @param db - FirebaseService used to connect the database
   * @param collectionID -  String as the target collectionID in the database
   */
  public AddFavoritesHandler(FirebaseService db, String collectionID) {
    this.db = db;
    this.collectionID = collectionID;
  }

  /**
   * This is the override handle method. Takes in a request containing the username
   * and stock ticker, then add this stock to the user's favorites list.
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
    // add the stock to the favorites list if not already
    if (!favorites.contains(ticker)) {
      // add the new stock to the very front
      favorites.add(0, ticker);
      this.db.setDocument(this.collectionID,user,fields);
      // return success message
      String message = "Successfully added " + ticker + " to " + user + "'s favorites";
      Map<String,Object> res = ImmutableMap.of("message", message);
      Gson gson = new Gson();
      return gson.toJson(res);
    }
    // return success message
    String message = ticker + " is already in " + user + "'s favorites";
    Map<String,Object> res = ImmutableMap.of("message", message);
    Gson gson = new Gson();
    return gson.toJson(res);
  }
}