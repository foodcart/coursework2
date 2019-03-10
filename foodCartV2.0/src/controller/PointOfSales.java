/**
 * 
 */
package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import log.LogKeeper;
import model.InvalidQuantityException;
import model.Order;
import model.OrderEntry;
import model.OrderQueue;

/**
 * @author Vimal this is the Point of Sales Thread This will add Customers and
 *         their Orders from the Ordelist to the Shop Queue.
 */
public class PointOfSales implements Runnable {
	/**
	 * myQueue is the Instance of the Shop Queue autoMode is when Orders are
	 * being loaded from OrderList. orders is the Collection from the OrderList.
	 */
	private OrderQueue myQueue;
	private boolean autoMode;
	Collection<Order> orders;

	public PointOfSales(OrderQueue oQueue, Collection<Order> orders) {
		this.myQueue = oQueue;
		this.autoMode = true;
		this.orders = orders;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LogKeeper.getInstance().addLog(Thread.currentThread().getName(), "Ready to Take Orders");
		if (autoMode) {
			LogKeeper.getInstance().addLog(Thread.currentThread().getName(), "AutoMode:ON");
			addOrderstoQueue();
		}
		LogKeeper.getInstance().addLog(Thread.currentThread().getName(), "AutoMode:OFF");

	}

	/*
	 * Add Orders to the Shop Queue
	 */
	public void addOrderstoQueue() {
		OrderEntry oEntry;
		List<Order> ordersArray = null;
		Integer currentCustomer = 0;
		Iterator<Order> oIterator = orders.iterator();

		LogKeeper.getInstance().addLog(Thread.currentThread().getName(), "Adding Orders from OrderList");

		while (oIterator.hasNext()) {
			Order oneOrder = oIterator.next();
			if (currentCustomer == 0) {
				currentCustomer = oneOrder.getCustomer();
				ordersArray = new ArrayList<Order>();
				ordersArray.add(oneOrder);
			} else {
				if (currentCustomer == oneOrder.getCustomer()) {
					ordersArray.add(oneOrder);
				} else {
					oEntry = this.myQueue.add(currentCustomer, ordersArray.toArray(new Order[ordersArray.size()]));
					currentCustomer = oneOrder.getCustomer();
					ordersArray = new ArrayList<Order>();
					ordersArray.add(oneOrder);
				}
			}
		}
		// maybe there is an OrdersArray pending to be added to the queue.
		if (ordersArray.size() > 0) {
			oEntry = this.myQueue.add(currentCustomer, ordersArray.toArray(new Order[ordersArray.size()]));
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
		LogKeeper.getInstance().addLog(Thread.currentThread().getName(), "Orders added");
	}

}
