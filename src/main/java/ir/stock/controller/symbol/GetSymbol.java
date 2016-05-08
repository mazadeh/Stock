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
		
		List<Symbol> symbolList = null;
		try
		{
			symbolList = repo.getSymbolList();
		}
		catch (SQLException ex)
		{
			System.err.println("Unable to connect to server when getting symbols");
		}
		finally
		{
			out.print(gson.toJson(symbolList));
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}
}
