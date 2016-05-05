package ir.stock.controller.cusotmer;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.google.gson.Gson;

import ir.stock.domain.*;

@WebServlet("/customer/get")
public class GetCustomer extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Gson gson = new Gson();
		Customer customer;
		
		response.setContentType("text/html");
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();

		String id = request.getParameter("id");
		
		if (id == null || id.equals(""))
		{
			Map<Integer, Customer> customerList = StockRepository.getCustomerList();
			out.print(gson.toJson(customerList));
		}
		else
		{
			int newId = Integer.parseInt(id);
			customer = StockRepository.getCustomer(newId);
			out.print(gson.toJson(customer));
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}
}
