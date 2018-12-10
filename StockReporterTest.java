package stockticker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class StockReporterTest {
  private StockReporter stockReporter;
  private StockPriceService tickerStock;

  @BeforeEach
  void init() {
    tickerStock = Mockito.mock(StockPriceService.class);
    stockReporter = new StockReporter();
    stockReporter.setStockPriceService(tickerStock);
  }
  
  @Test
  void canary(){
    assertTrue(true);
  }
  
  @Test
  void getHighestPriceOfEmptyList() {
    assertEquals(Map.of(), stockReporter.getHighestPrices(Map.of()));
  }
  
  @Test
  void getHighestPriceWithOneHighestPrice(){
    Map<String, Integer> highestPrices = 
      stockReporter.getHighestPrices(Map.of(
        "GOOG", 1000, "MSFT", 2000, "ORCL", 3000,
        "AMZN",4000, "INTC", 5000));

    assertEquals(Map.of("INTC", 5000),
      stockReporter.getHighestPrices(highestPrices));
  }
  
  @Test
  void getHighestPriceWithTwoHighestPrices(){
    Map<String, Integer> highestPrices =
      stockReporter.getHighestPrices(Map.of(
        "GOOG", 1000, "MSFT", 2000, "ORCL", 3000, "AMZN",5000, "INTC", 5000));

    assertEquals(Map.of("AMZN", 5000, "INTC", 5000),
      stockReporter.getHighestPrices(highestPrices));
  }
  
  @Test
  void getLowestPriceOfEmptyList() {
    assertEquals(Map.of(), stockReporter.getLowestPrices(Map.of()));
  }
  
  @Test
  void getLowestPriceWithOneLowestPrice(){
    Map<String, Integer> lowestPrices = stockReporter.getLowestPrices(
      Map.of("GOOG", 1000, "MSFT", 2000, "ORCL", 3000, "AMZN",4000, 
        "INTC", 5000));

    assertEquals(Map.of("GOOG", 1000), lowestPrices);
  }
  
  @Test
  void getLowestPriceWithTwoLowestPrices(){
    Map<String, Integer> lowestPrices = stockReporter.getLowestPrices(
      Map.of("GOOG", 1000, "MSFT", 1000, "ORCL", 3000, "AMZN",4000,
        "INTC", 5000));
        
    assertEquals(Map.of("GOOG", 1000, "MSFT", 1000), lowestPrices);
  }
  
  @Test
  void sortByTickersEmptyList(){
    Map<String, Integer> sortedPrices = stockReporter.sortByTickers(Map.of());

    assert(sortedPrices.isEmpty());
  }
  
  @Test
  void sortByTickersSomeData(){
    Map<String, Integer> sortedTickers = stockReporter.sortByTickers(
      Map.of("MSFT", 10, "GOOG", 20, "AMZN", 5, "INTC", 100));
  
    assertEquals(List.of("AMZN", "GOOG", "INTC", "MSFT"), new ArrayList<>(sortedTickers.keySet()));
  }
  
  @Test
  void returnPricesForEmptyTickers(){
    Result result = stockReporter.fetchPrices(List.of());
    assertEquals(Map.of(), result.prices);
    assertEquals(Map.of(), result.errors);
  }
  
  @Test
  void returnPriceForOneTicker(){
    when(tickerStock.getPrice("GOOG")).thenReturn(100);
    Result result = stockReporter.fetchPrices(List.of("GOOG"));

    assertEquals(Map.of("GOOG", 100), result.prices);
    assertEquals(Map.of(), result.errors);
  }
  
  @Test
  void returnPriceForTwoTickers(){
    when(tickerStock.getPrice("GOOG")).thenReturn(100);
    when(tickerStock.getPrice("AMZN")).thenReturn(200);
    
    stockReporter.setStockPriceService(tickerStock);
    Result result = stockReporter.fetchPrices(List.of("GOOG", "AMZN"));

    assertEquals(
      Map.of("GOOG", 100,"AMZN",200), result.prices);
    assertEquals(Map.of(), result.errors);
  }
  
  @Test
  void returnPriceForOneError(){
    when(tickerStock.getPrice("GOOG"))
      .thenThrow(new RuntimeException("Invalid Ticker"));
    
    stockReporter.setStockPriceService(tickerStock);
    Result result = stockReporter.fetchPrices(List.of("GOOG"));

    assertEquals(Map.of("GOOG", "Invalid Ticker"), result.errors);
    assertEquals(Map.of(), result.prices);
  }
  
  @Test
  void returnPriceForTwoErrorOneSuccess(){

    when(tickerStock.getPrice("AMZN")).thenReturn(100);
    when(tickerStock.getPrice("GOOG")).thenThrow(new RuntimeException("Invalid Ticker"));
    when(tickerStock.getPrice("MSFT")).thenThrow(new RuntimeException("Network Error"));
    
    stockReporter.setStockPriceService(tickerStock);
    Result result = stockReporter.fetchPrices(List.of("GOOG", "AMZN", "MSFT"));
    assertEquals(Map.of("AMZN", 100), result.prices);
    assertEquals(Map.of("GOOG", "Invalid Ticker", "MSFT", "Network Error"), result.errors);
  }
}
