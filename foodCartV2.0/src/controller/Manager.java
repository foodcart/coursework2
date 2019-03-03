package controller;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.logging.Level;
import java.util.logging.Logger;

import log.LogKeeper;
import log.LogManagerException;
import model.OrderList;
import model.OrderQueue;

public class Manager {
	public static final Integer TERMINATOR = new Integer(9999999);
	public static final Integer WAIT_TIME_PER_ITEM = new Integer(2);
	public static final Integer POS_SERVICE_TIME = new Integer(2);
	private static final String logProperties = new String("logger.properties");
	private static OrderQueue shopQueue;
	private static OrderList orderList;
	private static LogKeeper logKeeper;
	
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
	
	
	
	public static void main(String[] args){
		// get current directory
		String jarDir = getCurrentDirectory();
		//init the logging
		try {
			logKeeper = new LogKeeper(jarDir,logProperties);
		} catch (LogManagerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		logKeeper.getLogger().log(Level.FINE, "Starting up");
		//initialize the OrderQueue
		shopQueue = new OrderQueue();
		logKeeper.getLogger().log(Level.FINE, "Shop Queue is not open");
		//read the orders file to TreeMap
		try {
			orderList = new OrderList(jarDir + File.separator + "orderlist.db");
			logKeeper.getLogger().log(Level.FINE, "Loaded OrderList from "+jarDir + File.separator + "orderlist.db");
		} catch (Exception e) {
			logKeeper.getLogger().log(Level.SEVERE, "Order List not loaded", e);;
			e.printStackTrace();
		}	
		//start the waiter threads.
		new Thread(new Waiter(shopQueue),"Waiter 1").start();
		logKeeper.getLogger().log(Level.FINE, "Started Thread: Waiter 1");
		new Thread(new Waiter(shopQueue),"Waiter 2").start();
		logKeeper.getLogger().log(Level.FINE, "Started Thread: Waiter 2");
		//start the POS thread
		new Thread(new PointOfSales(shopQueue, orderList.getOrderItems()),"POS 1").start();		
		logKeeper.getLogger().log(Level.FINE, "Started Thread: POS");
	}
	
	public static Logger getLogger(){
		return logKeeper.getLogger();
	}
	
}
