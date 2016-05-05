package ir.stock.domain;

import java.util.Map;
import java.util.HashMap;

public class Customer
{
	private String firstname;
	private String lastname;
	private int id;
	private int depositedAmount;
	private Map<String, Integer> shareList;
	private boolean admin;
	
	public Customer(int id, String firstname, String lastname)
	{
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		depositedAmount = 0;
		admin = false;
		shareList = new HashMap<String, Integer>();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (admin)
			sb.append('*');
		sb.append(id);
		sb.append('\t');
		sb.append(firstname);
		sb.append('\t');
		sb.append(lastname);
		sb.append('\t');
		sb.append(depositedAmount);
		sb.append('$');
		return sb.toString();
	}

	public boolean isAdmin()
	{
		return admin;
	}
	public void setAdmin(boolean admin)
	{
		this.admin = admin;
	}
	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}
	public String getFistname()
	{
		return firstname;
	}
	public void setLastname(String lastname)
	{
		this.lastname = lastname;
	}
	public String getLastname()
	{
		return lastname;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public int getId()
	{
			return id;
	}
	public void setDepositedAmount(int depositedAmount)
	{
		this.depositedAmount = depositedAmount;
	}
	public int getDepositedAmount()
	{
		return depositedAmount;
	}
	public void deposit(int amount)
	{
		depositedAmount += amount;
	}
	public boolean withdraw(int amount)
	{
		if (amount <= depositedAmount)
		{
			depositedAmount -= amount;
			return true;
		}
		return false;
	}
	public void sell(String symbol, int price, int quantity)
	{
		shareList.put(symbol, shareList.get(symbol) - quantity);
		depositedAmount += price * quantity;
		if (shareList.get(symbol) < 0)
			System.err.println("Customer " + id + " wronq quantity of instrument " + symbol);
	}
	public void buy(String symbol, int price, int quantity)
	{
		if (shareList.get(symbol) == null)
			shareList.put(symbol, quantity);
		else
			shareList.put(symbol, shareList.get(symbol) + quantity);
	}
	public boolean hasEnoughShare(String symbol, int quantity)
	{
		if (shareList.get(symbol) == null || shareList.get(symbol) < quantity)
			return false;
		return true;
	}
}
