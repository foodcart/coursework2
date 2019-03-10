/**
 * 
 */
package controller;

import java.util.Observable;
import java.util.logging.Level;

import log.LogKeeper;
import model.OrderEntry;
import model.OrderQueue;

/**
 * @author Vimal
 *
 */
public class Waiter  implements Runnable {

	private String status;
	private OrderEntry currentOrder;
	private volatile boolean exit;;
	/**
	 * 
	 */
	private OrderQueue myQueue;

	public Waiter(OrderQueue oQueue) {
		this.myQueue = oQueue;
		this.status = "Not Started";
		this.exit = false;
	}

	public String getStatus() {
		return this.status;
	}

	public OrderEntry getCurrentOrder() {
		return this.currentOrder;
	}

	public void setExit(boolean e) {
		this.exit = e;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// when exit is false, keep running
		while (!exit) {
			this.status = "Running";
			LogKeeper.getInstance().addLog(Thread.currentThread().getName(), "Running");
			// check for New entry, and process it
			try {
				for (OrderEntry oEntry = myQueue.claim(Thread.currentThread().getName()); oEntry
						.getCustomerID() <= Manager.TERMINATOR; oEntry = myQueue
								.claim(Thread.currentThread().getName())) {
					// log the claim
					LogKeeper.getInstance().addLog(Thread.currentThread().getName(),
							"Preparing Orders for CustomerID " + oEntry.getCustomerID());
					// check if entry is End of Queue, then Stop Thread.
					if (oEntry.getCustomerID().equals(Manager.TERMINATOR)) {
						exit = true;
						break;
					}
					// else move the claimed entry to currentOrder and notify
					// observers
					currentOrder = oEntry;
					//notifyObservers();
					Integer itemCount = oEntry.getItemCount();
					// process the item, i.e. wait till its prepared in kitchen
					long waitForMillis = Manager.WAIT_TIME_PER_ITEM * itemCount * 1000;
					try {
						Thread.sleep(waitForMillis);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						// 
						LogKeeper.getInstance().addLog(Thread.currentThread().getName(), "Error in Thread.Sleep", e);
						e.printStackTrace();
					}
					// items prep time over, so lets set it Ready
					LogKeeper.getInstance().addLog(Thread.currentThread().getName(),
							"Order Ready for CustomerID " + oEntry.getCustomerID());
					oEntry.setReady();
					if (exit)
						break;
				}
			} catch (Exception e) {
				LogKeeper.getInstance().addLog(Thread.currentThread().getName(), "Error in processing", e);
				e.printStackTrace();
				exit = true;
			}
			// stopping so stop and notifyObservers
			this.status = "Stopped";
			LogKeeper.getInstance().addLog(Thread.currentThread().getName(), "Stopped");
			//notifyObservers();
		}
	}

}
