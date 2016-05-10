package ir.stock.domain;

public class IncreaseCasheRequest
{
	private int id;
	private int customerId;
	private int cashe;
	private String status;
	
	public IncreaseCasheRequest(int customerId, int cashe)
	{
		this.customerId = customerId;
		this.cashe = cashe;
	}
	
	public IncreaseCasheRequest(int id, int customerId, int cashe, String status)
	{
		this.id = id;
		this.customerId = customerId;
		this.cashe = cashe;
		this.status = status;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	public int getId()
	{
		return id;
	}
	public void setCustomerId(int customerId)
	{
		this.customerId = customerId;
	}
	public int getCustomerId()
	{
		return customerId;
	}
	public void setCashe(int cashe)
	{
		this.cashe = cashe;
	}
	public int getCashe()
	{
		return cashe;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public String getStatus()
	{
		return status;
	}
}
