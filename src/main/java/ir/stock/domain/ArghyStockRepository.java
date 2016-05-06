package ir.stock.domain;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Collection;

/**
 * Stock Repository
 * Database is kept here
 */
public class ArghyStockRepository
{
	private static Map<Integer, Customer> customerList;
	private static Customer admin;
	private static Customer admin2;
	private static Map<Integer, Symbol> symbolList;
	
	
	static 
	{
		customerList = new HashMap<Integer, Customer>();
		admin = new Customer(1, "password", "Admin", "Administrator");
		admin.setAdmin(true);
		customerList.put(1, admin);
		admin2 = new Customer(2, "pass", "Mohammad", "Azadeh");
		admin2.setAdmin(true);
		customerList.put(2, admin2);
		
		symbolList = new HashMap<Integer, Symbol>();
		symbolList.put( 1, new Symbol( "symbol1",2400, 500, 1 ) );
		symbolList.put( 2, new Symbol( "symbol2",4800, 1000, 2 ) );
		symbolList.put( 3, new Symbol( "symbol3",9600, 2000, 3 ) );
		symbolList.put( 4, new Symbol( "symbol4",19200, 4000, 4 ) );
	}
	
	public static void addCustomer(Customer newCustomer)
	{
		if (customerList.get(newCustomer.getId()) != null)
		{
			newCustomer.setId(customerList.size() + 1);
		}
		
		customerList.put(newCustomer.getId(),newCustomer);
	}
	
	public static Customer getCustomer(int id)
	{
		return customerList.get(id);
	}
	
	public static Set<Integer> getCustomersId()
	{
		return customerList.keySet();
	}
	
	public static List<Customer> getCustomerList()
	{
		Collection<Customer> customers = customerList.values();
		return new ArrayList<Customer>(customers);
	}
	
	public static int getCustomerSize()
	{
		return customerList.size();
	}
	//////////////////////////////////////////////
	public static Symbol getSymbol(int id)
	{
		return symbolList.get(id);
	}
	
	public static Set<Integer> getSymbolsId()
	{
		return symbolList.keySet();
	}
	
	public static List<Symbol> getSymbolList()
	{
		Collection<Symbol> symbols = symbolList.values();
		return new ArrayList<Symbol>(symbols);
	}
	
	public static int getSymbolsSize()
	{
		return symbolList.size();
	}
}
