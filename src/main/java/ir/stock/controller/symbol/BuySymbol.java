package ir.stock.controller.symbol;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.google.gson.Gson;

import ir.stock.data.*;
import ir.stock.domain.*;

@WebServlet("/symbol/buy")
public class BuySymbol extends HttpServlet
{
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Gson gson = new Gson();
		
		response.setContentType("text/html");
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		StockRepository repo = StockRepository.getRepository();
		
		boolean hasError = false;
		List<String> errMessages = new ArrayList<String>();

		String customerId = request.getParameter("customerId");
		String symbolId = request.getParameter("symbolId");
		String quantity = request.getParameter("quantity");
		String price = request.getParameter("price");
		String type = request.getParameter("type");
		
		if (customerId == null || customerId.equals(""))
		{
			errMessages.add("Customer ID could not be empty");
			hasError = true;
		}
		if (symbolId == null || symbolId.equals(""))
		{
			errMessages.add("Symbol ID could not be empty");
			hasError = true;
		}
		if (quantity == null || quantity.equals(""))
		{
			errMessages.add("Quantity could not be empty");
			hasError = true;
		}
		if (price == null || price.equals(""))
		{
			errMessages.add("Price could not be empty");
			hasError = true;
		}
		if (type == null || type.equals(""))
		{
			errMessages.add("Type could not be empty");
			hasError = true;
		}
		
		if (!hasError)
		{
			SellBuyRequest req = new SellBuyRequest(-1,
													Integer.parseInt(customerId), 
													Integer.parseInt(symbolId), 
													Integer.parseInt(quantity),
													Integer.parseInt(price), type,
													false );
			try
			{
				req = repo.addSellBuyRequest(req);
				out.print(gson.toJson(req));
			}
			catch (SQLException ex)
			{
				System.err.println("Unable to connect to server when buying : " + req);
				System.err.println(ex);
				out.print(gson.toJson(ex));
			}
			
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
