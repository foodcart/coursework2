/**
 * 
 */
package controller;

import model.OrderEntry;
import model.OrderQueue;

/**
 * @author Vimal
 *
 */
public class Customer implements Runnable {

	/**
	 * 
	 */
	private OrderEntry myEntry;
	public Customer(OrderEntry oEntry) {
		this.myEntry = oEntry;
		System.out.println("I am a Customer, and my ID is "+ myEntry.getCustomerID());
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
