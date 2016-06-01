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

@WebServlet("/customer/add_increase_request")
public class AddIncreaseCashe extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Gson gson = new Gson();
		
		response.setContentType("text/html");
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		StockRepository repo = StockRepository.getRepository();
		
		boolean hasError = false;
		List<String> errMessages = new ArrayList<String>();

		String money = request.getParameter("cashe");
		String customerId = request.getParameter("customerId");
		
		
		if (money == null || money.equals(""))
		{
			errMessages.add("Money could not be empty");
			hasError = true;
		}
		if (customerId == null || customerId.equals(""))
		{
			errMessages.add("Customer ID could not be empty");
			hasError = true;
		}
		
		if (!hasError)
		{
			int cid;
			int cashe;
			try
			{
				cid = Integer.parseInt(customerId);
				cashe = Integer.parseInt(money);
				
				IncreaseCasheRequest req = new IncreaseCasheRequest(cid, cashe);
				req = repo.addIncreaseCasheRequest(req);
				out.print(gson.toJson(req));
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
