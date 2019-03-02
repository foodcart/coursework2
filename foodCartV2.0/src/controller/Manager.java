package controller;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
import model.OrderList;
import model.OrderQueue;

public class Manager {
	public static final Integer TERMINATOR = new Integer(9999999);
	public static final Integer WAIT_TIME_PER_ITEM = new Integer(2);
	public static final Integer POS_SERVICE_TIME = new Integer(2);
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
	
	
	
	public static void main(String[] args){
		// get current directory
		String jarDir = getCurrentDirectory();
		
		//initialize the OrderQueue
		shopQueue = new OrderQueue();
		
		//read the orders file to TreeMap
		try {
			orderList = new OrderList(jarDir + File.separator + "orderlist.db");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		//start the waiter threads.
		new Thread(new Waiter(shopQueue),"Waiter 1").start();
		new Thread(new Waiter(shopQueue),"Waiter 2").start();
		//start the POS thread
		new Thread(new PointOfSales(shopQueue, orderList.getOrderItems()),"POS 1").start();		
	}
	
}
