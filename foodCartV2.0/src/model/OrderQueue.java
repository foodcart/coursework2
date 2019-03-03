package model;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Level;

import controller.Manager;


public class OrderQueue {
	private ConcurrentSkipListMap<Integer, OrderEntry> orderQueue;
	private Integer newCount;
	
	public OrderQueue(){
		orderQueue = new ConcurrentSkipListMap<Integer, OrderEntry>();
		newCount = 0;
	}
	
	public synchronized OrderEntry add(Integer CustomerID, Order[] Orders){
		OrderEntry oe = new OrderEntry(Orders);
		orderQueue.put(CustomerID, oe);
		newCount ++;
		Manager.getLogger().log(Level.FINE, "Added new Customer to OrderQueue, Customer: " + CustomerID);
		//System.out.println("Added new Customer to OrderQueue, Customer: " + CustomerID );
		notifyAll();
		return orderQueue.get(CustomerID);
	}
	
	public void remove(Integer CustomerID){
		orderQueue.remove(CustomerID);
		System.out.println("Removed Customer from OrderQueue, Customer: " + CustomerID );
	}
	
	public synchronized OrderEntry claim(String threadName) {
		OrderEntry oe = null;
		while(newCount == 0){
			try {
				Manager.getLogger().log(Level.FINE, "I am put to WAIT State");
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		for (Entry<Integer, OrderEntry> entry : orderQueue.entrySet()) {
			if(entry.getValue().getStatus().equals(OrderStatus.NEW.status)){
				oe = entry.getValue();
				oe.setClaim(threadName);
				newCount--;
				break;
			}
		}
		notifyAll();
		return oe;
	}
}
