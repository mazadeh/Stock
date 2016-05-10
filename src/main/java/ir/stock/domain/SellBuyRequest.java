package ir.stock.domain;

import java.util.Map;
// import java.sql.Date;
// import java.text.SimpleDateFormat;
// import java.text.ParseException;

public class SellBuyRequest
{
	//private SimpleDateFormat format;

	private int id;
	private int customerId;
	private int symbolId;
	private int quantity;
	private int price;
	private Type type;
	private boolean isSell;
	private String status;
	private String time;
	
	public SellBuyRequest(int id, int customerId, int symbolId, int quantity, int price, String type, boolean isSell)
	{
		this.id = id;
		this.customerId = customerId;
		this.symbolId = symbolId;
		this.quantity = quantity;
		this.price = price;
		//this.type = type;
		this.isSell = isSell;
		
		String t = "ir.stock.domain.Type" + type.toUpperCase();
		try
		{
			Class clazz = Class.forName(t);
			this.type = (Type)clazz.newInstance();
		}
		catch (ClassNotFoundException ex)
		{
			System.err.println("Can not find class " + t);
			System.err.println(ex);
		}
		catch (InstantiationException ex)
		{
			System.err.println(ex);
		}
		catch (IllegalAccessException ex)
		{
			System.err.println(ex);
		}
		
		//format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	public int getId()
	{
		return id;
	}
	public void setSymbolId(int symbolId)
	{
		this.symbolId = symbolId;
	}
	public int getSymbolId()
	{
		return symbolId;
	}
	
	public void setCustomerId(int customerId)
	{
		this.customerId = customerId;
	}
	public int getCustomerId()
	{
		return customerId;
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
	public void setStatus(String status)
	{
		this.status = status;
	}
	public String getStatus()
	{
		return status;
	}
	public void setTime(String time)
	{
		this.time = time;
	}
	public String getTime()
	{
		return this.time;
	}
}
