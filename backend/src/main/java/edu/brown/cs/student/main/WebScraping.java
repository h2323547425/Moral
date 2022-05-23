package edu.brown.cs.student.main;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.swing.text.Element;
import javax.xml.xpath.XPath;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class WebScraping {

  private Connection conn;

  /**
   * Class that takes scare of web scraping Yahoo finance pages for relevant
   * financial information
   */

  // The list obtained from the original db may have some inconsistencies with yahoo finance
  // so create a new one here.
  private final Map<String, GenericStock> cleanedList;

  public WebScraping() throws SQLException {
    this.cleanedList = new HashMap<>();
    String filePath = "data/esgdatabase.db";
    // Connect to database that is specified via the filepath in the constructor
    String urlToDB = "jdbc:sqlite:" + filePath;
    conn = DriverManager.getConnection(urlToDB);

    Statement foreignKeys = conn.createStatement();
    foreignKeys.executeUpdate("PRAGMA foreign_keys=ON");
    foreignKeys.close();
  }

  /**
   * Goes through yahoo finance pages for every stock in our database and scrapes relevant information
   * to calculate an growth rate. In order to calculate growth rates we draw on analyst 1-year predictions for stocks.
   * If these are not present (indicated by a value of N/A on the web-page) we calculate a stocks fair growth rate
   * using the stock's beta (which can also be found on yahoo finance). Beta is defined as a stocks return relative to the
   * market.
   */
  public Map<String, Double> scrapeAnalystPrediction(Map<String, GenericStock> stockMap)
      throws SQLException {

    Map<String, Double> tickerToRate = new HashMap<>();
    List<String> tickers = new ArrayList<>();
    for (String ticker : stockMap.keySet()) {
      WebDriverManager.chromedriver().setup();

      System.out.println("\n" + "\n" + ticker + "\n" + "\n");

      ChromeOptions options = new ChromeOptions();
      options.addArguments("--headless");
      ChromeDriver driver = new ChromeDriver(options);

      driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);


      String url = "https://finance.yahoo.com/quote/" + ticker + "/";
      driver.get(url);
      String className = "//td[@class='Ta(end) Fw(600) Lh(14px)']";
      List<WebElement> finance_info = driver.findElements(By.xpath(className));
      if (!finance_info.isEmpty()) {
        double prevClose = Double.parseDouble(finance_info.get(0).getText().replace(",", ""));
        double growthRate;
        // Check if stock has analyst target values
        if (finance_info.get(finance_info.size() - 1).getText().equals("N/A")) {
          if (finance_info.get(9).getText().equals("N/A")) {
            growthRate = 0.10;
          } else {
            // If no value is found calculate growth rate using Beta value (most if not all stocks will have this)
            double beta = Double.parseDouble(finance_info.get(9).getText());
            growthRate = ((beta - 1.0) * 1.10) - 1.00;
          }

        } else {
          // If value is found for analyst prediction, calculate growth rate using that.
          String parsedString = finance_info.get(finance_info.size() - 1).getText();
          parsedString = parsedString.replace(",", "");
          double analystPrediction =
              Double.parseDouble(parsedString);
          growthRate = (analystPrediction - prevClose) / prevClose;
        }

        System.out.println("\n" + "\n" + ticker + " " + growthRate + "\n" + "\n");

        tickerToRate.put(ticker, growthRate);
      } else {
        tickers.add(ticker);
      }

      driver.quit();

    }
    tickerToRate = this.normalizeGrowthRates(tickerToRate);
    this.fillTable(tickerToRate);

    return tickerToRate;
  }

  /**
   * Creates a table to store stock growth rates.
   *
   * @throws SQLException when error occurs with sql commands
   */
  public void createSqlTable() throws SQLException {
    PreparedStatement createTable =
        conn.prepareStatement("CREATE TABLE IF NOT EXISTS growthRates(" +
            "ticker TEXT," + "growth_rate DOUBLE);");
    createTable.executeUpdate();
  }

  /**
   * Insert the calculated growth rates into the growth rates table in the db.
   *
   * @param growthMap Map of a stock ticker to given growth rate.
   * @throws SQLException when error occurs with sql commands.
   */
  public void fillTable(Map<String, Double> growthMap) throws SQLException {
    for (String ticker : growthMap.keySet()) {
      Double value = growthMap.get(ticker);
      PreparedStatement insert = conn.prepareStatement("INSERT INTO growthRates VALUES(?, ?);");
      insert.setString(1, ticker);
      insert.setDouble(2, value);
      insert.executeUpdate();
    }
    PreparedStatement sort = conn.prepareStatement(
        "CREATE TABLE sorted_growth AS SELECT * FROM growthRates ORDER BY growth_rate DESC");
    sort.execute();
    PreparedStatement drop = conn.prepareStatement("DROP TABLE growthRates");
    drop.execute();
  }

  /**
   * Find the z score of each value to see how their growth rates differ from the means.
   * This function makes use of z-score values because some of the growth rates were outliers
   * and using z-scores handles outliers better than min-max normalization.
   *
   * @param inputMap a map of tickers to their growth rates.
   * @return a map of tickers to their normalized growth rates using z-scores.
   */
  public Map<String, Double> normalizeGrowthRates(Map<String, Double> inputMap) {
    Map<String, Double> outputMap = new HashMap<>();
    Collection<Double> originalRate = inputMap.values();
    List<Double> originalRates = new ArrayList<Double>(originalRate);
    double sum = 0;
    for (Double growthRate : originalRates) {
      sum += growthRate;
    }
    // Find average of all growth rates.
    double mean = sum / (double) originalRates.size();

    double squaredDifference = 0;
    for (Double growthRate : originalRates) {
      squaredDifference += (growthRate - mean) * (growthRate - mean);
    }
    // calculate variance of all growth rates.
    double variance = squaredDifference / (double) originalRates.size();

    // Iterate through each stock and calculate the z-score (distance from the mean).
    for (String ticker : inputMap.keySet()) {
      double currGrowthRate = inputMap.get(ticker);
      double zScore = (currGrowthRate - mean) / variance;
      outputMap.put(ticker, zScore);
    }
    return outputMap;
  }


  /**
   * Scrape the database to return the most recently calculated growth rates.
   * @param stockMap ticker to genericStock.
   * @return map of a stock ticker to information about the stock. e.g.: {{TSLA, {name: Tesla, dividend yield: %4.00}}
   * @throws SQLException for errors relating to the db.
   */
  public Map<String, Map<String, String>> getGrowthRates(Map<String, GenericStock> stockMap)
      throws SQLException {
    Map<String, Map<String, String>> tickerInfoMap = new HashMap<>();
    Set<String> tickerList = stockMap.keySet();
    for (String ticker : tickerList) {
      PreparedStatement selectRates = conn.prepareStatement(
          "Select * FROM sorted_growth WHERE ticker = " + "\"" + ticker + "\"" + ";");
      selectRates.execute();
      ResultSet tableInfo = selectRates.getResultSet();
      if (!tableInfo.isClosed()) {
        Map<String, String> tickerInfo = new HashMap<>();

        GenericStock currStock = stockMap.get(ticker);
        tickerInfo.put("name", currStock.getStockName());
        tickerInfo.put("dividend yield", currStock.getDivYield());
        tickerInfo.put("Score", tableInfo.getString(2));
        this.cleanedList.put(ticker, currStock);
        currStock.setFinanceScore(Double.parseDouble(tableInfo.getString(2)));
        tickerInfo.put("Price", currStock.getStockPrice());
        tickerInfo.put("Market_Cap", currStock.getMarketCap());
        tickerInfo.put("PE_Ratio", currStock.getPeRatio());
        tickerInfo.put("Fields_Does_Well", currStock.getFieldsDoesWellIn());

        tickerInfoMap.put(ticker, tickerInfo);
        tableInfo.close();
      }
    }


    return tickerInfoMap;

  }


  /**
   * Return most up-to-date version of stock list.
   *
   * @return ticker symbol to stock object.
   */
  public Map<String, GenericStock> getCleanedList() {
    return this.cleanedList;
  }
}
