/**
 * 
 */
package controller;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;

import model.OrderEntry;

/**
 * @author Vimal
 *
 */
public class Customer implements Observer {

	/**
	 * 
	 */
	private OrderEntry myEntry;
	public Customer(OrderEntry oEntry) {
		this.myEntry = oEntry;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void update(Observable obj, Object arg){
		System.out.println("This is Customer " + myEntry.getCustomerID()+ ". and I got my Order");
	}

}
