/*
 * Class Name: PortfolioManager
 * Author: Sarah Moore
 * Date created: 05. February 2026
 * Purpose: Contains the main method, 
 * maintains the Portfolio 
 * and allows for user interaction.
 */

package stockPortfolio;

import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class PortfolioManager {

    private ArrayList<TransactionHistory> portfolioList = new ArrayList<TransactionHistory>();
    private double cashBalance = 0.0;
  
    private static final String userCash = "CASH";
    private static final String userDeposit = "DEPOSIT";
    private static final String userWithdraw = "WITHDRAW";
    private static final String userPurchase = "BUY";
    private static final String userSale = "SELL";
    private static final String historyRowFormat = "%-10s %-8s %-10.1f $%-10.1f %-10s%n";
    private static final String userCashMsgFormat = "%-8s %.1f%n";

    public static void main(String[] args) {
        PortfolioManager manager = new PortfolioManager();
        Scanner input = new Scanner(System.in);
        
        int choice = -1;

        String brokerName = "\nSarah Moore ";
        String brokerName2 = "\t   Sarah Moore ";
        String brokerageTitle = "Brokerage Account\n";
        String welcomeMessage = "Welcome to your portfolio." + 
        		"\nI keep track of:" + 
        		"\n\tStock purchases, stock sales," +
        		"\n\tcash deposits, and cash withdraws";
        String brokerageMenu = "\nHow can I help you today? "
        		+ "\nPlease choose an option from the menu\n\n  "
        		+ "MENU"
        		+ "\n\t0 - Exit"
        		+ "\n\t1 - Deposit Cash"
        		+ "\n\t2 - Withdraw Cash"
        		+ "\n\t3 - Buy Stock"
        		+ "\n\t4 - Sell Stock"
        		+ "\n\t5 - Display Transaction History"
        		+ "\n\t6 - Display Portfolio";
        String userOption = "\nEnter an option 1 through 6, or enter 0 to exit: ";
        String historyHeader = "\nDate       "
        		+ "Ticker   "
        		+ "Quantity   "
        		+ "Cost Basis   "
        		+ "Trans Type"
        		+ "\n========================================================";
        String borderLine = "========================================================";
        String tickerQtyMsg = "Ticker   Quantity\n==================";        
        String depAmount = "Amount to deposit: ";
        String withAmount = "Amount to withdraw: ";
        String sellQtyMsg = "Quantity to sell: ";
        String qty = "Quantity to buy: ";                
        String ticker = "Ticker: ";
        String ppShare = "Price per share: ";
        String sellPriceMsg = "Selling price: ";        
        String portAsOf = "\nPortfolio as of: ";
        String goodBye = "Thank you! Good bye!";          
        String errorEntryInvalid = "Invalid entry. ";
        String nsfMsg = "Insufficient funds.";        
        String nsfCash = "Not enough cash!";    
        String dnoError = "Error: You do not own ";
        String nsfSharesError = "Insufficient shares. You only own %.1f%n";
        

        while (choice != 0) {
            System.out.println(brokerName);
            System.out.println(brokerageTitle);
            System.out.println(welcomeMessage);
            System.out.println(brokerageMenu);
            System.out.print(userOption);
            try {
                if (input.hasNextInt()) {
                    choice = input.nextInt();
                    input.nextLine(); 
                } else {
                    System.out.println(errorEntryInvalid);
                    input.nextLine(); 
                    choice = -1; 
                    continue; 
                }
            } catch (Exception e) {
                System.out.println(errorEntryInvalid);
                input.nextLine();
                continue;
            }

            switch (choice) {
                case 1: 
                    System.out.print(depAmount);
                    double deposit = input.nextDouble();
                    input.nextLine();
                    manager.cashBalance += deposit;
                    manager.record(userCash, userDeposit, deposit, 1.0);
                    break;

                case 2: 
                	System.out.print(withAmount);
                    double withdraw = input.nextDouble();
                    input.nextLine();
                    
                    if (withdraw > manager.cashBalance) {
                        System.out.println(nsfMsg); 
                    } else {
                        manager.cashBalance -= withdraw;
                        manager.record(userCash, userWithdraw, -withdraw, 1.0);
                    }
                    break;

                case 3: 
                	System.out.print(ticker);
                    String buyTicker = input.nextLine().toUpperCase();
                    System.out.print(qty);
                    double buyQty = input.nextDouble();
                    System.out.print(ppShare);
                    double buyPrice = input.nextDouble();
                    input.nextLine();
                    
                    double totalCost = buyQty * buyPrice;
                    
                    if (totalCost > manager.cashBalance) {
                        System.out.println(nsfCash); 
                    } else {
                        manager.cashBalance -= totalCost;
                        manager.record(buyTicker, userPurchase, buyQty, buyPrice);
                        manager.record(userCash, userWithdraw, -totalCost, 1.0);
                    }
                    break;

                case 4: 
                	System.out.print(ticker);
                    String sellTicker = input.nextLine().toUpperCase();

                    double currentShares = manager.getSharesOwned(sellTicker);

                    if (currentShares <= 0) {
                        System.out.println(dnoError + sellTicker);
                        break;
                    }

                    System.out.print(sellQtyMsg);
                    double sellQty = input.nextDouble();
                    input.nextLine(); 

                    if (sellQty > currentShares) {                      
                        System.out.printf(nsfSharesError, currentShares);
                    } else {
                        System.out.print(sellPriceMsg);
                        double sellPrice = input.nextDouble();
                        input.nextLine();                         
                        double totalProceeds = sellQty * sellPrice;

                        manager.cashBalance += totalProceeds;
                        manager.record(sellTicker, userSale, -sellQty, sellPrice);
                        manager.record(userCash, userDeposit, totalProceeds, 1.0);
                    }
                    break;

                case 5: 
                	 System.out.print(brokerName2 + brokerageTitle);
                	 System.out.println(borderLine);
                	    System.out.println(historyHeader); 
                	    
                	    for (TransactionHistory th : manager.portfolioList) {
                	        System.out.printf(historyRowFormat, 
                	            th.getTransDate(), 
                	            th.getTicker(), 
                	            th.getQty(), 
                	            th.getCostBasis(), 
                	            th.getTransType());
                	    }
                	    System.out.println(borderLine);
                	    break;

                case 6: 
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
                    System.out.println(portAsOf + dtf.format(now));
                    System.out.println(borderLine);
                    System.out.println(tickerQtyMsg);
                    System.out.printf(userCashMsgFormat, userCash, manager.cashBalance);
                    manager.displayHoldings();
                    break;

                case 0:
                    System.out.println(goodBye);
                    break;
                    
                default:
                    System.out.println(errorEntryInvalid);
                    break;
            }
        }
        input.close();
    }

    private void record(String ticker, String type, double qty, double price) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = LocalDate.now().format(dtf);
        portfolioList.add(new TransactionHistory(ticker, formattedDate, type, qty, price));
    }

    private double getSharesOwned(String ticker) {
        double total = 0.0;
        for (TransactionHistory th : portfolioList) {
            if (th.getTicker().equalsIgnoreCase(ticker)) {
                total += th.getQty();
            }
        }
        return total;
    }

    private void displayHoldings() {
        HashMap<String, Double> holdings = new HashMap<>();
        for (TransactionHistory th : portfolioList) {
            if (th.getTicker().equals(userCash)) continue;
            holdings.put(th.getTicker(), holdings.getOrDefault(th.getTicker(), 0.0) + th.getQty());
        }
        holdings.forEach((ticker, qty) -> {
            if (qty > 0) {
                System.out.printf(userCashMsgFormat, ticker, qty);
            }
        });
    }
}