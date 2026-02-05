/*
 * Class Name: TransactionHistory
 * Author: Sarah Moore
 * Date created: 05. February 2026
 * Purpose: Defines a transaction and 
 * provides the template for storing data.
 */

package stockPortfolio;

public class TransactionHistory {
	
	private String ticker;
	private String transDate;
	private String transType;
	private double qty;
	private double costBasis;
	
	public TransactionHistory() {
		this.ticker = "";
		this.transDate = "";
		this.transType = "";
		this.qty = 0.0;
		this.costBasis = 1.00;
	}
	
	public TransactionHistory(String ticker, String transDate, String transType, double qty, double costBasis) {
		this.ticker = ticker;
		this.transDate = transDate;
		this.transType = transType;
		this.qty = qty;
		this.costBasis = costBasis;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public double getCostBasis() {
		return costBasis;
	}

	public void setCostBasis(double costBasis) {
		this.costBasis = costBasis;
	}

}
