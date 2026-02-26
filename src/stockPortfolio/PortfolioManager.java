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

	private ArrayList<TransactionHistory> portfolioList = new ArrayList<>();
	private double cashBalance = 0.0;

	private static final String USER_CASH = "CASH";
	private static final String USER_DEP = "DEPOSIT";
	private static final String USER_WITH = "WITHDRAW";
	private static final String USER_PURC = "BUY";
	private static final String USER_SALE = "SELL";
	private static final String BKR_NAME = "\nSarah Moore ";
	private static final String BKR_NAME2 = "\n\t   Sarah Moore ";
	private static final String BKG_TITLE = "Brokerage Account\n";
	private static final String WELCOME_MSG = """
			Welcome to your portfolio.
			I keep track of:
			    Stock purchases, stock sales,
			    cash deposits, and cash withdraws
			""";
	private static final String BROKERAGE_MENU = """
			How can I help you today?
			Please choose an option from the menu

			  MENU
			    0 - Exit
			    1 - Deposit Cash
			    2 - Withdraw Cash
			    3 - Buy Stock
			    4 - Sell Stock
			    5 - Display Transaction History
			    6 - Display Portfolio
			""";
	private static final String USR_OPT = "\nEnter an option 1 through 6, or enter 0 to exit: ";
	private static final String HISTORY_HEADER = """
			Date       Ticker   Quantity   Cost Basis   Trans Type
			========================================================
			""";
	private static final String BORD_LN = "========================================================";
	private static final String TICK_QTY_MSG = "Ticker   Quantity\n==================";
	private static final String DEP_AMT = "Amount to deposit: ";
	private static final String WITH_AMT = "Amount to withdraw: ";
	private static final String SELL_QTY_MSG = "Quantity to sell: ";
	private static final String QTY = "Quantity to buy: ";
	private static final String TICK = "Ticker: ";
	private static final String PRICE_PER_SHARE = "Price per share: ";
	private static final String SELL_PRICE_MCG = "Selling price: ";
	private static final String PORT_AS_OF = "\nPortfolio as of: ";
	private static final String GOOD_BYE = "Thank you! Good bye!";
	private static final String ERROR_INV_MSG = "Invalid entry. ";
	private static final String NSF_MSG = "Insufficient funds.";
	private static final String NSF_CASH = "Not enough cash!";
	private static final String DO_NOT_OWN = "Error: You do not own ";
	private static final String NSF_SHARES_MSG = "Insufficient shares. You only own %.1f%n";
	private static final String HISTORY_ROW_FORMAT = "%-10s %-8s %-10.1f $%-10.1f %-10s%n";
	private static final String CASH_MSG_FORMAT = "%-8s %.1f%n";

	public static void main(String[] args) {
		PortfolioManager manager = new PortfolioManager();
		Scanner input = new Scanner(System.in);

		int choice = -1;

		while (choice != 0) {
			System.out.println(BKR_NAME);
			System.out.println(BKG_TITLE);
			System.out.println(WELCOME_MSG);
			System.out.println(BROKERAGE_MENU);
			System.out.print(USR_OPT);
			try {
				if (input.hasNextInt()) {
					choice = input.nextInt();
					input.nextLine();
				} else {
					System.out.println(ERROR_INV_MSG);
					input.nextLine();
					choice = -1;
					continue;
				}
			} catch (Exception e) {
				System.out.println(ERROR_INV_MSG);
				input.nextLine();
				continue;
			}

			switch (choice) {
			case 1:
				System.out.print(DEP_AMT);
				double deposit = input.nextDouble();
				input.nextLine();
				manager.cashBalance += deposit;
				manager.record(USER_CASH, USER_DEP, deposit, 1.0);
				break;

			case 2:
				System.out.print(WITH_AMT);
				double withdraw = input.nextDouble();
				input.nextLine();

				if (withdraw > manager.cashBalance) {
					System.out.println(NSF_MSG);
				} else {
					manager.cashBalance -= withdraw;
					manager.record(USER_CASH, USER_WITH, -withdraw, 1.0);
				}
				break;

			case 3:
				System.out.print(TICK);
				String buyTicker = input.nextLine().toUpperCase();
				System.out.print(QTY);
				double buyQty = input.nextDouble();
				System.out.print(PRICE_PER_SHARE);
				double buyPrice = input.nextDouble();
				input.nextLine();

				double totalCost = buyQty * buyPrice;

				if (totalCost > manager.cashBalance) {
					System.out.println(NSF_CASH);
				} else {
					manager.cashBalance -= totalCost;
					manager.record(buyTicker, USER_PURC, buyQty, buyPrice);
					manager.record(USER_CASH, USER_WITH, -totalCost, 1.0);
				}
				break;

			case 4:
				System.out.print(TICK);
				String sellTicker = input.nextLine().toUpperCase();

				double currentShares = manager.getSharesOwned(sellTicker);

				if (currentShares <= 0) {
					System.out.println(DO_NOT_OWN + sellTicker);
					break;
				}

				System.out.print(SELL_QTY_MSG);
				double sellQty = input.nextDouble();
				input.nextLine();

				if (sellQty > currentShares) {
					System.out.printf(NSF_SHARES_MSG, currentShares);
				} else {
					System.out.print(SELL_PRICE_MCG);
					double sellPrice = input.nextDouble();
					input.nextLine();
					double totalProceeds = sellQty * sellPrice;

					manager.cashBalance += totalProceeds;
					manager.record(sellTicker, USER_SALE, sellQty, sellPrice);
					manager.record(USER_CASH, USER_DEP, totalProceeds, 1.0);
				}
				break;

			case 5:
				System.out.print(BKR_NAME2 + BKG_TITLE);
				System.out.println(BORD_LN);
				System.out.println(HISTORY_HEADER);

				for (TransactionHistory th : manager.portfolioList) {
					System.out.printf(HISTORY_ROW_FORMAT, th.getTransDate(), th.getTicker(), th.getQty(),
							th.getCostBasis(), th.getTransType());
				}
				System.out.println(BORD_LN);
				break;

			case 6:
				LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
				System.out.println(PORT_AS_OF + dtf.format(now));
				System.out.println(BORD_LN);
				System.out.println(TICK_QTY_MSG);
				System.out.printf(CASH_MSG_FORMAT, USER_CASH, manager.cashBalance);
				manager.displayHoldings();
				break;

			case 0:
				System.out.println(GOOD_BYE);
				break;

			default:
				System.out.println(ERROR_INV_MSG);
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
				if (th.getTransType().equals(USER_SALE)) {
					total -= th.getQty();
				} else {
					total += th.getQty();
				}
			}
		}
		return total;
	}

	private void displayHoldings() {
		HashMap<String, Double> holdings = new HashMap<>();
		for (TransactionHistory th : portfolioList) {
			if (th.getTicker().equals(USER_CASH))
				continue;

			double amount = th.getQty();
			if (th.getTransType().equals(USER_SALE)) {
				amount = -amount;
			}
			holdings.put(th.getTicker(), holdings.getOrDefault(th.getTicker(), 0.0) + amount);
		}
		holdings.forEach((ticker, qty) -> {
			if (qty > 0) {
				System.out.printf(CASH_MSG_FORMAT, ticker, qty);
			}
		});
		System.out.println(BORD_LN);
	}
}