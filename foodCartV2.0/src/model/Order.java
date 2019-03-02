package model;

import java.sql.Timestamp;

public class Order {

	private int id;
	private int customer;
	private String item;
	private int quantity;
	private double discount;
	private double total;
	private Timestamp time;
	
	public Order(int id, int customer, String item, int quantity, Timestamp time, double discount, double total) 
		throws InvalidQuantityException {
		if (quantity < 1) throw new InvalidQuantityException();
		else {
			this.id = id;
			this.customer = customer;
			this.item = item;
			this.quantity = quantity;
			this.time = time;
			this.discount = discount;
			this.total = total;
		}
	}
	
	public int getID() { return id;}
	public int getCustomer() { return customer; }
	public String getItem() { return item; }
	public int getQuantity() {return quantity; }
	public Timestamp getTime() { return time; }
	public double getDiscount() { return discount; }
	public double getTotal() { return total; }
	
}
