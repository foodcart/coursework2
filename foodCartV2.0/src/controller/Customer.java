/**
 * 
 */
package controller;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import log.LogKeeper;
import model.OrderEntry;
import model.OrderStatus;

/**
 * @author Vimal
 * This is the Customer in the Shop Queue. 
 * Customer gets instantiated when the Order is added to the Queue, 
 * and then waits for Order to be ready
 */
public class Customer implements Observer {

	/**
	 * 
	 */
	private OrderEntry myEntry;
	public Customer(OrderEntry oEntry) {
		this.myEntry = oEntry;
		LogKeeper.getInstance().addLog("Customer " +myEntry.getCustomerID(), "Thanks, I  will wait for my order");
//		System.out.println("This is Customer " + myEntry.getCustomerID()+ ". and I am waiting for my items");
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * Update will make the customer receive the Order from the Shop Queue
	 */
	@Override
	public synchronized void update(Observable obj, Object arg){

		long leftLimit = new Long(2000);//2seconds
	    long rightLimit = new Long(5000);//5seconds
		if(myEntry.getStatus().equals(OrderStatus.READY.status)){
		    //random wait to respond
		try {
			wait(leftLimit + (long) (Math.random() * (rightLimit - leftLimit)));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //}
		LogKeeper.getInstance().addLog("Customer " +myEntry.getCustomerID(), "I got my items. Thank you");
		myEntry.setPickedUp();
		}
	}

}
