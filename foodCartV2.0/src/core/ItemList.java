package core;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.io.File;
import java.io.FileNotFoundException;
//import java.io.FileWriter;
//import java.io.IOException;
import java.util.Scanner;
/**
 * 
 */
//import java.util.Set;

/**
 * This class is used to record the menu items and read from the already provided text file
 * @author Anwar Kamil
 * @author Vimal
 *
 */
public class ItemList {
//	private HashMap<String, Item> ItemList;
	private Map<String, Item > ItemList ;
	private Map<String, String> Index;

	/**
	 * Constructor to create ItemList object / Menu
	 * @param itemList
	 */
	public ItemList(String file ) {
		String filename = file;
		ItemList  = readFile(filename );
		genIndex();
	}
	
	public ItemList( ) {
		ItemList  = new HashMap<String,Item>();
	}
	
	/**
	 * Method to search by Id the item record
	 * @param id
	 * @return Item
	 */
	public Item findByID(String id) {
		return ItemList.get(id); 	
	}

	/**
	 * Method to return Item from Name
	 */
	public Item findByName(String desc){
		return ItemList.get(Index.get(desc));
	}
/**
 * Method keeps an index for Key, Description
 * 	
 */
	private void genIndex(){
		Index = new HashMap<String, String>();
		for(Entry<String, Item> entry : ItemList.entrySet()){
			Index.put(entry.getValue().getDescription().toString(), entry.getKey().toString());
			
		}
	}
	
	/**
	 * Method to read menu text file.
	 * @param filename
	 * @return 
	 */
	private Map<String, Item> readFile(String filename) {
		
		Map<String, Item > ItemList = new HashMap<String, Item >();
		
		try {
			File f = new File(filename);
			Scanner scanner = new Scanner(f);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine(); 
				if (line.length() != 0) {
					try {
						String parts [] = line.split(",");
						String id = parts[0];
						String category = parts[2];
						String description = parts[1];
						Double cost = Double.parseDouble(parts[3].trim());
						Item item = new Item(id,category,description, cost);
						ItemList.put(id, item);
						
					
					}catch (NumberFormatException nfe) {
						String error = "Number conversion error in '" + line + "'  - " + nfe.getMessage();
						System.out.println(error);
					}
					//this catches null values
					catch (NullPointerException npe) {
						String error = "Null value in '" + line + "'  - " + npe.getMessage();
						System.out.println(error);
					}
					//this catches other errors
					catch (Exception e) {
						String error = "Error occured in '" + line + "'  - " + e.getMessage();
						System.out.println(error);
					}
					}
	

			}
			scanner.close();
		}
		//if the file is not found, stop with system exit
		catch (FileNotFoundException fnf){
		System.out.println( filename + " not found ");
		System.exit(0);
		}
		return ItemList;
	}
	
	/**
	 * This method returns the hashmap ItemList created
	 * @return ItemList
	 */
	public Map<String, String> getMenuItems(String Category){
		
		Map<String, String> menuItems = new HashMap<String, String>();
		
		for (Entry<String, Item> entry : ItemList.entrySet()) {
			if(Category.equals(entry.getValue().getCategory())){
					menuItems.put(entry.getKey(), entry.getValue().getDescription());
			}
		}
		return menuItems;
	}
	
	/**
	 * This method returns the hashmap Menu Categories loaded
	 * @return Map<String, String>
	 */
	public Map<String, String> getMenuCategories(){
		
		Map<String, String > Categories = new HashMap<String, String >();
		
		String KeyString;
		//construct menu categories from the Menu Map
		for (Entry<String, Item> entry : ItemList.entrySet()) {
		    if( !Categories.containsValue( entry.getValue().getDescription())){
		    	//2 Character Key for the Menu Category
		    	KeyString = entry.getKey(); KeyString = KeyString.substring(KeyString.length()-2);
		    	Categories.put(KeyString, entry.getValue().getCategory());
		    }
		}
		return Categories;
	}	
}
