package edu.brown.cs.student.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * Class that represents a generic stock and keeps track of both financial characteristics and
 */

public class GenericStock {

  private static final Logger log = LoggerFactory.getLogger(Stock.class);
  private Stock stock;

  private int fossilFuelScore = 5;
  private int deforestationScore = 9;
  private int genderEqualityScore = 0;
  private int prisonScore = 3;
  private int weaponsScore = 2;
  private double financeScore = 0.0;
  private StringBuilder fieldsDoesWellIn = new StringBuilder();

  public GenericStock(String name) throws IOException {
    try {
      this.stock = YahooFinance.get(name);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Returns up-to-date bid/ask price and other statistics for stocks
   *
   * @return stats for stocks.
   */
  public String getStockInfo() {
    StringBuilder output = new StringBuilder();
    // output.append(this.stock.getSymbol()).append("\n");
    output.append("--------------------------------" + "\n");

    output.append("name: ").append(this.stock.getName()).append("\n");
    output.append("currency: ").append(this.stock.getCurrency()).append("\n");
    output.append("Stock Exchange: ").append(this.stock.getStockExchange()).append("\n");
    output.append("quote: ").append(this.stock.getQuote()).append("\n");

    output.append("stats: ").append(this.stock.getStats()).append("\n");
    output.append("dividend: ").append(this.stock.getDividend()).append("\n");

    output.append("--------------------------------").append("\n");


    output.append("ESG Information").append("\n");
    output.append("--------------------------------").append("\n");
    for (Field f : this.getClass().getDeclaredFields()) {
      try {
        output.append(f.getName()).append(": ").append(f.get(this)).append("\n");
      } catch (IllegalArgumentException | IllegalAccessException ex) {
        log.error(null, ex);
      }
    }
    output.append("--------------------------------").append("\n");

    return output.toString();
  }

  public void decrementFossilFuel() {
    this.fossilFuelScore -= 1;
  }

  /**
   * Decrements deforestation score.
   */
  public void decrementDeforestationScore() {
    this.deforestationScore -= 1;
  }

  /**
   * Increments Gender Score.
   */
  public void incrementGenderScore() {
    this.genderEqualityScore += 1;
  }

  /**
   * Decrements Weapons Score.
   */
  public void decrementWeaponsScore() {
    this.weaponsScore -= 1;
  }

  /**
   * Decrements Prison Score.
   */
  public void decrementPrisonScore() {
    this.prisonScore -= 1;
  }

  public void setFinanceScore(Double score) {
    this.financeScore = score;
  }

  public double getFinanceScore() {
    return this.financeScore;
  }

  /**
   * Returns fossil fuel score.
   *
   * @return fossil fuel score.
   */
  public int getFossilFuelScore() {
    return this.fossilFuelScore;
  }

  public int getDeforestationScore() {
    return deforestationScore;
  }

  public int getGenderEqualityScore() {
    return genderEqualityScore;
  }

  public int getPrisonScore() {
    return prisonScore;
  }

  public int getWeaponsScore() {
    return weaponsScore;
  }

  /**
   * Returns dividend yield of a stock.
   *
   * @return div. yield of a stock.
   */

  public String getDivYield() {
    return "" + this.stock.getDividend().getAnnualYield() + "";
  }

  /**
   * Returns stock name. ex. tsla -> Tesla.
   *
   * @return stock name.
   */
  public String getStockName() {
    return this.stock.getName();
  }

  public void setFieldDoesWellIn(String field) {
    this.fieldsDoesWellIn.append(field).append(" ");
  }

  public String getFieldsDoesWellIn() {
    return this.fieldsDoesWellIn.toString();
  }


  /**
   * Returns stock price.
   *
   * @return current stock ask price.
   */
  public String getStockPrice() {
    return "" + this.stock.getQuote().getAsk() + "";
  }

  /**
   * Returns stock market cap in billions cast to an int.
   *
   * @return
   */
  public String getMarketCap() {
    BigDecimal mp = BigDecimal.valueOf(0);
    if (this.stock.getStats().getMarketCap() != null) {
      mp = this.stock.getStats().getMarketCap();
      mp = mp.divide(BigDecimal.valueOf(1000000000), BigDecimal.ROUND_CEILING);
    }
    return "" + mp + "B";
  }

  public String getPeRatio() {
    Integer pe = 0;
    if (this.stock.getStats().getPe() != null) {
      pe = this.stock.getStats().getPe().intValue();
    }
    return "" + pe + "";
  }


}

