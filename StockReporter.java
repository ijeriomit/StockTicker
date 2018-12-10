package stockticker;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

class StockReporter {
  private StockPriceService stockPriceService;
  
  Map<String, Integer> getHighestPrices(
          Map<String, Integer> stockPrices) {

    int highestPrice = stockPrices.isEmpty() ? 0 :
      Collections.max(stockPrices.values());
    
    return stockPrices.keySet()
      .stream()
      .filter(key -> stockPrices.get(key) == highestPrice)
      .collect(toMap(Function.identity(), stockPrices::get));
  }
  
  Map<String, Integer> getLowestPrices(
          Map<String, Integer> stockPrices) {

    int lowestPrice = stockPrices.isEmpty() ? 0 :
      Collections.min(stockPrices.values());
    
    return stockPrices.keySet()
      .stream()
      .filter(key -> stockPrices.get(key) == lowestPrice)
      .collect(toMap(Function.identity(), stockPrices::get));
  }
  
  Map<String, Integer> sortByTickers(
          Map<String, Integer> stockTickers){

    return new TreeMap<>(stockTickers);
  }
  
  Result fetchPrices(List<String> inputList){

    Map<String, Integer> prices = new HashMap<>();
    Map<String, String> errors = new HashMap<>();

    for (String anInputList : inputList) {
      try {
        prices.put(anInputList, stockPriceService.getPrice(anInputList));
      } catch (RuntimeException ex) {
        errors.put(anInputList, ex.getMessage());
      }
    }
    return new Result(prices, errors);
  }
  
  void setStockPriceService(StockPriceService aStockPriceService) {
    stockPriceService = aStockPriceService;
  }
}