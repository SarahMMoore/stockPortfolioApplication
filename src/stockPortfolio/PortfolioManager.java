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
		String userOption = "Please enter a number 1 to 6 or 0 to exit: ";	// Ask for user input
		String exitMessage = "Thank you! Good bye!";
		String userCash = "CASH";
		String userDeposit = "DEPOSIT";
		String userWithdraw = "WITHDRAW";
		String userPurchase = "BUY";
		String userSale = "SELL";
		String userQty = "QTY";
		String depMessage = "Amount to deposit: ";
		String withMessage = "Amount to withdraw: ";
		String nsfMessage = "Insufficient funds.";
		String tickMessage = "Ticker: ";
		String nsfCashMessage = "Not enough cash!";
		String invalidInput = "Invalid entry. ";							// Invalid entry
		String ppsMessage = "Price per share: ";
		String cbMessage = "Cost Basis: ";
		
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
				manager.cashBalance += deposit;
				manager.record(userCash, userDeposit, 1, deposit);
				break;
	
			case 2:
				System.out.println(withMessage);
				double withdraw = input.nextDouble();
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
				System.out.println("Price per share: ");
				double buyPrice = input.nextDouble();
				
				if((buyQty * buyPrice) <= manager.cashBalance) {
					manager.cashBalance -= (buyQty * buyPrice);
					manager.record(buyTicker, userPurchase, buyQty, buyPrice);
				}
				else {
					System.out.println(nsfCashMessage);
				}
				break;
			// TO DO: SELL STOCK
			case 4:
				System.out.println("NEEDS FINISHINGSell stock:   ");
				break;
			// TO DO: DISPLAY TRANSACTION HISTORY
			case 5:
				System.out.println("NEEDS FINISHINGDisplay Transaction History:   ");
				break;
			// TO DO: DISPLAY PORTFOLIO
			case 6:
				System.out.println("NEEDS FINISHINGDisplay PORTFOLIO:   ");
				break;
			// EXIT
			case 0:
				System.out.println("NEEDS FINISHINGYou are exiting the program. Thank you, good bye!   ");
				break;
			default:
				System.out.println("Invalid entry. Your options are 0 through 6: ");
			}			
		}
	}
	private void record(String ticker, String type, double qty, double price) {
        String date = LocalDate.now().toString();
        portfolioList.add(new TransactionHistory(ticker, date, type, qty, price));
    }
}
