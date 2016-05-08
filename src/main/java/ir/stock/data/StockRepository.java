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
			Symbol symbol = new Symbol(id, name);
			list.add(symbol);
		}
		con.close();
		return list;
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
}
