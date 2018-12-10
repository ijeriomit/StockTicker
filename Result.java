package stockticker;

import java.util.Map;

class Result {

  Map<String, String>  errors;
  Map<String, Integer> prices;

  Result(Map<String, Integer> thePrices, Map<String, String> theErrors){
    prices = thePrices;
    errors = theErrors;
  }
}