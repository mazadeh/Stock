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
public class StockRepository
{
	private static Map<Integer, Customer> customerList;
	private static Customer admin;
	private static Customer admin2;
	
	static 
	{
		customerList = new HashMap<Integer, Customer>();
		admin = new Customer(1, "Admin", "Administrator");
		admin.setAdmin(true);
		customerList.put(1, admin);
		admin2 = new Customer(2, "Mohammad", "Azadeh");
		admin2.setAdmin(true);
		customerList.put(2, admin2);
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
}
