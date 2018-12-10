package stockticker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UHStockPriceService implements StockPriceService{

  @Override
  public int getPrice(String ticker) {
    int convertedData;

    try {
      String responseString = getResponse(ticker);
      convertedData = (int)(Double.parseDouble(responseString)*100);

    } catch (IOException e) {
      if(e.getMessage() == "Invalid Ticker")
        throw new RuntimeException(e.getMessage());
      else
        throw new RuntimeException("NETWORK ERROR 503: https://agile.cs.uh.edu/stock?ticker="+ticker);
    }
      return convertedData;
  }

  public String getResponse(String ticker) throws IOException {
    HttpURLConnection url = (HttpURLConnection) new URL("http://agile.cs.uh.edu/stock?ticker=" + ticker).openConnection();

    if(url.getResponseCode() != 200){
      throw new IOException("Invalid Ticker");
    }

    return new BufferedReader(new InputStreamReader(url.getInputStream())).readLine();
  }
}
