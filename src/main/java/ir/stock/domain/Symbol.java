package ir.stock.domain;

public class Symbol
{
	private int id;
	private String name;
	
	public Symbol( int id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	public int getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
}
