package model;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;
import java.text.*;
import java.time.format.DateTimeParseException;

public class OrderList {
/**
 * OrdeList is the TreeMap of Orders. 
 * This class provides utilities to initialize/access/update the OrderList
 * @author Gabi
 * @author Vimal
 */
	private TreeMap<Integer,Order> OrderList;
	private String FileName;
	private MessageStore excpMessage = null;

/**
 * This is the constructor that reads the given file to populate the Order TreeMap	
 * @param file
 */
	public OrderList(String file) {

		FileName = file;//new String("../foodCart/core/orderlist.db");
		OrderList = new TreeMap<Integer,Order>();
		excpMessage = readFile(FileName);
	}
	
	public OrderList() {
		OrderList = new TreeMap<Integer,Order>();
	}
	
	public MessageStore getInitExcp(){
		return excpMessage;
	}

/**
 *  this method adds a given order to the map
 * @param 
 */
	private void add(Order o){
		OrderList.put(o.getID(),o);
	}
/**
 *  This method adds a new Order to the OrderList 
 * @param customer
 * @param item
 * @param quantity
 * @param discount
 * @param total
 * @return
 */
	public Object[] add(int customer, String item, int quantity, double discount, double total) {
		Object obj[] = new Object[2];
		obj[0] = null; obj[1] = null;
		int id = 0;
		
		try{
		id = OrderList.lastKey(); //increment order id
		}catch (NoSuchElementException e) {
			// do nothing here, as id will be zero
		}
		id ++;
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Order newOrder;
		try {
			newOrder = new Order(id, customer, item, quantity, time, discount, total);
			//append to OrderList
			add(newOrder);
			obj[0] = newOrder;
			// append to the orders.db file
			obj[1] = appendFile(newOrder);
		} catch (InvalidQuantityException e) {
			obj[1] = new MessageStore(e);
		}
		catch(Exception e){
			obj[1] = new MessageStore(e);
		}
		return obj;
	}

	
/**
 * This method gets the Customer Number in the Last Order.
 * @return
 */
   public int getLastCustomer(){
	   try {
	   return OrderList.lastEntry().getValue().getCustomer();
	   } catch (Exception e) {
	return 0;
	}
   }
/**
 * Get the number of items in Order		
 * @return
 */
	public int getSize() {
		return OrderList.size();
	}
/**
 * This Method takes the file name as input and reads the file to create 
 * the Orders Tree. 	
 * @param filename
 * @return MessageStore - exception, if any
 */
	private MessageStore readFile(String filename) {
		//local variables
		File f;
		Scanner scanner = null;
		//try reading the file
		try {
			f = new File(filename);
			scanner = new Scanner(f);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine(); 
				if (line.length() != 0) {
					try {
						String [] parts = line.split(",");
						int id = Integer.parseInt(parts[0].trim());
						int cust = Integer.parseInt(parts[1].trim());
						String item = parts[2].trim();
						int qty = Integer.parseInt(parts[3].trim());
						
						double disc = Double.parseDouble(parts[4].trim());
						double total = Double.parseDouble(parts[5].trim());
						
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date d = sdf.parse(parts[6].trim());
						Timestamp ts = new Timestamp(d.getTime());
						
						Order o = new Order(id, cust, item, qty, ts, disc, total);
						this.add(o);	
					}
					catch (NumberFormatException nfe) {
						return new MessageStore(nfe, "Number conversion error in '" + line + "'  - " + nfe.getMessage());
						
					}
					catch (DateTimeParseException dtpe) {
						return new MessageStore(dtpe, "DateTime conversion error in '" + line + "'  - " + dtpe.getMessage());
					}
					catch (ArrayIndexOutOfBoundsException air) {
						return new MessageStore(air, "Not enough items in  : '" + line + "' index position : " + air.getMessage());
					}
					catch (NullPointerException npe) {
						return new MessageStore( npe, "Null value in '" + line + "'  - " + npe.getMessage() );
					}
					catch (Exception e) {
						return new MessageStore(e, "Error occured in '" + line + "'  - " + e.getMessage());
					}
				}
			}
			return null;
		}
		catch (FileNotFoundException fnf){
			 return new MessageStore(fnf, filename + " not found ");
		 }
		finally {
			scanner.close();
		}
		
	}
/**
 * Returns the Collection of Orders and exception(if any)	
 * @return
 */
	public Collection<Order> getOrderItems( ){

		return OrderList.values();
	}
	
	public Object[] getOrderItems( int Customer ){
		Object obj[] = new Object[2];
		Map<Integer,Order> OrderItems = new TreeMap<Integer,Order>(); 
		try{
		for (Entry<Integer, Order> entry : OrderList.entrySet()) {
			if( Customer == entry.getValue().getCustomer()){
				OrderItems.put(entry.getKey(), entry.getValue());
			}
		}}catch (Exception e) {
			obj[1] = new MessageStore(e);
		}
		obj[0] = OrderItems;
		return obj;
	}
	
	private MessageStore appendFile(Order o){
		
		MessageStore ms = null;
		
		String parts[] = new String[7];
		parts[0] = Integer.toString(o.getID());//id
		parts[1] = Integer.toString(o.getCustomer());//customer
		parts[2] = o.getItem();
		parts[3] = Integer.toString(o.getQuantity());
		parts[4] = Double.toString(o.getDiscount());
		parts[5] = Double.toString(o.getTotal());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date(); d.setTime(o.getTime().getTime());
		parts[6] = sdf.format( d );
		 
		
		String output = System.lineSeparator() + String.join(",", parts);
		
		//try appending to orderlist
		try {
			BufferedWriter fileWriter =  new BufferedWriter(new FileWriter( FileName, true ));
			fileWriter.write(output);
			fileWriter.close();
		} catch (IOException e) {
			ms = new MessageStore(e);
		}
		
		return ms;
	}
}

