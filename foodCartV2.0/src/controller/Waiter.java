/**
 * 
 */
package controller;

import java.util.logging.Level;

import model.OrderEntry;
import model.OrderQueue;

/**
 * @author Vimal
 *
 */
public class Waiter implements Runnable {

	/**
	 * 
	 */
	private OrderQueue myQueue;
	public Waiter(OrderQueue oQueue) {
		this.myQueue = oQueue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {	
		Manager.getLogger().log(Level.FINE, "I am ready to recieve New Orders");
		//check for New entry, and process it
		for(OrderEntry oEntry = myQueue.claim(Thread.currentThread().getName());
				oEntry.getCustomerID()< Manager.TERMINATOR;
				oEntry = myQueue.claim(Thread.currentThread().getName())){
			Manager.getLogger().log(Level.FINE, "I am Preparing Orders for CustomerID "+oEntry.getCustomerID());
			Integer itemCount = oEntry.getItemCount();
			//System.out.println(Thread.currentThread().getName() + ": I am Processing Orders for Customer: " + oEntry.getCustomerID());
			//System.out.println(Thread.currentThread().getName() + ": Items in Order = " + itemCount);
			long waitForMillis = Manager.WAIT_TIME_PER_ITEM * itemCount * 1000; //n seconds per item.
			try {
				Thread.sleep(waitForMillis);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				Manager.getLogger().log(Level.SEVERE, "Error in Thread.Sleep", e);
			}
			Manager.getLogger().log(Level.FINE, "Items Ready for CustomerID "+oEntry.getCustomerID());
			oEntry.setReady();
		}
	}

}
