package edu.brown.cs.student.firebase;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * The FirebaseService class provides essential functionalities such as accessing
 * or mutating the firebase database. It also utilizes FirebaseSetup to initialize
 * the connection to a firebase database.
 *
 * @author Hongyi Li
 */
public class FirebaseService {
  private final FirebaseApp app;

  /**
   * Constructor of the FirebaseService, take in a path to firebase account key and
   * make connection to the indicated database using the FirebaseSetup class.
   *
   * @param path String representing the path to firebase account key
   * @throws IOException caused by FileInputStream and GoogleCredentials
   */
  public FirebaseService(String path) throws IOException {
    FirebaseSetup fbSetup = new FirebaseSetup(path);
    this.app = fbSetup.setup();
  }

  /**
   * Access a specific document in the connected firebase by finding the collection
   * with input collectionID, then the document with documentID. The document is
   * finally returned as a DocumentSnapshot object.
   *
   * @param collectionID String representing the id of a collection in the firebase
   * @param documentID String representing the id of a document in the firebase
   * @return Map representing the desired document fields in firebase
   */
  public Map<String,Object> getDocument(String collectionID, String documentID) {
    try {
      Firestore db = FirestoreClient.getFirestore(this.app);
      DocumentReference dr = db.collection(collectionID).document(documentID);
      DocumentSnapshot ds = dr.get().get();
      return ds.getData();
    } catch (ExecutionException | InterruptedException e) {
      return null;
    }
  }

  /**
   * Modify a specific document in the connected firebase by finding the collection
   * with input collectionID, then the document with documentID. The fields of the
   * document is set to the input map.
   *
   * @param collectionID String representing the id of a collection in the firebase
   * @param documentID   String representing the id of a document in the firebase
   * @param fields       Map representing the key-values pairs of the new fields
   * @return boolean indicating whether the document was updated successfully
   */
  public boolean setDocument(String collectionID, String documentID,
                          Map<String, Object> fields) {
    try {
      Firestore db = FirestoreClient.getFirestore(this.app);
      ApiFuture<WriteResult> future =
          db.collection(collectionID).document(documentID).set(fields);
      System.out.println("Successfully updated at: " + future.get().getUpdateTime());
    } catch (ExecutionException | InterruptedException e) {
      System.out.println("Update failed on document " + documentID);
      return false;
    }
    return true;
  }

  /**
   * Close the FirebaseApp after the firebase connection is no longer in use. Mainly
   * used for testing purposes.
   */
  public void close() {
    this.app.delete();
  }

}
