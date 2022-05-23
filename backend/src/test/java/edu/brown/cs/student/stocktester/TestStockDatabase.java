package edu.brown.cs.student.stocktester;

import edu.brown.cs.student.main.GenericStock;
import edu.brown.cs.student.main.StockDatabase;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestStockDatabase {

  @Test
  public void testScrapeDatabase() throws SQLException, IOException {
    StockDatabase db = new StockDatabase("data/esgdatabase.db");
    db.getStockNames();
    Map<String, GenericStock> stockMap = db.initializeStocks();
    assertEquals(stockMap.size(), 451);
    assertEquals(stockMap.get("AMZN").getStockName(), "Amazon.com, Inc.");
  }

}
