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

@WebServlet("/customer/increase_cashe")
public class IncreaseCashe extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Gson gson = new Gson();
		
		response.setContentType("text/html");
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		StockRepository repo = StockRepository.getRepository();
		
		boolean hasError = false;
		List<String> errMessages = new ArrayList<String>();

		String[] list = request.getParameterValues("list");
		
		if (list == null || list.equals(""))
		{
			errMessages.add("List could not be empty");
			hasError = true;
		}
		
		if (!hasError)
		{
			try
			{
				Map<Integer, Integer> rspns = repo.increaseCashe(list);
				out.print(gson.toJson(rspns));
			}
			catch (Exception ex)
			{
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
