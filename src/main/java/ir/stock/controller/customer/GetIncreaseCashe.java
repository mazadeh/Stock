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

@WebServlet("/customer/get_increase_request")
public class GetIncreaseCashe extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Gson gson = new Gson();
		
		response.setContentType("text/html");
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		StockRepository repo = StockRepository.getRepository();
		
		try
		{
			out.print(gson.toJson(repo.getIncreaseCasheRequestList()));
		}
		catch (Exception ex)
		{
			out.print(gson.toJson(ex));
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}
}
