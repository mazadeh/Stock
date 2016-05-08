package ir.stock.domain;

import java.util.List;

public class Symbol
{
	private int id;
	private String name;
	
	private List<SellBuyRequest> sellList;
	private List<SellBuyRequest> buyList;
	
	public Symbol( int id, String name)
	{
		this.id = id;
		this.name = name;
		
		sellList = new List<SellBuyRequest>();
		buyList = new List<SellBuyRequest>();
	}
	
	public int getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
}
