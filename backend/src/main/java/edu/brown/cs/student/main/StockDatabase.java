package edu.brown.cs.student.main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class that interacts and calls sql commands on our esg database.
 * Used to initialize list of stocks.
 */
public class StockDatabase {

  private Connection conn;
  private List<String> stockNames;

  /**
   * Initialize connection to database.
   *
   * @param filePath path to sql database
   * @throws SQLException if sql command throws an error.
   */
  public StockDatabase(String filePath) throws SQLException {

    this.stockNames = new ArrayList<>();
    // Connect to database that is specified via the filepath in the constructor
    String urlToDB = "jdbc:sqlite:" + filePath;
    conn = DriverManager.getConnection(urlToDB);

    Statement foreignKeys = conn.createStatement();
    foreignKeys.executeUpdate("PRAGMA foreign_keys=ON");
  }

  /**
   * Gets list of stock names from database.
   *
   * @throws SQLException if sql command throws an error.
   */
  public void getStockNames() throws SQLException {
    try {
      PreparedStatement tables = conn.prepareStatement("Select Ticker FROM joined_table;");
      ResultSet tickerNames = tables.executeQuery();
      while (tickerNames.next()) {
        this.stockNames.add(tickerNames.getString(1));

      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

  }

  /**
   * Initialize hashmap of stock ticker to stock objects. Use ESG information from database to
   * initialize stocks.
   *
   * @throws SQLException if sql command throws an error.
   */
  public Map<String, GenericStock> initializeStocks() throws SQLException, IOException {
    Map<String, GenericStock> nameToInfo = new TreeMap<>();
    for (String ticker : this.stockNames) {
      // Instantiate a new stock object with the given ticker and put into hashmap.
      GenericStock currStock = new GenericStock(ticker);
      nameToInfo.put(ticker, currStock);

      PreparedStatement info =
          conn.prepareStatement(
              "Select * FROM joined_table WHERE Ticker = " + "\"" + ticker + "\"" + ";");
      info.execute();

      ResultSet rowSet = info.getResultSet();
      ResultSetMetaData rowMetaData = rowSet.getMetaData();

      // Check each environmental variable to see if a stock meets it.
      // If so update the respective instance variable in Generic_Stock class.
      if (rowSet.getString(3) != null) {
        currStock.decrementFossilFuel();
      }
      if (rowSet.getString(4) != null) {
        currStock.decrementFossilFuel();
      }

      if (rowSet.getString(5) != null) {
        currStock.decrementFossilFuel();

      }
      if (rowSet.getString(6) != null) {
        currStock.decrementFossilFuel();
      }
      if (rowSet.getString(7) != null) {
        currStock.decrementFossilFuel();

      }
      if (rowSet.getString(8) != null) {
        currStock.decrementDeforestationScore();
      }
      if (rowSet.getString(9) != null) {
        currStock.decrementDeforestationScore();
      }
      if (rowSet.getString(10) != null) {
        currStock.decrementDeforestationScore();
      }
      if (rowSet.getString(11) != null) {
        currStock.decrementDeforestationScore();
      }
      if (rowSet.getString(12) != null) {
        currStock.decrementDeforestationScore();
      }
      if (rowSet.getString(13) != null) {
        currStock.decrementDeforestationScore();
      }
      if (rowSet.getString(14) != null) {
        currStock.decrementDeforestationScore();
      }
      if (rowSet.getString(15) != null) {
        currStock.decrementDeforestationScore();
      }
      if (rowSet.getString(16) != null) {
        currStock.decrementDeforestationScore();
      }
      if (rowSet.getString(17) != null) {
        currStock.decrementDeforestationScore();
      }
      if (rowSet.getString(18) != null) {
        currStock.decrementDeforestationScore();
      }
      if (rowSet.getString(19) != null) {
        currStock.decrementDeforestationScore();
      }
      if (rowSet.getString(20) != null) {
        currStock.decrementDeforestationScore();
      }
      if (rowSet.getString(21) != null) {
        currStock.decrementDeforestationScore();
      }
      if (rowSet.getString(22) != null) {
        currStock.decrementDeforestationScore();
      }
      if (rowSet.getString(23) != null) {
        currStock.incrementGenderScore();
      }
      if (rowSet.getString(24) != null) {
        currStock.decrementWeaponsScore();
      }
      if (rowSet.getString(25) != null) {
        currStock.decrementWeaponsScore();
      }
      if (rowSet.getString(26) != null) {
        currStock.decrementPrisonScore();
      }
      if (rowSet.getString(27) != null) {
        currStock.decrementPrisonScore();
      }
      if (rowSet.getString(28) != null) {
        currStock.decrementPrisonScore();
      }
      if (rowSet.getString(29) != null) {
        currStock.decrementPrisonScore();
      }
      if (rowSet.getString(30) != null) {
        currStock.decrementWeaponsScore();
      }
      if (rowSet.getString(31) != null) {
        currStock.decrementWeaponsScore();
      }
      if (rowSet.getString(32) != null) {
        currStock.decrementWeaponsScore();
      }
      if (currStock.getDeforestationScore() == 9) {
        currStock.setFieldDoesWellIn("Deforestation,");
      }
      if (currStock.getFossilFuelScore() == 5) {
        currStock.setFieldDoesWellIn("Fossil Fuel Use,");
      }
      if (currStock.getGenderEqualityScore() == 1) {
        currStock.setFieldDoesWellIn("Gender Equality,");
      }
      if (currStock.getPrisonScore() == 3) {
        currStock.setFieldDoesWellIn("Prisons,");
      }
      if (currStock.getWeaponsScore() == 2) {
        currStock.setFieldDoesWellIn("Weapons,");
      }

      nameToInfo.put(ticker, currStock);

    }
    conn.close();
    return nameToInfo;
  }
}
