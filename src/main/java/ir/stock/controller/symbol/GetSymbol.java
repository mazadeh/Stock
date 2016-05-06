package ir.stock.controller.symbol;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.google.gson.Gson;

import ir.stock.domain.*;

@WebServlet("/symbol/get")
public class GetSymbol extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Gson gson = new Gson();
		Customer customer;
		
		response.setContentType("text/html");
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		List<Symbol> symbolList = ArghyStockRepository.getSymbolList();
		out.print(gson.toJson(symbolList));

		
		
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}
}
