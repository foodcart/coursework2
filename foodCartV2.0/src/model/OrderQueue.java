package model;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;


public class OrderQueue {
	private ConcurrentSkipListMap<Integer, OrderEntry> orderQueue;

	public OrderQueue(){
		orderQueue = new ConcurrentSkipListMap<Integer, OrderEntry>();
	}
	
	public OrderEntry add(Integer CustomerID, Order[] Orders){
		OrderEntry oe = new OrderEntry(Orders);
		orderQueue.put(CustomerID, oe);
		System.out.println("Added new Customer to OrderQueue, Customer: " + CustomerID );
		return orderQueue.get(CustomerID);
	}
	
	public void remove(Integer CustomerID){
		orderQueue.remove(CustomerID);
		System.out.println("Removed Customer from OrderQueue, Customer: " + CustomerID );
	}
	
	public synchronized OrderEntry claim(String threadName){
		OrderEntry oe = null;
		for (Entry<Integer, OrderEntry> entry : orderQueue.entrySet()) {
			if(entry.getValue().getStatus().equals(OrderStatus.NEW.status)){
				oe = entry.getValue();
				oe.setClaim(threadName);
				break;
			}
		}
		return oe;
	}
}
