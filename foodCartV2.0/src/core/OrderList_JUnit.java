package core;


import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

import org.junit.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

class OrderList_JUnit 
{
	
	public void method2() throws ParseException, InvalidQuantityException
	
	{
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.SSS");
	String s = "2019-01-01 11:11:11";
	Date d = sdf.parse(s.trim());
	Timestamp ts = new Timestamp(d.getTime());	
	TreeMap<Integer, Order> tm1 = new TreeMap<Integer, Order>();
	tm1.put(1, new Order(1, 5, "Bread", 2, ts, 0.2, 8));
	
	}
	

	@Test
	void testGetSize() 
	{
		
	}

	@Test
	void testReadFile() {
		fail("Not yet implemented");
	}

}
