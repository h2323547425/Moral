package edu.brown.cs.student.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * The FirebaseSetup class initialize a firebase connection based on an indicated
 * firebase with secret key so that the database can be later on used.
 *
 * @author Hongyi Li
 */
public class FirebaseSetup {
  private final String keyPath;

  /**
   * Constructor of the FirebaseSetup, take in a firebase url and make connection
   * to the indicated database using the setup() method.
   *
   * @param path String representing the path to firebase account key
   */
  public FirebaseSetup(String path) {
    this.keyPath = path;
  }

  /**
   * Make connection to the database by getting the secret serviceAccountKey, initialize
   * FirebaseOptions bt setting up credentials, and initialize the FirebaseAPP.
   *
   * @return FirebaseApp with the connected database
   * @throws IOException caused by FileInputStream and GoogleCredentials
   */
  public FirebaseApp setup() throws IOException {
    FileInputStream serviceAccount =
      new FileInputStream(this.keyPath);

    FirebaseOptions options = new FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build();

    return FirebaseApp.initializeApp(options);
  }
}
