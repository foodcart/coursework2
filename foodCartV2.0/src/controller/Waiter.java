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
public class Waiter implements Runnable {

	/**
	 * 
	 */
	private OrderQueue myQueue;
	public Waiter(OrderQueue oQueue) {
		this.myQueue = oQueue;
		System.out.println(Thread.currentThread().getName() + ": I am ready to take Orders");
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {	
		//check for New entry, and process it
		for(OrderEntry oEntry = myQueue.claim(Thread.currentThread().getName());
				!(oEntry.getCustomerID()==Manager.TERMINATOR);
				oEntry = myQueue.claim(Thread.currentThread().getName())){
			Integer itemCount = oEntry.getItemCount();
			System.out.println(Thread.currentThread().getName() + ": I am Processing Orders for Customer: " + oEntry.getCustomerID());
			System.out.println(Thread.currentThread().getName() + ": Items in Order = " + itemCount);
			long waitForMillis = Manager.WAIT_TIME_PER_ITEM * itemCount * 1000; //n seconds per item.
			try {
				Thread.sleep(waitForMillis);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			oEntry.setReady();
		}
	}

}
