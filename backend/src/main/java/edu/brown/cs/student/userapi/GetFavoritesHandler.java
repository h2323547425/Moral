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
 * This is the GetFavoritesHandler class. It handles get requests to the ./favorites endpoint,
 * and creates a response including all the stocks in the indicated user's favorite list.
 *
 * @author Hongyi Li
 */
public class GetFavoritesHandler implements Route {
  private final FirebaseService db;
  private final String collectionID;

  /**
   * This is the FavoritesHandler constructor, which takes in a FirebaseService utilized to
   * access or modify the firestore database.
   *
   * @param db - FirebaseService used to connect the database
   * @param collectionID -  String as the target collectionID in the database
   */
  public GetFavoritesHandler(FirebaseService db, String collectionID) {
    this.db = db;
    this.collectionID = collectionID;
  }

  /**
   * This is the override handle method. Takes in a request containing the username,
   * and produces a response with the user's favorites list.
   *
   * @param request  HTTP request
   * @param response HTTP response
   * @return json format with favorites as the response
   * @throws JSONException caused by instantiating JSONObject from request
   */
  @Override
  public Object handle(Request request, Response response) throws JSONException {
    // get the information needed
    String user = request.queryParams("username");

    // get the favorite list using FirebaseService
    Map<String,Object> fields = this.db.getDocument(this.collectionID, user);
    // check if the fields list exists
    if (fields == null || !fields.containsKey("favorites")) {
      fields = new HashMap<>();
      List<String> favorites = new ArrayList<>();
      fields.put("favorites",favorites);
      this.db.setDocument(this.collectionID,user,fields);
    }

    // convert the fields to json
    Map<String,Object> res = ImmutableMap.of("favorites", fields.get("favorites"));
    Gson gson = new Gson();
    return gson.toJson(res);
  }
}
