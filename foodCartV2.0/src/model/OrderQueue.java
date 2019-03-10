package model;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Level;

import javax.swing.table.DefaultTableModel;

import controller.Customer;
import controller.Manager;
import log.LogKeeper;
import view.QueueStatus;

public class OrderQueue extends Observable {
	private ConcurrentSkipListMap<Integer, OrderEntry> orderQueue;
	private Integer newCount;
	private QueueStatus queueStatus;

	public OrderQueue(QueueStatus queue) {
		orderQueue = new ConcurrentSkipListMap<Integer, OrderEntry>();
		newCount = 0;
		this.queueStatus = queue;
		this.addObserver(queue);
	}

	public synchronized OrderEntry add(Integer CustomerID, Order[] Orders) {
		
		try {
			Thread.sleep(new Long(Manager.POS_SERVICE_TIME * 1000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogKeeper.getInstance().addLog(Thread.currentThread().getName(), "Error in Thread.Sleep", e);

		}
		
		OrderEntry oe = new OrderEntry(Orders);
		// place the Order in the Shop Queue
		orderQueue.put(CustomerID, oe);
		// increment count of New items
		newCount++;
		if(!(CustomerID.equals(Manager.TERMINATOR))){
		LogKeeper.getInstance().addLog(Thread.currentThread().getName(),
				"Orders for CustomerID " + CustomerID + " added.");
		// get reference back and add customer as observer
		orderQueue.get(CustomerID).addObserver(new Customer(orderQueue.get(CustomerID)));
		// also add queueStatus view as the observer
		orderQueue.get(CustomerID).addObserver(queueStatus); }
		// notify the view that a new order has been added
		this.setChanged();
		notifyObservers();
		try {
			Thread.sleep(new Long(Manager.POS_NOTIFY_TIME * 1000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogKeeper.getInstance().addLog(Thread.currentThread().getName(), "Error in Thread.Sleep", e);

		}
		// Notify the Waiters
		notifyAll();
		return orderQueue.get(CustomerID);
	}

	public void remove(Integer CustomerID) {
		orderQueue.remove(CustomerID);
		System.out.println("Removed Customer from OrderQueue, Customer: " + CustomerID);
	}

	public synchronized OrderEntry claim(String threadName) {
		OrderEntry oe = null;
		while (newCount == 0) {
			try {
				LogKeeper.getInstance().addLog(Thread.currentThread().getName(), "Going to WAIT State");
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
		try {
			for (Entry<Integer, OrderEntry> entry : orderQueue.entrySet()) {
				if (entry.getValue().getStatus().equals(OrderStatus.NEW.status)) {
					oe = entry.getValue();
					if (!oe.getCustomerID().equals(Manager.TERMINATOR)) {
						oe.setClaim(threadName);
						newCount--;
					}
					break;
				}
			}
			notifyAll();
		} catch (Exception e) {
			LogKeeper.getInstance().addLog(Thread.currentThread().getName(), "Error in Claim()", e);
		}
		return oe;
	}

	public Collection<OrderEntry> getCollection() {
		return orderQueue.values();
	}
}