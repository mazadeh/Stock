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

@WebServlet("/customer/remove_role")
public class RemoveRole extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Gson gson = new Gson();
		
		response.setContentType("text/html");
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		StockRepository repo = StockRepository.getRepository();
		
		boolean hasError = false;
		List<String> errMessages = new ArrayList<String>();
		
		String username = request.getParameter("username");
		String role = request.getParameter("role");
		
		if (username == null || username.equals(""))
		{
			errMessages.add("Username could not be empty");
			hasError = true;
		}
		if (role == null || role.equals(""))
		{
			errMessages.add("Role could not be empty");
			hasError = true;
		}
		
		if (!hasError)
		{
			try
			{
				role = repo.removeRoleFromCustomer(username, role);
				out.print(gson.toJson(role));
			}
			catch (SQLException ex)
			{
				System.err.println("Unable to connect to server : " + username);
				System.err.println(ex);
			}
		}
		if (hasError)
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
