package edu.brown.cs.student.userapi;

import edu.brown.cs.student.firebase.FirebaseService;
import spark.Spark;

import java.io.IOException;

/**
 * The UserApiSetup class initialize 3 endpoints which access, add to, and delete from
 * a firestore API connected using FirebaseService.
 *
 * @author Hongyi li
 */
public class UserApiSetup {
  private FirebaseService fbs;

  /**
   * The constructor of the UserApiSetup class initialize 3 endpoints which access, add to,
   * and delete from a firestore API connected using FirebaseService.
   *
   * @param path String representing the path to firebase account key
   * @param collectionID -  String as the target collectionID in the database
   * @throws IOException caused by initializing firebase connection
   */
  public UserApiSetup(String path, String collectionID) throws IOException {
    this.fbs = new FirebaseService(path);
    Spark.get("/favorites", new GetFavoritesHandler(fbs,collectionID));
    Spark.post("/favorites/add", new AddFavoritesHandler(fbs,collectionID));
    Spark.post("/favorites/delete", new DeleteFavoritesHandler(fbs,collectionID));
  }

  /**
   * Close the FirebaseApp after the User Api is no longer in use. Mainly
   * used for testing purposes.
   */
  public void close() {
    this.fbs.close();
  }

}
