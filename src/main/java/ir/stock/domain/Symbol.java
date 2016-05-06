package ir.stock.domain;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Collection;

public class Symbol
{
	private String name;
	private int price;
	private int quantity;
	private int id;
	//private BuySellQueues buySellQueues;
	
	Symbol( String name,
		   int price,
		   int quantity,
		   int id )
		   //BuySellQueues buySellQueues )
	{
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.id = id;
		//this.buySellQueues = buySellQueues;
	}
	
	String getSymbolName()
	{
		return name;
	}
	
	int getSymbolPrice()
	{
		return price;
	}
	
	int getSymbolQuantity()
	{
		return quantity;
	}
	
	int getSymbolID()
	{
		return id;
	}
}
