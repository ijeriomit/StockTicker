# StockTicker
Stock price simulator
Program fetches simulated prices from a service at http://agile.cs.uh.edu/stock
Then outputs a table of all stock prices as well as lowest and highest prices.

The StockReporter, UHStockPriceService, StockReporterUI, Result, and StockPriceService .java files are required to run program
StockReporterTest and UHStockPriceServiceTest .java files are required for automated tests

StockReporterUI contains main and is used to execute code
StockReporter handles the organization and storage of stock data
StockPriceService is an interface
UHStockPriceService implements StockPriceService interface and fetches prices from website url
Result is class used to store price and error data

Some version of Java SDK is required to run project
Mockito ibraries are required to run tests 
Internet connection is needed to use service
Build with gradle 
