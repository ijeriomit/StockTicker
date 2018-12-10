package stockticker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class UHStockPriceServiceTest {
  private UHStockPriceService service;

  @BeforeEach
  void init(){
        service = new UHStockPriceService();
    }

  @Test
  void assertGOOGResponse(){
    assertTrue(service.getPrice("GOOG") > 0);

  }

  @Test
  void assertPricesAreDifferentForDifferentTickers(){
    assertTrue(service.getPrice("GOOG") != service.getPrice("AMZN"));
  }

  @Test
  void assertINVALIDResponse(){
    assertThrows(Exception.class, ()->service.getPrice("INVALID"));
  }

  @Test
  void checkConnectionFailure() throws IOException {
    service = Mockito.spy(new UHStockPriceService());
    when(service.getResponse("GOOG")).thenThrow(new IOException());

    assertThrows(Exception.class,()->service.getPrice("GOOG"));
  }
}