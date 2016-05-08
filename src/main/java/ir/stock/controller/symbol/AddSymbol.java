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

@WebServlet("/symbol/add")
public class AddSymbol extends HttpServlet
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

		String symbolName = request.getParameter("name");
		
		if (symbolName == null || symbolName.equals(""))
		{
			errMessages.add("Name could not be empty");
			hasError = true;
		}

		if (!hasError)
		{
			Symbol symbol = null;
			try
			{
				symbol = repo.addSymbol(symbolName);
			}
			catch (SQLException ex)
			{
				System.err.println("Unable to connect to server when adding a new symbol: " + symbolName);
				System.err.println(ex);
			}
			out.print(gson.toJson(symbol));
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
