/**
 * 
 */
package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import model.InvalidQuantityException;
import model.Order;
import model.OrderEntry;
import model.OrderQueue;

/**
 * @author Vimal
 *
 */
public class PointOfSales implements Runnable {

	/**
	 * 
	 */
	private OrderQueue myQueue;
	private boolean autoMode;
	Collection<Order> orders;

	public PointOfSales(OrderQueue oQueue, Collection<Order> orders) {

		this.myQueue = oQueue;
		this.autoMode = true;
		this.orders = orders;
		//System.out.println(Thread.currentThread().getName()+": I have opened the POS and waiting for Customers");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		Manager.getLogger().log(Level.FINE, "I opened the POS and waiting for Customers");
		if (autoMode) {
			Manager.getLogger().log(Level.FINE, "I am adding Orders from the OrderList now");
			addOrderstoQueue();
		}

	}

	public void addOrderstoQueue() {
		OrderEntry oEntry;
		List<Order> ordersArray = null;
		Integer currentCustomer = 0;
		Iterator<Order> oIterator = orders.iterator();
		while (oIterator.hasNext()) {
			Order oneOrder = oIterator.next();
			if (currentCustomer == 0) {
				currentCustomer = oneOrder.getCustomer();
				ordersArray = new ArrayList<Order>();
				ordersArray.add(oneOrder);
				// shopQueue.add(oneOrder.getCustomer(),
				// ordersArray.toArray(ordersArray.length));
			} else {
				if (currentCustomer == oneOrder.getCustomer()) {
					ordersArray.add(oneOrder);
				} else {
					oEntry = this.myQueue.add(currentCustomer, ordersArray.toArray(new Order[ordersArray.size()]));
					//new Thread(new Customer(oEntry)).start();
					try {
						Thread.sleep(new Long(Manager.POS_SERVICE_TIME * 1000));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						Manager.getLogger().log(Level.SEVERE, "Error in Thread.Sleep", e);
					}
					currentCustomer = oneOrder.getCustomer();
					ordersArray = new ArrayList<Order>();
					ordersArray.add(oneOrder);
				}
			}
		}
		// maybe there is an OrdersArray pending to be added to the queue.
		if (ordersArray.size() > 0) {
			oEntry = this.myQueue.add(currentCustomer, ordersArray.toArray(new Order[ordersArray.size()]));
			//new Thread(new Customer(oEntry)).start();
		}
		// add the terminator item.
		try {
			this.myQueue.add(Manager.TERMINATOR,
					new Order[] { new Order(Manager.TERMINATOR, Manager.TERMINATOR, null, 1, null, 1, 1) });
		} catch (InvalidQuantityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.autoMode = false;// end automode
		Manager.getLogger().log(Level.FINE, "I finished adding orders from OrderList");
	}

}
