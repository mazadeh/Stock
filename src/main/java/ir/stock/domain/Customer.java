package ir.stock.domain;

import java.util.Map;
import java.util.HashMap;

public class Customer
{
	private int id;
	private String firstname;
	private String lastname;
	private String username;
	private String password;
	private int depositedAmount;
	private Map<String, Integer> shareList;
	
	public Customer(int id, String firstname, String lastname, String username, String password, int depositedAmount)
	{
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.password = password;
		this.depositedAmount = depositedAmount;
		shareList = new HashMap<String, Integer>();
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	public int getId()
	{
			return id;
	}
	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}
	public String getFirstname()
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
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getPassword()
	{
		return password;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	public String getUsername()
	{
		return username;
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
