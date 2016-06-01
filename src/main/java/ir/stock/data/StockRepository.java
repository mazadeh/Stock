package ir.stock.data;

import java.util.*;
import java.sql.*;
import ir.stock.domain.*;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;

public class StockRepository
{
	private final static Logger LOGGER = LOGUtil.getLogger(StockRepository.class); 
	
	// Singleton Design Pattern
	private static StockRepository theRepository;
	
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
			theRepository.createTables();
		}
		catch (SQLException ex)
		{
			LOGGER.severe("Unable to create tables");
			LOGGER.fine(ex.toString());
		}
	}
	
	public static StockRepository getRepository()
	{
		return theRepository;
	}
	
	public void createTables() throws SQLException
	{
		Connection con = JDBCUtil.getConnection();
		Statement st = con.createStatement();
		ResultSet rs;
		rs = st.executeQuery("select table_name from INFORMATION_SCHEMA.TABLES where table_schema='PUBLIC'");
		while (rs.next()) 
		{
			tables.put(rs.getString("table_name"), true);
		}
		
		LOGGER.info("Loaded Tables:" + tables.toString());
		
		if (tables.get("SYMBOL") == null)
		{
			st.executeUpdate("create table symbol ( id INTEGER IDENTITY PRIMARY KEY, name varchar(20) not null, owner integer not null )");
			LOGGER.info("Symbol table created successfully");
		}
		
		if (tables.get("SELL_BUY_REQUEST") == null)
		{
			st.executeUpdate("create table sell_buy_request ( id integer IDENTITY PRIMARY KEY, cid integer not null, " +
							 "sid integer not null, quantity integer not null, " +
							 "price int not null, type varchar(20), issell boolean not null, " +
							 "status varchar(20) not null, request_time datetime not null )" );
			LOGGER.info("Sell-Buy-Request table created successfully");
		}
		
		if (tables.get("USERS") == null)
		{
			st.executeUpdate("create table users ( id integer IDENTITY PRIMARY KEY, firstname varchar(20) not null, " +
							 "lastname varchar(20) not null, username varchar(20) not null unique, " +
							 "password varchar(20) not null, depositedAmount integer default '0' )" );
			LOGGER.info("Users table created successfully");
			
			st.executeUpdate("insert into users (firstname, lastname, username, password)  values ( " +
							 "'admin', 'administrator', 'admin', 'password' )" );
			LOGGER.info("Admin user added");
			
			st.executeUpdate("insert into users (firstname, lastname, username, password)  values ( " +
							 "'user', 'user', 'user', 'user' )" );
			LOGGER.info("User user added");
			
			st.executeUpdate("insert into users (firstname, lastname, username, password)  values ( " +
							 "'owner', 'owner', 'owner', 'owner' )" );
			LOGGER.info("Owner user added");
			
			st.executeUpdate("insert into users (firstname, lastname, username, password)  values ( " +
							 "'manager', 'manager', 'manager', 'manager' )" );
			LOGGER.info("Financial Manager user added");
		}
		
		if (tables.get("USER_ROLES") == null)
		{
			st.executeUpdate("create table user_roles ( id integer IDENTITY PRIMARY KEY, username varchar(20) not null, " +
							 "user_role varchar(20) default 'user' )" );
			LOGGER.info("User Roles table created successfully");
			
			st.executeUpdate("insert into user_roles (username)  values ('admin')" );
			LOGGER.info("Role of Admin is set as 'user'");
			st.executeUpdate("insert into user_roles (username, user_role)  values ( 'admin', 'admin' )" );
			LOGGER.info("Role of Admin is set as 'admin'");
			st.executeUpdate("insert into user_roles (username, user_role)  values ( 'admin', 'finance manager' )" );
			LOGGER.info("Role of Admin is set as 'finance manager'");
			st.executeUpdate("insert into user_roles (username, user_role)  values ( 'admin', 'owner' )" );
			LOGGER.info("Role of Admin is set as 'owner'");
			
			
			st.executeUpdate("insert into user_roles (username, user_role)  values ( 'user', 'user' )" );
			LOGGER.info("Role of User is set as 'admin'");
			st.executeUpdate("insert into user_roles (username, user_role)  values ( 'manager', 'finance manager' )" );
			LOGGER.info("Role of Financial Manager is set as 'finance manager'");
			st.executeUpdate("insert into user_roles (username, user_role)  values ( 'owner', 'owner' )" );
			LOGGER.info("Role of Owener is set as 'owner'");
		}
		
		if (tables.get("INCREASE_CASHE_REQUEST") == null)
		{
			st.executeUpdate("create table increase_cashe_request ( id integer IDENTITY PRIMARY KEY, cid integer not null, " +
							 "cashe integer not null, status varchar(20) )" );
			LOGGER.info("Increase Cashe Request table created successfully");
		}
		
		con.close();
	}
	
	public List<Customer> getCustomerList() throws SQLException
	{
		List<Customer> list = new ArrayList();
		Connection con = JDBCUtil.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from users");
		while (rs.next()) {
			Customer customer = new Customer(
				rs.getInt("id"),
				rs.getString("firstname"),
				rs.getString("lastname"),
				rs.getString("username"),
				rs.getString("password"),
				rs.getInt("depositedAmount") );
			list.add(customer);		
			customer.setRoles(getCustomerRoles(customer.getUsername()));
		}
		con.close();
		return list;
	}
	
	public Customer getCustomer(String username) throws SQLException
	{
		Customer customer = null;
		Connection con = JDBCUtil.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from users where (username='" + username + "')");

		if (rs.next()) {
			customer = new Customer(
						rs.getInt("id"),
						rs.getString("firstname"),
						rs.getString("lastname"),
						rs.getString("username"),
						null, //rs.getString("password"),
						rs.getInt("depositedAmount") );
		}
		con.close();
		//customer.setSellList(getSellBuyRequestListByCustomer(customer.getId(), true));
		//customer.setBuyList(getSellBuyRequestListByCustomer(customer.getId(), false));
		customer.setRoles(getCustomerRoles(customer.getUsername()));
		return customer;
	}
	
	public Customer getCustomer(String username, String password) throws SQLException
	{
		Customer customer = null;
		Connection con = JDBCUtil.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from users where ( " +
									   "username='" + username + "' and " +
									   "password='" + password + "' )");

		if (rs.next()) {
			customer = new Customer(
						rs.getInt("id"),
						rs.getString("firstname"),
						rs.getString("lastname"),
						rs.getString("username"),
						null, //rs.getString("password"),
						rs.getInt("depositedAmount") );
			customer.setSellList(getSellBuyRequestListByCustomer(customer.getId(), true));
			customer.setBuyList(getSellBuyRequestListByCustomer(customer.getId(), false));
			customer.setRoles(getCustomerRoles(customer.getUsername()));
			
			if (customer.isOwner)
			{
				rs= st.executeQuery("select * from symbol where ( owner='" + rs.getInt("id") + "' )");
				while (rs.next()) {
					customer.addOwnedSymbol(rs.getInt("id"));
				}
			}
		}
		con.close();
		
		return customer;
	}
	
	public String addRoleToCustomer(String username, String role) throws SQLException
	{
		Connection con = JDBCUtil.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from users where (username='" + username + "')");

		if (rs.next()) {
			st.executeUpdate("insert into user_roles (username, user_role)  values ( '" + username + "', '" + role + "' )" );
			LOGGER.info("Role of " + username + " is set as '" + role + "'");
		}
		con.close();
		
		return role;
	}
	
	public String removeRoleFromCustomer(String username, String role) throws SQLException
	{
		Connection con = JDBCUtil.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("delete from user_roles where (username='" + username + "' and user_role='" + role + "' )" );
		LOGGER.info("The role '" + role + "' of " + username + " is removed" );
		
		return role;
	}
	
	public List<String> getCustomerRoles(String username) throws SQLException
	{
		List<String> roles = new ArrayList<String>();
		Connection con = JDBCUtil.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from user_roles where (username='" + username + "') order by user_role");

		while (rs.next()) {
			roles.add(rs.getString("user_role"));
		}
		con.close();
		return roles;
	}
	
	public Customer addCustomer(Customer customer) throws SQLException
	{
		Connection con = JDBCUtil.getConnection();
		Statement st = con.createStatement();
		st.executeUpdate("insert into users (firstname, lastname, username, password, depositedAmount) values ( " + 
						 "'" + customer.getFirstname() + "', " + 
						 "'" + customer.getLastname() + "', " + 
						 "'" + customer.getUsername() + "', " + 
						 "'" + customer.getPassword() + "', " + 
						 "'" + customer.getDepositedAmount() + "' )");
		ResultSet rs = st.executeQuery("select id from users where (username='" + customer.getUsername() + "')");
		if (rs.next()) {
			customer.setId(rs.getInt("id"));
			st.executeUpdate("insert into user_roles (username) values ( '" + customer.getUsername() + "' )");
			customer.addRole("user");
		}
		con.close();
		return customer;
	}
	
	public List<Symbol> getSymbolList() throws SQLException
	{
		List<Symbol> list = new ArrayList();
		Connection con = JDBCUtil.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from symbol order by name");
		while (rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			int oid = rs.getInt("owner");
			Symbol symbol = new Symbol(id, 
									   name,
									   oid,
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
		Connection con = JDBCUtil.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from symbol where (id='" + id + "')");
		if (rs.next()) {
			symbol = new Symbol(id,
								rs.getString("name"),
								rs.getInt("owner"),
								getSellBuyRequestListBySymbol(id, true),
								getSellBuyRequestListBySymbol(id, false) );
		}
		con.close();
		return symbol;
	}
	
	public Symbol addSymbol(String name, String owner) throws SQLException
	{
		Connection con = JDBCUtil.getConnection();
		Statement st = con.createStatement();
		st.executeUpdate("insert into symbol (name, owner) values ('" + name + "', '" + owner + "')");
		ResultSet rs = st.executeQuery("select * from symbol where (name='" + name + "')");
		int id = -1;
		int oid = -1;
		if (rs.next()) {
			id = rs.getInt("id");
			oid = rs.getInt("owner");
		}
		con.close();
		return new Symbol(id, name, oid);
	}
	
	public List<SellBuyRequest> getSellBuyRequestListBySymbol(int symbolId, boolean isSell) throws SQLException
	{
		List<SellBuyRequest> list = new ArrayList<SellBuyRequest>();
		Connection con = JDBCUtil.getConnection();
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
		Connection con = JDBCUtil.getConnection();
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
		Connection con = JDBCUtil.getConnection();
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
		Connection con = JDBCUtil.getConnection();
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
		Connection con = JDBCUtil.getConnection();
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
		Connection con = JDBCUtil.getConnection();
		Statement st = con.createStatement();
		ResultSet rs;
		for (String arg : args)
		{
			rs = st.executeQuery("select * from increase_cashe_request where (id='" + arg + "')" );
			if (rs.next())
			{
				int cashe = rs.getInt("cashe");
				int cid = rs.getInt("cid");
				rs = st.executeQuery("select * from users where (id='" + cid + "')" );
				if (rs.next())
				{
					int depositedAmount = rs.getInt("depositedAmount");
					depositedAmount += cashe;
					st.executeUpdate("update users set depositedAmount='" + depositedAmount + "' " +
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
