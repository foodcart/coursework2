package controller;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import log.LogFormatter;
import log.LogKeeper;
import log.LogManagerException;
import model.OrderList;
import model.OrderQueue;

public class Manager {
	public  static final Integer TERMINATOR = new Integer(9999999);
	public  static final Integer WAIT_TIME_PER_ITEM = new Integer(1);
	public  static final Integer POS_SERVICE_TIME = new Integer(1);
	private static final String logProperties = new String("logger.properties");
	private static final String myName = "Manager";
	private static OrderQueue shopQueue;
	private static OrderList orderList;
	private static LogKeeper logKeeper;
	private static Thread[] waiterThreadArray; 
	private static Waiter[] waiterArray;
	private static Manager myInstance;
	private static Thread POS;
	
	public static Manager getInstance(String jarDir){
		if(myInstance==null) myInstance = new Manager(jarDir);
		return myInstance;
	}
	private Manager(String jarDir){
		//init the logging
		try {
			logKeeper = LogKeeper.getInstance(jarDir,logProperties);
		} catch (LogManagerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		logKeeper.addLog(myName, "Starting up Application");
		//initialize the OrderQueue
		shopQueue = new OrderQueue();
		logKeeper.addLog(myName,"...Starting up Shop Queue ");
		//read the orders file to TreeMap
		try {
			orderList = new OrderList(jarDir + File.separator + "orderlist.db");
			logKeeper.addLog(myName,"...OrderList loaded from "+jarDir + File.separator + "orderlist.db");
		} catch (Exception e) {
			logKeeper.addLog(myName,"...OrderList load failed", e);;
		}	
		//init the waiter threads.
		waiterThreadArray = new Thread[5];
		waiterArray = new Waiter[5];
		
		for(int i=0; i<5; i++){
			waiterArray[i] = new Waiter(shopQueue);
			waiterThreadArray[i] = new Thread(waiterArray[i],"Waiter" + i );
		}
		//init the POS
		POS = new Thread(new PointOfSales(shopQueue, orderList.getOrderItems()),"POS");
		
		//start 2 waiters
		logKeeper.addLog(myName, "...Started Thread: Waiter0");
		waiterThreadArray[0].start();
		logKeeper.addLog(myName, "...Started Thread: Waiter1");
		waiterThreadArray[1].start();
		//start the POS thread
		POS.start();
		logKeeper.addLog(myName, "...Started Thread: POS");
		logKeeper.addLog(myName, "Startup complete.");
	}
	

}
