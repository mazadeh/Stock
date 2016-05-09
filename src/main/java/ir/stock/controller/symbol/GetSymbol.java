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

@WebServlet("/symbol/get")
public class GetSymbol extends HttpServlet
{
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Gson gson = new Gson();
		
		response.setContentType("text/html");
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		StockRepository repo = StockRepository.getRepository();
		
		String id = request.getParameter("id");
		
		if (id == null || id.equals(""))
		{
			List<Symbol> symbolList = null;
			try
			{
				symbolList = repo.getSymbolList();
			}
			catch (SQLException ex)
			{
				System.err.println("Unable to connect to server when getting symbols");
				System.err.println(ex);
			}
			out.print(gson.toJson(symbolList));
		}
		else
		{
			Symbol symbol = null;
			try
			{
				symbol = repo.getSymbol(Integer.parseInt(id));
			}
			catch (SQLException ex)
			{
				System.err.println("Unable to connect to server when getting symbol: " + id);
				System.err.println(ex);
			}
			out.print(gson.toJson(symbol));
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}
}
