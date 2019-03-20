package junits;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.jupiter.api.Test;

import controller.PointOfSales;
import model.InvalidQuantityException;
import model.Order;
import model.OrderQueue;
import view.QueueStatus;
import view.NorthPanel;

public class JUnitTest_Pos2_PointOfSales 
{

	/**
	 * JUnit Test Case
	 * @author Vishnu
	 */
	
	@Test
	public void test() 
	{
		DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		try { date = dateformat.parse("21/03/2019"); } 
		catch (ParseException e) { e.printStackTrace(); }
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		try 
		{
			Order o = new Order(50, 2, "Bread", 4, ts, 0.2, 20);
			Collection<Order> arr = new ArrayList<Order>();
			arr.add(o);
			QueueStatus qs = new QueueStatus(10);
			OrderQueue oq = new OrderQueue(qs);
			NorthPanel np = new NorthPanel();
			PointOfSales pos = new PointOfSales(oq, arr, np);
			assertEquals(pos.getOrders(), arr);
		} 
		catch (InvalidQuantityException e) { e.printStackTrace(); }
	}

}