package ir.stock.controller.cusotmer;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.google.gson.Gson;

import ir.stock.data.*;
import ir.stock.domain.*;

@WebServlet("/customer/add")
public class AddCustomer extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Gson gson = new Gson();
		Customer customer = null;
		
		response.setContentType("text/html");
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		StockRepository repo = StockRepository.getRepository();
		
		boolean hasError = false;
		List<String> errMessages = new ArrayList<String>();
		
		customer = new Customer(-1,
			request.getParameter("firstname"), 
			request.getParameter("lastname"), 
			request.getParameter("username"), 
			request.getParameter("password"), 0 );
		
		
		if (customer.getFirstname() == null || customer.getFirstname().equals(""))
		{
			errMessages.add("Firstname could not be empty");
			hasError = true;
		}
		if (customer.getLastname() == null || customer.getLastname().equals(""))
		{
			errMessages.add("Lastname could not be empty");
			hasError = true;
		}
		if (customer.getUsername() == null || customer.getUsername().equals(""))
		{
			errMessages.add("Username could not be empty");
			hasError = true;
		}
		if (customer.getPassword() == null || customer.getPassword().equals(""))
		{
			errMessages.add("Password could not be empty");
			hasError = true;
		}
		
		int newusername = 0;
		if (!hasError)
		{
			try
			{
				customer = repo.addCustomer(customer);
			}
			catch (SQLException ex)
			{
				System.err.println("Unable to connect to server when adding a new customer: " + customer.getUsername());
				System.err.println(ex);
			}
			if (customer.getId() < 0)
			{
				errMessages.add("Username is repetetive");
				hasError = true;
			}
			else
				out.print(gson.toJson(customer));
		}
		if (hasError)
		{
			request.setAttribute("errors", errMessages);
			out.print(gson.toJson(errMessages));
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}
}
