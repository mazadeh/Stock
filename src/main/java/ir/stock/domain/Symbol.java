package ir.stock.domain;

import java.util.List;
import java.util.ArrayList;

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
		
		sellList = new ArrayList<SellBuyRequest>();
		buyList = new ArrayList<SellBuyRequest>();
	}
	
	public Symbol( int id, String name, List<SellBuyRequest> sellList, List<SellBuyRequest> buyList)
	{
		this.id = id;
		this.name = name;
		this.sellList = sellList;
		this.buyList = buyList;
	}
	
	public int getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public List<SellBuyRequest> getSellList()
	{
		return sellList;
	}
	
	public List<SellBuyRequest> getBuyList()
	{
		return buyList;
	}
}
