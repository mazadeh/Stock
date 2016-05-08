package ir.stock.domain;

import java.util.Map;

public class SellBuyRequest
{
	private int id;
	private Symbol symbol;
	private int quantity;
	private int price;
	private Type type;
	private boolean isSell;
	
	SellBuyRequest(int id, Symbol symbol, int quantity, int price, Type type, boolean isSell)
	{
		this.id = id;
		this.symbol = symbol;
		this.quantity = quantity;
		this.price = price;
		this.type = type;
		this.isSell = isSell;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		sb.append("\t");
		sb.append(symbol);
		sb.append("\t");
		sb.append(price);
		sb.append("$\t");
		sb.append(quantity);
		sb.append("#\t");
		sb.append(type);
		return sb.toString();
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	public int getId()
	{
		return id;
	}
	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}
	public String getSymbol()
	{
		return symbol;
	}
	public void setPrice(int price)
	{
		this.price = price;
	}
	public int getPrice()
	{
		return price;
	}
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}
	public int getQuantity()
	{
		return quantity;
	}
	public void setType(Type type)
	{
		this.type = type;
	}
	public Type getType()
	{
		return type;
	}
	public void setIsSell(boolean isSell)
	{
		this.isSell = isSell;
	}
	public boolean getIsSell()
	{
		return isSell;
	}
}
