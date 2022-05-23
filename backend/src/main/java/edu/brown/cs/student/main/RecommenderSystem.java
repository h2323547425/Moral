package edu.brown.cs.student.main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class RecommenderSystem {

  private Map<String, GenericStock> tickerToStock;

  public RecommenderSystem() {
  }

  /**
   * @return a map of stock ticker to the item that contains the stock.
   * @throws SQLException if sql throws an error.
   * @throws IOException  If an invalid ticker is entered.
   */
  public Map<String, GenericStock> symbolToInfo() throws SQLException, IOException {
    StockDatabase db = new StockDatabase("data/esgdatabase.db");
    db.getStockNames();
    Map<String, GenericStock> nameToInfo = new TreeMap<>();

    nameToInfo = db.initializeStocks();
    this.tickerToStock = nameToInfo;
    return nameToInfo;
  }

  /**
   * Prints out stock info given a ticker. Used to sanity check first week components
   *
   * @param ticker stock ticker.
   */
  public void getStockInfo(String ticker) {

    if (this.tickerToStock.containsKey(ticker)) {
      System.out.println(this.tickerToStock.get(ticker).getStockInfo());
    } else {
      System.err.println("ERROR: Ticker doesn't exist or hasn't been loaded into system");
    }
  }

  /**
   * Generate the score. By aggregating the different components.
   * Filters correspond to the ones on the frontend.
   *
   * @param stockMap map of tickers to stocks.
   * @param filter1  how much we should prioritize each component.
   * @param filter2  how much we should prioritize each component.
   * @param filter3  how much we should prioritize each component
   * @param filter4  how much we should prioritize each component
   * @param filter5  how much we should prioritize each component
   * @return
   */
  public Map<String, Double> generateScore(Map<String, GenericStock> stockMap, Double filter1,
                                           Double filter2, Double filter3, Double filter4,
                                           Double filter5) {
    Set<String> stockSet = stockMap.keySet();
    Map<String, Double> genderScoreMap = new HashMap<>();
    Map<String, Double> deforestationScoreMap = new HashMap<>();
    Map<String, Double> weaponsScoreMap = new HashMap<>();
    Map<String, Double> fossilFuelMap = new HashMap<>();
    Map<String, Double> prisonMap = new HashMap<>();
    Map<String, Double> financeScoreMap = new HashMap<>();

    // Create a map with scores for each component
    for (String ticker : stockSet) {
      genderScoreMap.put(ticker, (double) stockMap.get(ticker).getGenderEqualityScore());
      deforestationScoreMap.put(ticker, (double) stockMap.get(ticker).getDeforestationScore());
      weaponsScoreMap.put(ticker, (double) stockMap.get(ticker).getWeaponsScore());
      fossilFuelMap.put(ticker, (double) stockMap.get(ticker).getFossilFuelScore());
      prisonMap.put(ticker, (double) stockMap.get(ticker).getPrisonScore());
      financeScoreMap.put(ticker, stockMap.get(ticker).getFinanceScore());
    }

    // Normalize the values in each map
    genderScoreMap = this.normalizeValues(genderScoreMap);
    deforestationScoreMap = this.normalizeValues(deforestationScoreMap);
    weaponsScoreMap = this.normalizeValues(weaponsScoreMap);
    fossilFuelMap = this.normalizeValues(fossilFuelMap);
    prisonMap = this.normalizeValues(prisonMap);
    financeScoreMap = this.normalizeValues(financeScoreMap);

    // Calculate output scores by using the filter values to prioritize different components.
    Map<String, Double> outputMap = new LinkedHashMap<>();
    for (String ticker : stockSet) {
      Double aggregateScore =
          financeScoreMap.get(ticker) * 0.20 + deforestationScoreMap.get(ticker) * filter1 +
              genderScoreMap.get(ticker) * filter2 + weaponsScoreMap.get(ticker) * filter3 +
              prisonMap.get(ticker) * filter4 + fossilFuelMap.get(ticker) * filter5;
      stockMap.get(ticker).setFinanceScore(financeScoreMap.get(ticker));
      outputMap.put(ticker, aggregateScore);
    }

    // Normalize the final values and sort the map using streams and a linkedhashmap to preserve order.
    outputMap = this.normalizeValues(outputMap);
    Map<String, Double> sortedMap = new LinkedHashMap<>();
    outputMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

    return sortedMap;

  }

  /**
   * Normalizes all the values in the map using minmax normalization.
   *
   * @param scoreMap
   * @return
   */
  public Map<String, Double> normalizeValues(Map<String, Double> scoreMap) {

    Collection<Double> scoreValues = scoreMap.values();
    Double max = Collections.max(scoreValues);
    Double min = Collections.min(scoreValues);
    Map<String, Double> normalizedMap = new LinkedHashMap<>();
    for (String ticker : scoreMap.keySet()) {
      Double originalValue = scoreMap.get(ticker);
      double normalizedValue = ((originalValue - min) / (max - min)) * 100.0;

      // Truncate final value to two decimal points
      normalizedValue = normalizedValue * Math.pow(10, 2);
      normalizedValue = Math.floor(normalizedValue);
      normalizedValue = normalizedValue / Math.pow(10, 2);

      normalizedMap.put(ticker, normalizedValue);
    }

    return normalizedMap;
  }


  /**
   * Getter method used for testing.
   *
   * @return tickerToStock HashMap
   */
  public Map<String, GenericStock> getTickerToStock() {
    return tickerToStock;
  }
}
