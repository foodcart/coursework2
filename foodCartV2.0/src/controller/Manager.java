package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import log.LogFormatter;
import log.LogKeeper;
import log.LogManagerException;
import model.OrderList;
import model.OrderQueue;
import view.MainPanel;
import view.NorthPanel;
import view.QueueStatus;

public class Manager extends JFrame {
	/**
	 * The Manager is the Application Window
	 */
	private static final long serialVersionUID = -6467568814079672123L;
	// max orders/customers
	public static final Integer TERMINATOR = new Integer(9999999);
	// wait time for preparing each item
	public static final Integer WAIT_TIME_PER_ITEM = new Integer(5);
	// wait time at POS
	public static final Integer POS_SERVICE_TIME = new Integer(1);
	// wait time at POS to notify waiter
	public static final Integer POS_NOTIFY_TIME = new Integer(3);
	// file for logger props
	private static final String logProperties = new String("logger.properties");
	// this class identifies
	private static final String myName = "Manager";
	// the ShopQueue
	private static OrderQueue shopQueue;
	// Orders loaded from file
	private static OrderList orderList;
	// the Log provider SingleTon
	private static LogKeeper logKeeper;
	// Array list for Waiter Staff
	private static ArrayList<Thread> waiterThreadArray;
	private static ArrayList<Waiter> waiterArray;
	// A single POS in this Shop
	private static Thread POS;
	// Instance of this class
	private static Manager myInstance;
	// Action Listener for ToolBar Buttons
	private ActionListener actionListener;
	// The main view anchor
	private MainPanel viewAnchor;
	// status Panel
	private QueueStatus queueStatus;
// the end time report
	private static String log;
	/*
	 * This class is also a SingleTon, so return its instance.
	 */
	public static Manager getInstance(String jarDir) {
		if (myInstance == null)
			myInstance = new Manager(jarDir);
		return myInstance;
	}
	
	/*
	 * Return an already existing instance.
	 */
	public static Manager getInstance() {
		
		return myInstance;
	}
	
	public void addMessage(String message){
		log = log.concat(message);
	}

	/*
	 * Private constructor
	 */
	private Manager(String jarDir) {
		// this class is also a JFrame, so init itself.
		super();
		// init the Singleton Logger
		initLogger(jarDir);
		// startup the application
		startUp(jarDir, 5);
	}

	/*
	 * Startup the application
	 */
	private void startUp(String jarDir, Integer WaiterCount) {

		logKeeper.addLog(myName, "Starting up Application");
		//prepare the reporting area also
		log = new String();
		// setup the Application Window.
		preparePanels(WaiterCount);
		// initialize the OrderQueue
		initShopQueue(jarDir, queueStatus);
		// init the 5 waiter threads.
		initWaiterArray(WaiterCount);
		// init the POS
		initPOS();
		// start the POS thread
		startPOS();		
		// start 2 waiters
		startWaiter(0);
		startWaiter(1);
		//startWaiter(2);
		//startWaiter(3);
		//startWaiter(4);
		logKeeper.addLog(myName, "Startup complete.");
		this.setVisible(true);
		joinAll(5);
	}

	/*
	 * Init the Logger
	 */
	private void initLogger(String jarDir) {
		// init the logging
		try {
			logKeeper = LogKeeper.getInstance(jarDir, logProperties);
		} catch (LogManagerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/*
	 * This method will initialize the Shop Queue before opening it for
	 * customers
	 */
	private void initShopQueue(String jarDir, QueueStatus queueStatus) {
		shopQueue = new OrderQueue(queueStatus);
		queueStatus.linkToQueue(shopQueue);
		logKeeper.addLog(myName, "...Starting up Shop Queue ");
		// read the orders file to TreeMap
		try {
			orderList = new OrderList(jarDir + File.separator + "orderlist.db");
			logKeeper.addLog(myName, "...OrderList loaded from " + jarDir + File.separator + "orderlist.db");
		} catch (Exception e) {
			logKeeper.addLog(myName, "...OrderList load failed", e);
			;
		}
	}

	/*
	 * This method inits the Thread Waiter Array, and adds Max number of Waiters
	 */
	private void initWaiterArray(Integer Max) {

		logKeeper.addLog(myName, "...Initializing " + Max + " Waiter Thread(s)");
		waiterThreadArray = new ArrayList<Thread>();
		waiterArray = new ArrayList<Waiter>();

		for (int i = 0; i < Max; i++) {
			waiterArray.add(new Waiter(shopQueue));
			try {
				waiterThreadArray.add(new Thread(waiterArray.get(i), "Waiter" + i));
			} catch (ArrayIndexOutOfBoundsException e) {
				logKeeper.addLog(myName, "Failed to add Waiter#" + i, e);
				;
			}
		}
		queueStatus.linkToWaiters(waiterThreadArray);
	}

	private void initPOS() {
		logKeeper.addLog(myName, "...Initializing 1 POS Thread");
		POS = new Thread(new PointOfSales(shopQueue, orderList.getOrderItems()), "POS");
	}

	/*
	 * Start the Waiter Thread i
	 */
	private void startWaiter(Integer i) {
		// start a Waiter Thread from the Thread Array, ThreadArray[i]
		try {
			logKeeper.addLog(myName, "...Started Thread: Waiter" + i);
			waiterThreadArray.get(i).start();
		} catch (ArrayIndexOutOfBoundsException e) {

			logKeeper.addLog(myName, "Error starting Thread: Waiter" + i, e);
		}
	}

	/*
	 * Start the POS thread
	 */
	private void startPOS() {
		POS.start();
		logKeeper.addLog(myName, "...Started Thread: POS");
	}

	/*
	 * Prepare other panels
	 */
	private void preparePanels(Integer maxWaiters) {
		// set look and feel of the UI
		setDefaultLookAndFeel();
		setWindowOnCloseHandler();
		setNorthPanel();
		setCenterPanel(maxWaiters);

	}

	/*
	 * Set the GUI look and feel based on OS
	 */
	private void setDefaultLookAndFeel() {
		try {
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		// set layout for the frame.
		this.setLayout(new BorderLayout(2, 2));
		// set output properties of the frame.
		this.setTitle("FoodCart: Coffee Shop Manager");
		// this.setIconImage(icon.getImage());
		this.setSize(1000, 650);// 1200 width : 600 height
		
		this.setLocationRelativeTo(null);// to set to center
		
	}
	
	private void setWindowOnCloseHandler(){
		//this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we){
				generateLogReport(true);
			}
	});
	}

	private void generateLogReport(boolean b){
		this.setVisible(false);
		this.dispose();
		Color white = new Color(255,255,255);

		JTextArea logArea = new JTextArea( );
		JScrollPane logPane = new JScrollPane(logArea);
		logPane.setBackground(white);
		JFrame logFrame = new JFrame("Food Cart: Log File");
		logFrame.setSize(900, 600);// 1200 width : 600 height
		logFrame.setLocationRelativeTo(null);// to set to center
		logFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		logFrame.getContentPane().add(logPane);
		log = logKeeper.getLogMessage();
		logArea.append(log); 
		logArea.setEditable(false);
		logFrame.getContentPane().validate();
		logFrame.setVisible(true);
		
	}	
	
	private void setNorthPanel() {
		actionListener = new ButtonsActionListener();
		NorthPanel northPanel = new NorthPanel(actionListener);
		northPanel.setButtonState("new", false);
		northPanel.setButtonState("order", false);
		northPanel.setButtonState("print", false);
		northPanel.setButtonState("queue", false);
		this.add(northPanel, BorderLayout.NORTH);	
	}
	
	private void setCenterPanel(Integer maxWaiters) {	
		viewAnchor = new MainPanel();
		setQueuePanel(maxWaiters);
		JScrollPane viewScroller = new JScrollPane(viewAnchor);
		this.add(viewScroller,BorderLayout.CENTER);
		
	}
	
	private void setQueuePanel(Integer maxWaiters){
		queueStatus = new QueueStatus(maxWaiters);
		viewAnchor.add(queueStatus,BorderLayout.CENTER);
		//this.add(queueStatus, BorderLayout.WEST);
	}
	
	private void joinAll(Integer Max){
		//join waiters
		for (int i = 0; i < Max; i++) {
			try {
				waiterThreadArray.get(i).join();
			} catch (ArrayIndexOutOfBoundsException e) {
				logKeeper.addLog(myName, "Failed to join Waiter#" + i, e);
				e.printStackTrace();
				
			} catch (InterruptedException e) {
				logKeeper.addLog(myName, "Failed to join Waiter#" + i, e);
				e.printStackTrace();
			}
		}
		try {
			POS.join();
		} catch (InterruptedException e) {
			logKeeper.addLog(myName, "Failed to join POS", e);
			e.printStackTrace();
		}
		System.out.println("Closing......");
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	/*
	 * Sub classes for Action Handling etc
	 */
	// sub class actionListener for other buttons
	class ButtonsActionListener implements ActionListener {
		/*
		 * Listens to Button Presses on the ToolBar (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.
		 * ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
		}

	}

}
