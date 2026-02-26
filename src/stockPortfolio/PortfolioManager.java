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

/*	
 * This program tracks a personal brokerage account.
 * It keeps a record of cash deposits and withdraws, 
 * and a record of stock sales and purchases.  
*/

public class PortfolioManager {

	private ArrayList<TransactionHistory> portfolioList = new ArrayList<TransactionHistory>();
	private double cashBalance = 0.0;
	private static final String userCash = "CASH";
    private static final String userDeposit = "DEPOSIT";
    private static final String userWithdraw = "WITHDRAW";
    private static final String userPurchase = "BUY";
    private static final String userSale = "SELL";

	public static void main(String[] args) {
		PortfolioManager manager = new PortfolioManager();
		Scanner input = new Scanner(System.in);
		int choice = -1;
		
		
		String brokerName = "Sarah Moore ";									// Broker Name
		String brokerageTitle = "Brokerage Account";						// Brokerage Title
		String welcomeMessage = "Welcome to your portfolio. " +				// Welcome message and instructions
				"\nI keep track of:  Stock purchases, stock sales, " +
				"\n                  cash desposits, and cash withdraws" +
				"\nHow can I help you today?\n\n";
		String brokerageMenu = "\tMenu" +									// Menu
				"\n\t\t0 - Exit" +
				"\n\t\t1 - Desposit Cash" + 
				"\n\t\t2 - Withdraw Cash" +
				"\n\t\t3 - Buy Stock" + 
				"\n\t\t4 - Sell Stock" +
				"\n\t\t5 - Display Transaction History" +
				"\n\t\t6 - Display Portfolio";
		String printTransMessage = "%s | %-6s | %-8s | %-5.2f | $%.2f%n";
		String userOption = "Please enter a number 1 to 6 or 0 to exit: ";	// Ask for user input
		String exitMessage = "Thank you! Good bye!";
		String userQty = "QTY";
		String depMessage = "Amount to deposit: ";
		String withMessage = "Amount to withdraw: ";
		String nsfMessage = "Insufficient funds.";
		String tickMessage = "Ticker: ";
		String qtyMessage = "Quantity: "; 
		String nsfCashMessage = "Not enough cash!";
		String invalidInput = "Invalid entry. ";							// Invalid entry
		String ppsMessage = "Price per share: ";
		String spMessage = "Selling price: ";
		String ownMessage = "Error: You do not own any shares of ";
		String nsfSharesMessage = "Error: Insufficient shares. You only own %.2f shares of %s.%n";
		String portHead = "\nDate       | Ticker | Type     | Qty   | Cost/Price";
		String currentCsh = "Current Cash Balance: $";
		String histMessage = "View History (Option 5) for full details.";

		String stckMessage = "\n--- Current Stock Holdings ---";
		String successMsg = "Sale successful!";
		
		while (choice != 0) {
			System.out.println(brokerName + brokerageTitle);			// Name of Brokerage Account
			System.out.println(welcomeMessage);							// Program description and function
			System.out.println(brokerageMenu);							// Brokerage Menu
			if (input.hasNextInt()) {									// User inputs menu choice
				choice = input.nextInt();
				input.nextLine();
			}
			else {
				System.out.println(invalidInput + userOption);
				input.next();
				continue;
			}
			switch (choice) {

			case 1:
				System.out.println(depMessage);
				double deposit = input.nextDouble();
				input.nextLine();
				manager.cashBalance += deposit;
				manager.record(userCash, userDeposit, 1, deposit);
				break;
	
			case 2:
				System.out.println(withMessage);
				double withdraw = input.nextDouble();
				input.nextLine();
				if (withdraw <= manager.cashBalance) {
					manager.cashBalance -= withdraw;
					manager.record(userCash, userWithdraw, 1, withdraw);
				}
				else {
					System.out.println(nsfMessage);
				}				
				break;

			case 3:
				System.out.println(tickMessage);
				String buyTicker = input.nextLine().toUpperCase();
				System.out.println(userQty);
				double buyQty = input.nextDouble();
				System.out.println(ppsMessage);
				double buyPrice = input.nextDouble();
				input.nextLine();
				
				if((buyQty * buyPrice) <= manager.cashBalance) {
					manager.cashBalance -= (buyQty * buyPrice);
					manager.record(buyTicker, userPurchase, buyQty, buyPrice);
				}
				else {
					System.out.println(nsfCashMessage);
				}
				break;

			case 4:
				System.out.print(tickMessage);
			    String sellTicker = input.nextLine().toUpperCase();
			    
			    double currentShares = manager.getSharesOwned(sellTicker);
			    
			    if (currentShares <= 0) {
			        System.out.println(ownMessage + sellTicker);
			        break;
			    }

			    System.out.print(qtyMessage);
			    double sellQty = input.nextDouble();

			    if (sellQty > currentShares) {
			        System.out.printf(nsfSharesMessage, currentShares, sellTicker);
			        input.nextLine(); 
			        break;
			    }

			    System.out.print(spMessage);
			    double sellPrice = input.nextDouble();
			    input.nextLine(); 
			    
			    manager.cashBalance += (sellQty * sellPrice);
			    manager.record(sellTicker, userSale, sellQty, sellPrice);
			    System.out.println(successMsg);
			    break;

			case 5:
				System.out.println(portHead);
			    for (TransactionHistory th : manager.portfolioList) {  	
			        System.out.printf(printTransMessage, 
			        		th.getTransDate(), th.getTicker(), th.getTransType(), th.getQty(), th.getCostBasis());
			    }
                break;

			case 6:
				 System.out.println(currentCsh + String.format("%.2f", manager.cashBalance));
				 System.out.println(stckMessage);
				 manager.displayHoldings(); 
				    System.out.println("\n" + histMessage);
                break;

			case 0:
                System.out.println(exitMessage);
                break;
			default:
				System.out.println(invalidInput + userOption);
			}			
		}
		input.close();
	}
	
	private void record(String ticker, String type, double qty, double price) {
        String date = LocalDate.now().toString();
        portfolioList.add(new TransactionHistory(ticker, date, type, qty, price));
	}
        
    private double getSharesOwned(String ticker) {
        double total = 0.0;
        		
        for (TransactionHistory th : portfolioList) {
            if (th.getTicker().equalsIgnoreCase(ticker)) {
                if (th.getTransType().equalsIgnoreCase(userPurchase)) {
                    total += th.getQty();
                } else if (th.getTransType().equalsIgnoreCase(userSale)) {
                    total -= th.getQty();
                }
            }
        }
        return total;
    	}    
    
    private void displayHoldings() {
	    java.util.HashMap<String, Double> holdings = new java.util.HashMap<>();
		String notOwned = "No stocks currently owned.";
		String ownTicker = "Ticker: %-6s | Total Shares: %.2f%n";


	    for (TransactionHistory th : portfolioList) {
	        String ticker = th.getTicker();
	        if (ticker.equals(userCash)) continue; 

	        double currentQty = holdings.getOrDefault(ticker, 0.0);
	        
	        if (th.getTransType().equalsIgnoreCase(userPurchase)) {
	            holdings.put(ticker, currentQty + th.getQty());
	        } else if (th.getTransType().equalsIgnoreCase(userSale)) {
	            holdings.put(ticker, currentQty - th.getQty());
	        }
	    }

	    if (holdings.isEmpty()) {
	        System.out.println(notOwned);
	    } 
	    else {
	    	holdings.forEach((ticker, qty) -> {
	    		if (qty > 0) {
	                System.out.printf(ownTicker, ticker, qty);
	            }
	        });
	    }
	
	
	}
}
