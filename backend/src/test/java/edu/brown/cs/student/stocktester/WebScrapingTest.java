package edu.brown.cs.student.stocktester;

import edu.brown.cs.student.main.GenericStock;
import edu.brown.cs.student.main.RecommenderSystem;
import edu.brown.cs.student.main.WebScraping;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class WebScrapingTest {

  @Test
  public void getStockInfoTest() throws SQLException, IOException {
    RecommenderSystem recommenderSystem = new RecommenderSystem();
    Map<String, GenericStock> stockMap = recommenderSystem.symbolToInfo();
    WebScraping scraper = new WebScraping();
    Map<String, Map<String, String>> allStockInfo = scraper.getGrowthRates(stockMap);
  }
}
