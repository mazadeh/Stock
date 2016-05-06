package ir.stock.controller.cusotmer;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.google.gson.Gson;

import ir.stock.domain.*;

@WebServlet("/customer/add")
public class AddCustomer extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Gson gson = new Gson();
		Customer customer;
		
		boolean hasError = false;
		List<String> errMessages = new ArrayList<String>();
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String id = request.getParameter("id");
		String password = request.getParameter("password");
		String firstName = request.getParameter("firstname");
		String lastName = request.getParameter("lastname");
		
		
		if (id == null || id.equals(""))
		{
			errMessages.add("ID could not be empty");
			hasError = true;
		}
		if (password == null || password.equals(""))
		{
			errMessages.add("Password could not be empty");
			hasError = true;
		}
		if (firstName == null || firstName.equals(""))
		{
			errMessages.add("Firstname could not be empty.");
			hasError = true;
		}
		if (lastName == null || lastName.equals(""))
		{
			errMessages.add("Lastname could not be empty.");
			hasError = true;
		}
		
		if (!hasError)
		{
			int newId = Integer.parseInt(id);
			
			if (StockRepository.getCustomer(newId) != null)
				newId = StockRepository.getCustomerSize() + 1;
			customer = new Customer(newId, password, firstName, lastName);
			StockRepository.addCustomer(customer);
			out.print(gson.toJson(customer));
		}
		else
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
