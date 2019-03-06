package core;


import static org.junit.Assert.*;

import org.junit.Test;

class Item_JUnit 

{

	Item testItem = new Item("AB25", "Beverage", "Coca-Cola", 2.0);
	
	@Test
	void testGetId() 
	{ 
		assertEquals("AB25", testItem.getId()); 
	}

	@Test
	void testGetCategory() 
	{ 
		assertEquals("Beverage", testItem.getCategory()); 
	}

	@Test
	void testGetDescription() 
	{ 
		assertEquals("Coca-Cola", testItem.getDescription()); 
	}

	@Test
	void testGetCost() 
	{ 
		assertEquals(2.0, testItem.getCost()); 
	}

}
