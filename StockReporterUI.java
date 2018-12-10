package stockticker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StockReporterUI {

  public static void main(String[] args) throws Exception {
     StockReporter stockReporter = new StockReporter();
     stockReporter.setStockPriceService(new UHStockPriceService());
     Result result = readFile(stockReporter);

     System.out.println("-------------------------------------------------------\n   STOCKS\n=============");
     printStocksFromMap(result.prices);
     System.out.println("-------------------------------------------------------");
     System.out.println("Highest Ticker: ");
     printStocksFromMap(stockReporter.getHighestPrices(result.prices));
     System.out.println("\nLowest Ticker: ");
     printStocksFromMap(stockReporter.getLowestPrices(result.prices));
     System.out.println("-------------------------------------------------------\n\tERRORS\n=======================");
     printErrors(result.errors);
  }

  private static void printStocksFromMap(Map<String, Integer> result) {
     for(Map.Entry<String, Integer>entry : result.entrySet()){
        System.out.printf(entry.getKey() + "\t" + "$" + "%.2f", (double)entry.getValue()/100);
        System.out.println();
     }
  }

  private static void printErrors(Map<String, String> result){
    for(Map.Entry<String, String>entry : result.entrySet()){
      System.out.println(entry.getKey() + "\t" + entry.getValue());
    }
  }

  private static Result readFile(StockReporter stockReporter) throws Exception {
    BufferedReader br = new BufferedReader(new FileReader(new File("stocks.txt")));
    List<String> stocksList = new ArrayList<>();
    String st;
    while((st = br.readLine())!=null)
      stocksList.add(st);
    return stockReporter.fetchPrices(stocksList);
  }
}
