package ir.stock.domain;

import java.util.*;
import java.io.*;

public abstract class Type
{	
	public abstract int sell(SellBuyRequest sellRequest);
	public abstract int buy(SellBuyRequest buyRequest);
	public abstract void transaction(SellBuyRequest request);
}
