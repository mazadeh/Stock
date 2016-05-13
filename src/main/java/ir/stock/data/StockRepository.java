package ir.stock.data;

import java.util.*;
import java.sql.*;
import ir.stock.domain.*;
import java.text.SimpleDateFormat;

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
							 "sid integer not null, quantity integer not null, " +
							 "price int not null, type varchar(20), issell boolean not null, " +
							 "status varchar(20) not null, request_time datetime not null )" );
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
		
		if (tables.get("INCREASE_CASHE_REQUEST") == null)
		{
			st.executeUpdate("create table increase_cashe_request ( id integer IDENTITY PRIMARY KEY, cid integer not null, " +
							 "cashe integer not null, status varchar(20) )" );
			System.err.println("Increase Cashe Request table created successfully");
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
		customer.setSellList(getSellBuyRequestListByCustomer(customer.getId(), true));
		customer.setBuyList(getSellBuyRequestListByCustomer(customer.getId(), false));
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
			Symbol symbol = new Symbol(id, 
									   name,
									   getSellBuyRequestListBySymbol(id, true),
									   getSellBuyRequestListBySymbol(id, false) );
			list.add(symbol);
		}
		con.close();
		return list;
	}
	
	public Symbol getSymbol(int id) throws SQLException
	{
		Symbol symbol = null;
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select name from symbol where (id='" + id + "')");
		if (rs.next()) {
			symbol = new Symbol(id,
								rs.getString("name"),
								getSellBuyRequestListBySymbol(id, true),
								getSellBuyRequestListBySymbol(id, false) );
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
	
	public List<SellBuyRequest> getSellBuyRequestListBySymbol(int symbolId, boolean isSell) throws SQLException
	{
		List<SellBuyRequest> list = new ArrayList<SellBuyRequest>();
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from sell_buy_request where (sid='" +
										symbolId + "' and issell='" + isSell + "')" );
		while (rs.next()) {
			SellBuyRequest request;
			request = new SellBuyRequest(rs.getInt("id"),
										 rs.getInt("cid"),
										 rs.getInt("sid"),
										 rs.getInt("quantity"),
										 rs.getInt("price"),
										 rs.getString("type"),
										 rs.getBoolean("issell") );
			request.setStatus(rs.getString("status"));
			request.setTime(rs.getString("request_time"));
			list.add(request);
		}
		con.close();
		return list;
	}
	
	public List<SellBuyRequest> getSellBuyRequestListByCustomer(int customerId, boolean isSell) throws SQLException
	{
		List<SellBuyRequest> list = new ArrayList<SellBuyRequest>();
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from sell_buy_request where (cid='" +
										customerId + "' and issell='" + isSell + "')" );
		while (rs.next()) {
			SellBuyRequest request;
			request = new SellBuyRequest(rs.getInt("id"),
										 rs.getInt("cid"),
										 rs.getInt("sid"),
										 rs.getInt("quantity"),
										 rs.getInt("price"),
										 rs.getString("type"),
										 rs.getBoolean("issell") );
			request.setStatus(rs.getString("status"));
			request.setTime(rs.getString("request_time"));
			list.add(request);
		}
		con.close();
		return list;
	}
	
	public SellBuyRequest addSellBuyRequest(SellBuyRequest request) throws SQLException
	{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(now).toString();
        
        request.setTime(time);
        
		st.executeUpdate("insert into sell_buy_request (cid, sid, quantity, price, type, " +
						 "issell, status, request_time)  values (" +
						 "'" + request.getCustomerId() + "', " +
						 "'" + request.getSymbolId() + "', " +
						 "'" + request.getQuantity() + "', " +
						 "'" + request.getPrice() + "', " +
						 "'" + request.getType().getName() + "', " +
						 "'" + request.getIsSell() + "', " +
						 "'" + request.getStatus() + "', " +
						 "'" + time + "')");
		
		ResultSet rs = st.executeQuery("select max(id) as max_id from sell_buy_request");
		if (rs.next()) {
			request.setId(rs.getInt("max_id"));
		}
		con.close();
		
		request.getType().transaction(request);
		
		return request;
	}
	
	public IncreaseCasheRequest addIncreaseCasheRequest(IncreaseCasheRequest request) throws SQLException
	{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		
		request.setStatus("waiting");
		st.executeUpdate("insert into increase_cashe_request (cid, cashe, status)  values (" +
						 "'" + request.getCustomerId() + "', " +
						 "'" + request.getCashe() + "', " +
						 "'" + request.getStatus() + "')");
		
		ResultSet rs = st.executeQuery("select max(id) as max_id from increase_cashe_request");
		if (rs.next()) {
			request.setId(rs.getInt("max_id"));
		}
		con.close();
		
		return request;
	}
	
	public List<IncreaseCasheRequest> getIncreaseCasheRequestList() throws SQLException
	{
		List<IncreaseCasheRequest> list = new ArrayList<IncreaseCasheRequest>();
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from increase_cashe_request order by status, cid, cashe");
		while (rs.next()) {
			IncreaseCasheRequest request;
			request = new IncreaseCasheRequest(rs.getInt("id"),
											   rs.getInt("cid"),
											   rs.getInt("cashe"),
										 	   rs.getString("status") );
			list.add(request);
		}
		con.close();
		return list;
	}
	
	public Map<Integer, Integer> increaseCashe(String[] args) throws SQLException
	{
		Map<Integer, Integer> newDepositedAmounts = new HashMap<Integer, Integer>();
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		ResultSet rs;
		for (String arg : args)
		{
			rs = st.executeQuery("select * from increase_cashe_request where (id='" + arg + "')" );
			if (rs.next())
			{
				int cashe = rs.getInt("cashe");
				int cid = rs.getInt("cid");
				rs = st.executeQuery("select * from customer where (id='" + cid + "')" );
				if (rs.next())
				{
					int depositedAmount = rs.getInt("depositedAmount");
					depositedAmount += cashe;
					st.executeUpdate("update customer set depositedAmount='" + depositedAmount + "' " +
									 "where (id='" + cid + "')");
					st.executeUpdate("update increase_cashe_request set status='" + "done" + "' " +
									 "where (id='" + arg + "')");

					if (newDepositedAmounts.get(cid) == null)
					{
						newDepositedAmounts.put(cid, depositedAmount);
					}
					else
					{
						newDepositedAmounts.put(cid, depositedAmount + newDepositedAmounts.get(cid));
					}
				}
			}
		}
		return newDepositedAmounts;
	}
}
