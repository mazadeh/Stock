package ir.stock.data;

import java.util.*;
import java.sql.*;
import ir.stock.domain.*;

public class StockRepository
{
	// Singleton Design Pattern
	private static StockRepository theRepository;
	public static final String CONN_STR = "jdbc:hsqldb:hsql://localhost";
	
	private Map<String, Boolean> tables;

	private StockRepository()
	{
		tables = new HashMap<String, Boolean>();
	}

	static
	{
		theRepository = new StockRepository();
		
		try
		{
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
			System.err.println("HSQLDB JDBC driver loaded");
		}
		catch (ClassNotFoundException ex)
		{
			System.err.println("Unable to load HSQLDB JDBC driver");
		}
		try
		{
			theRepository.createTables();
		}
		catch (SQLException ex)
		{
			System.err.println("Unable to create tables");
			System.err.println(ex);
		}
	}
	
	public static StockRepository getRepository()
	{
		return theRepository;
	}
	
	public void createTables() throws SQLException
	{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		ResultSet rs;
		rs = st.executeQuery("select table_name from INFORMATION_SCHEMA.TABLES where table_schema='PUBLIC'");
		while (rs.next()) 
		{
			tables.put(rs.getString("table_name"), true);
		}
		
		System.err.println("Loaded Tables:");
		System.err.println(tables);
		
		if (tables.get("SYMBOL") == null)
		{
			st.executeUpdate("create table symbol ( id INTEGER IDENTITY PRIMARY KEY, name varchar(20) not null )");
			System.err.println("Symbol table created successfully");
		}
		
		if (tables.get("SELL_BUY_REQUEST") == null)
		{
			st.executeUpdate("create table sell_buy_request ( id integer IDENTITY PRIMARY KEY, cid integer not null, " +
							 "sname varchar(20) not null, quantity integer not null, " +
							 "price int not null, type varchar(20), issell boolean not null )" );
			System.err.println("Sell-Buy-Request table created successfully");
		}
		
		if (tables.get("CUSTOMER") == null)
		{
			st.executeUpdate("create table customer ( id integer IDENTITY PRIMARY KEY, firstname varchar(20) not null, " +
							 "lastname varchar(20) not null, username varchar(20) not null unique, " +
							 "password varchar(20) not null, depositedAmount integer default '0' )" );
			System.err.println("Customer table created successfully");
			
			st.executeUpdate("insert into customer (firstname, lastname, username, password)  values ( " +
							 "'admin', 'administrator', 'admin', 'password' )" );
			System.err.println("Admin user added");
		}
		
		con.close();
	}
	
	public List<Customer> getCustomerList() throws SQLException
	{
		List<Customer> list = new ArrayList();
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from customer");
		while (rs.next()) {
			Customer customer = new Customer(
				rs.getInt("id"),
				rs.getString("firstname"),
				rs.getString("lastname"),
				rs.getString("username"),
				rs.getString("password"),
				rs.getInt("depositedAmount") );
			list.add(customer);
		}
		con.close();
		return list;
	}
	
	public Customer getCustomer(String username) throws SQLException
	{
		Customer customer = null;
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from customer where (username='" + username + "')");

		if (rs.next()) {
			customer = new Customer(
						rs.getInt("id"),
						rs.getString("firstname"),
						rs.getString("lastname"),
						rs.getString("username"),
						rs.getString("password"),
						rs.getInt("depositedAmount") );
		}
		con.close();
		return customer;
	}
	
	public Customer addCustomer(Customer customer) throws SQLException
	{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		st.executeUpdate("insert into customer (firstname, lastname, username, password, depositedAmount) values ( " + 
						 "'" + customer.getFirstname() + "', " + 
						 "'" + customer.getLastname() + "', " + 
						 "'" + customer.getUsername() + "', " + 
						 "'" + customer.getPassword() + "', " + 
						 "'" + customer.getDepositedAmount() + "' )");
		ResultSet rs = st.executeQuery("select id from customer where (username='" + customer.getUsername() + "')");
		if (rs.next()) {
			customer.setId(rs.getInt("id"));
		}
		con.close();
		return customer;
	}
	
	public List<Symbol> getSymbolList() throws SQLException
	{
		List<Symbol> list = new ArrayList();
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from symbol");
		while (rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			Symbol symbol = new Symbol(id, name,
					getSellBuyRequestList(name, true),
					getSellBuyRequestList(name, false) );
			list.add(symbol);
		}
		con.close();
		return list;
	}
	
	public Symbol getSymbol(String name) throws SQLException
	{
		Symbol symbol = null;
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select id from symbol where (name='" + name + "')");
		if (rs.next()) {
			symbol = new Symbol(
					rs.getInt("id"),
					name,
					getSellBuyRequestList(name, true),
					getSellBuyRequestList(name, false) );
		}
		con.close();
		return symbol;
	}
	
	public Symbol addSymbol(String name) throws SQLException
	{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		st.executeUpdate("insert into symbol (name) values ('" + name + "')");
		ResultSet rs = st.executeQuery("select id from symbol where (name='" + name + "')");
		int id = -1;
		if (rs.next()) {
			id = rs.getInt("id");
		}
		con.close();
		return new Symbol(id, name);
	}
	
	public List<SellBuyRequest> getSellBuyRequestList(String symbolName, boolean isSell) throws SQLException
	{
		List<SellBuyRequest> list = new ArrayList<SellBuyRequest>();
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from sell_buy_request where (sname='" +
										symbolName + "' and issell='" + isSell + "')" );
		while (rs.next()) {
			SellBuyRequest request;
			request = new SellBuyRequest(rs.getInt("id"),
										 rs.getInt("cid"),
										 rs.getString("sname"),
										 rs.getInt("quantity"),
										 rs.getInt("price"),
										 rs.getString("type"),
										 rs.getBoolean("issell") );
			list.add(request);
		}
		con.close();
		return list;
	}
	
	public SellBuyRequest addSellBuyRequest(SellBuyRequest request) throws SQLException
	{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		st.executeUpdate("insert into sell_buy_request (cid, sname, quantity, price, type, issell)  values (" +
						 "'" + request.getCustomerId() + "', " +
						 "'" + request.getSymbolName() + "', " +
						 "'" + request.getQuantity() + "', " +
						 "'" + request.getPrice() + "', " +
						 "'" + request.getType() + "', " +
						 "'" + request.getIsSell() + "')");
		ResultSet rs = st.executeQuery("select max(id) as max_id from sell_buy_request");
		if (rs.next()) {
			request.setId(rs.getInt("max_id"));
		}
		con.close();
		return request;
	}
}
