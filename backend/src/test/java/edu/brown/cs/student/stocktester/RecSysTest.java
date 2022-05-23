package edu.brown.cs.student.stocktester;

import edu.brown.cs.student.main.GenericStock;
import edu.brown.cs.student.main.RecommenderSystem;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RecSysTest {

  @Test
  public void testSymbolToInfo() throws SQLException, IOException {
    RecommenderSystem recsys = new RecommenderSystem();
    Map<String, GenericStock> stockMap = recsys.symbolToInfo();
    assertEquals(stockMap.size(), 451);
  }

  @Test
  public void testNormalizeValues() {
    RecommenderSystem recSys = new RecommenderSystem();
    Map<String, Double> testMap = new HashMap<>();
    testMap.put("one", 1.0);
    testMap.put("two", 2.0);
    testMap.put("three", 3.0);
    testMap.put("four", 4.0);
    testMap.put("five", 5.0);
    Map<String, Double> normalizedMap = recSys.normalizeValues(testMap);
    assertEquals(normalizedMap.get("one"), 0.0, 0);
    assertEquals(normalizedMap.get("five"), 100.0, 0);
  }

}
