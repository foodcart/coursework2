package core;


import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

import org.junit.Test;

class ItemList_JUnit {

	Map<String, Item> il = new HashMap<String, Item>();
	
	public void Method()
	{
	
	Item i = new Item("AB25", "Beverage", "Iced Coffee", 2.0);
	il.put("abc", i);
	
	}
	
	@Test
	public void testFindByID() 
	{ 
		assertEquals("abc" , il.get("a")); 
	}

	@Test
	public void testGetMenuItems() 
	{ 
		Map<String, String> m = new HashMap<String, String>();	
		m.put("abc", "Iced Coffee");
		assertEquals( (ItemList)m ,((ItemList)il).getMenuItems("Beverage")); 
	}
	
	@Test
	public void testGetMenuCategories()
	{
		Map<String, String > c = new HashMap<String, String >();
		c.put("abc", "Beverage");
		assertEquals((ItemList)c, ((ItemList)il).getMenuCategories() );
		System.out.println((ItemList)il);
	}
	

}
