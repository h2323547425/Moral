package edu.brown.cs.student.firebase;
import com.google.firebase.FirebaseApp;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This class tests the functionality of FirebaseSetup and FirebaseService.
 */
public class FirebaseServiceTest {

  /**
   * test if a firestore database could be connected correctly.
   */
  @Test
  public void testFirebaseSetup() {
    String path = "../config/secret/serviceAccountKey.json";
    FirebaseSetup fbSetup = new FirebaseSetup(path);
    try {
      // fail if setup() return null
      FirebaseApp app = fbSetup.setup();
      assertNotNull(app);
      app.delete();
    } catch (IOException e) {
      // fail if cause error
      fail();
    }
  }

  /**
   * test if a firestore database could be accessed correctly.
   */
  @Test
  public void testFirebaseAccess() throws IOException {
    String path = "../config/secret/serviceAccountKey.json";
    FirebaseService db = new FirebaseService(path);
    // check if non-existing collection return null
    assertNull(db.getDocument("unit_test","NA"));

    // check if existing collection return correct value
    Map<String, Object> fields = db.getDocument("unit_test","test1");
    assertEquals(2, fields.size());
    assertEquals("a", fields.get("field1"));
    assertEquals("b", fields.get("field2"));

    db.close();
  }

  /**
   * test if a firestore database could modify fields correctly.
   */
  @Test
  public void testFirebaseModify() throws IOException {
    String path = "../config/secret/serviceAccountKey.json";
    FirebaseService db = new FirebaseService(path);
    // make the new fields
    Map<String, Object> newFields = new HashMap<>();
    newFields.put("field1", "a");
    newFields.put("field2", "b");
    newFields.put("field3", "c");
    assertTrue(db.setDocument("unit_test","test1", newFields));

    // check if the fields are correctly added to the collection
    Map<String, Object> fields = db.getDocument("unit_test","test1");
    assertEquals(3, fields.size());
    assertEquals("a", fields.get("field1"));
    assertEquals("b", fields.get("field2"));
    assertEquals("c", fields.get("field3"));

    // change back to original fields
    newFields.remove("field3");
    assertTrue(db.setDocument("unit_test","test1", newFields));

    // check if the fields are correctly reset
    fields = db.getDocument("unit_test","test1");
    assertEquals(2, fields.size());
    assertEquals("a", fields.get("field1"));
    assertEquals("b", fields.get("field2"));

    db.close();
  }
}
