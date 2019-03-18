package model;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;
import java.text.*;
import java.time.format.DateTimeParseException;

public class OrderList {
	/**
	 * OrdeList is the TreeMap of Orders. This class provides utilities to
	 * initialize/access/update the OrderList
	 * 
	 * @author Gabi
	 * @author Vimal
	 */
	private TreeMap<Integer, Order> OrderList;
	private String FileName;

	/**
	 * This is the constructor that reads the given file to populate the Order
	 * TreeMap
	 * 
	 * @param file
	 */
	public OrderList(String file) throws Exception {

		FileName = file;// new String("../foodCart/core/orderlist.db");
		OrderList = new TreeMap<Integer, Order>();
		try {
			readFile(FileName);
		} catch (Exception e) {
			throw e;
		}
	}

	public OrderList() {
		OrderList = new TreeMap<Integer, Order>();
	}

	/**
	 * this method adds a given order to the map
	 * 
	 * @param
	 */
	private void add(Order o) {
		OrderList.put(o.getID(), o);
	}

	/**
	 * This method adds a new Order to the OrderList
	 * 
	 * @param customer
	 * @param item
	 * @param quantity
	 * @param discount
	 * @param total
	 * @return
	 */
	
	 
	public Order add(int customer, String item, int quantity, double discount, double total)
			throws InvalidQuantityException, NoSuchElementException, IOException {
		Order newOrder = null;
		int id = 0;
		try {
			id = OrderList.lastKey(); // increment order id
		} catch (NoSuchElementException e) {
			throw e;
		}
		id++;
		Timestamp time = new Timestamp(System.currentTimeMillis());
		try {
			newOrder = new Order(id, customer, item, quantity, time, discount, total);
			// append to OrderList
			add(newOrder);
			// append to the orders.db file
			try {
				appendFile(newOrder);
			} catch (IOException e) {
				throw e;
			}

		} catch (InvalidQuantityException e) {
			throw e;
		}
		return newOrder;
	}

	/**
	 * This method gets the Customer Number in the Last Order.
	 * 
	 * @return
	 */
	public int getLastCustomer() {
		try {
			return OrderList.lastEntry().getValue().getCustomer();
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Get the number of items in Order
	 * 
	 * @return
	 */
	public int getSize() {
		return OrderList.size();
	}

	/**
	 * This Method takes the file name as input and reads the file to create the
	 * Orders Tree.
	 * 
	 * @param filename
	 * @return
	 * @return MessageStore - exception, if any
	 */
	private void readFile(String filename) throws Exception {
		// local variables
		File f;
		Scanner scanner = null;
		// try reading the file
		try {
			f = new File(filename);
			scanner = new Scanner(f);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.length() != 0) {
					try {
						String[] parts = line.split(",");
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
					} catch (NumberFormatException nfe) {
						throw nfe;
					} catch (DateTimeParseException dtpe) {
						throw dtpe;
					} catch (ArrayIndexOutOfBoundsException air) {
						throw air;
					} catch (NullPointerException npe) {
						throw npe;
					} catch (Exception e) {
						throw e;
					}
				}
			}
		} catch (FileNotFoundException fnf) {
			throw fnf;
		} finally {
			scanner.close();
		}

	}

	/**
	 * Returns the Collection of Orders and exception(if any)
	 * 
	 * @return
	 */
	public Collection<Order> getOrderItems() {

		return OrderList.values();
	}

	/*
	 * public Object[] getOrderItems( int Customer ){ Object obj[] = new
	 * Object[2]; Map<Integer,Order> OrderItems = new TreeMap<Integer,Order>();
	 * try{ for (Entry<Integer, Order> entry : OrderList.entrySet()) { if(
	 * Customer == entry.getValue().getCustomer()){
	 * OrderItems.put(entry.getKey(), entry.getValue()); } }}catch (Exception e)
	 * { obj[1] = new MessageStore(e); } obj[0] = OrderItems; return obj; }
	 */

	public Map<Integer, Order> getOrderItems(int Customer) throws Exception {
		Object obj[] = new Object[2];
		Map<Integer, Order> OrderItems = new TreeMap<Integer, Order>();
		try {
			for (Entry<Integer, Order> entry : OrderList.entrySet()) {
				if (Customer == entry.getValue().getCustomer()) {
					OrderItems.put(entry.getKey(), entry.getValue());
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return OrderItems;
	}

	private void appendFile(Order o) throws IOException {

		String parts[] = new String[7];
		parts[0] = Integer.toString(o.getID());// id
		parts[1] = Integer.toString(o.getCustomer());// customer
		parts[2] = o.getItem();
		parts[3] = Integer.toString(o.getQuantity());
		parts[4] = Double.toString(o.getDiscount());
		parts[5] = Double.toString(o.getTotal());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		d.setTime(o.getTime().getTime());
		parts[6] = sdf.format(d);

		String output = System.lineSeparator() + String.join(",", parts);

		// try appending to orderlist
		try {
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(FileName, true));
			fileWriter.write(output);
			fileWriter.close();
		} catch (IOException e) {
			throw e;
		}

	}
}
