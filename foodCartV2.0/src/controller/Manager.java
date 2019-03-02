package controller;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import model.Order;
import model.OrderEntry;
import model.OrderList;
import model.OrderQueue;

public class Manager {
	private static OrderQueue shopQueue;
	private static OrderList orderList;
	
	private static String getCurrentDirectory(){
	
		CodeSource codeSource = Manager.class.getProtectionDomain().getCodeSource();
		File jarFile = null;
		try {
			jarFile = new File(codeSource.getLocation().toURI().getPath());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("foodCart Coffee Shop is running in dir : " + jarFile.getParentFile().getPath());
		System.out.println("Please ensure the menuitems.db and orderlist.db files are placed here");
		return jarFile.getParentFile().getPath();
	}
	
	private static void moveOrderToQueue(){
		OrderEntry oe;
		List<Order> ordersArray = null;
		Integer currentCustomer = 0; 
		Collection<Order> orders = orderList.getOrderItems();
		Iterator<Order> oIterator = orders.iterator();
		while (oIterator.hasNext()){
			Order oneOrder = oIterator.next();
			if(currentCustomer == 0){
				currentCustomer = oneOrder.getCustomer();
				ordersArray = new ArrayList<Order>();
				ordersArray.add(oneOrder);
				//shopQueue.add(oneOrder.getCustomer(), ordersArray.toArray(ordersArray.length));
			}else{
				if(currentCustomer == oneOrder.getCustomer()){
					ordersArray.add(oneOrder);
				}else{
					shopQueue.add(currentCustomer, ordersArray.toArray(new Order[ordersArray.size()]));
					currentCustomer = oneOrder.getCustomer();
					ordersArray = new ArrayList<Order>();
					ordersArray.add(oneOrder);
				}
			}
		}
		//maybe there isan OrdersArray pending to be added to the queue.
		if(ordersArray.size()>0){
			shopQueue.add(currentCustomer, ordersArray.toArray(new Order[ordersArray.size()]));
		}
		
	}
	
	
	public static void main(String[] args){
		// get current directory
		String jarDir = getCurrentDirectory();
		
		//initialize the OrderQueue
		shopQueue = new OrderQueue();
		
		//read the orders file to TreeMap
		orderList = new OrderList(jarDir + File.separator + "orderlist.db");
		
		//move the treeMap to the Queue
		moveOrderToQueue();
	}
	
}
