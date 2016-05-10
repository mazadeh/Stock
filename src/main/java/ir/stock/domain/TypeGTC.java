package ir.stock.domain;

import java.util.*;
import java.io.*;

public class TypeGTC extends Type
{
	static
	{
		System.err.println("GTC-Type Class Loaded");
	}
	public TypeGTC()
	{
		this.name = "GTC";
	}
	public int sell(SellBuyRequest sellRequest)
	{
		return 0;
	}
	public int buy(SellBuyRequest buyRequest)
	{
		return 0;
	}
	public void transaction(SellBuyRequest request)
	{
		if (request.getIsSell())	// It is a sell request
		{
		}
		else	// It is a buy request
		{
			System.err.println("Transacting buy request: " + request);
		}
	}
	
	public String getName()
	{
		return this.name;
	}
}
