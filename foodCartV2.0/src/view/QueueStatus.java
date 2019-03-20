package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import controller.Manager;
import controller.Waiter;
import log.LogKeeper;
import model.OrderEntry;
import model.OrderQueue;
import model.OrderStatus;


public class QueueStatus extends JPanel implements Observer {

	/**
	 * @author Vimal
	 * @author Gabi
	 */
	private static final long serialVersionUID = -1695282276004883099L;
	private DefaultTableModel orderModel;
	private OrderQueue shopQueue;
	private JTextArea[] waiterStatus;
	private JScrollPane[] waiterPane;
	private ArrayList<Thread> waiterThreadArray;
	private ArrayList<Waiter> waiterArray;
	private Integer maxWaiters;
	private JComboBox waiterNumber;
	private JComboBox waitSeconds;
	private JComboBox newOrderSeconds;
	private QSActionListener actionListener;

	public void createLink(OrderQueue oq) {
		this.shopQueue = oq;
	}

	public void linkToWaiters(ArrayList<Thread> waiterThreadArray, ArrayList<Waiter> waiterArray) {
		this.waiterThreadArray = waiterThreadArray;
		this.waiterArray = waiterArray;
	}

	public QueueStatus(Integer maxWaiters) {

		super(new GridLayout(1, 2));

		this.maxWaiters = maxWaiters;
		actionListener = new QSActionListener();
		// Order processing status
		prepareLeftPane();
		// Waiter status
		prepareRightPane();

	}

	/*
	 * On the left side we have the Order Status Table
	 */
	private void prepareLeftPane() {

		int cpHeight = this.getHeight();
		this.getBounds().setSize(20, cpHeight);
		JPanel splitWindow = new JPanel();
		splitWindow.setLayout(new BoxLayout(splitWindow, BoxLayout.Y_AXIS));

		// Show the Wait Time Settings Panel
		prepareTimeSettingPanel(splitWindow);
		// show the Orders status
		prepareStatusTable(splitWindow);

		this.add(splitWindow);
	}

	/*
	 * Prepare the TimeSettings Pane
	 */
	private void prepareTimeSettingPanel(JPanel panel) {
		JPanel timeSettingPanel = new JPanel();
		timeSettingPanel.setLayout(new BoxLayout(timeSettingPanel, BoxLayout.Y_AXIS));
		timeSettingPanel.setBorder(BorderFactory.createTitledBorder("Lead Time Settings"));
		prepareSpeedControl(timeSettingPanel);
		panel.add(timeSettingPanel);
	}

	/*
	 * Render the Status Table
	 */
	private void prepareStatusTable(JPanel panel) {
		// column names
		String[] columnNames = { "CustomerID", "Item Count", "Status", "Waiting Staff" };
		orderModel = new DefaultTableModel(columnNames, 0);
		// holding panel
		JPanel catPanel = new JPanel();
		catPanel.setLayout(new BoxLayout(catPanel, BoxLayout.Y_AXIS));
		// Status Table
		JTable statusTable = new JTable(orderModel);
		JScrollPane tableHolder = new JScrollPane(statusTable);
		catPanel.setBorder(BorderFactory.createTitledBorder("Order Status"));
		// populate to GUI
		catPanel.add(tableHolder);
		panel.add(catPanel);
	}

	/*
	 * Render panel to control Waiter speed
	 */
	private void prepareSpeedControl(JPanel panel) {
		// item speed control
		setSpeedPerItem(panel);
		setPOSNotificationSpeed(panel);

	}

	/*
	 * set speed per item
	 */
	private void setSpeedPerItem(JPanel panel) {
		String[] comboItems = new String[10];
		for (int i = 0; i < 10; i++) {
			comboItems[i] = Integer.toString(i + 1) + " seconds";
		}
		waitSeconds = new JComboBox<>(comboItems);
		waitSeconds.setSelectedIndex(9);

		JLabel comboLabel = new JLabel("Prep. Lead Time(per item): ");
		JButton comboButton = new JButton("Apply");
		comboButton.addActionListener(actionListener);
		comboButton.setActionCommand("WPI");
		JPanel comboPanel = new JPanel();
		comboPanel.setLayout(new GridLayout(1, 3, 5, 10));
		comboPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		comboPanel.setPreferredSize(new Dimension(485, 35));
		comboPanel.setMaximumSize(new Dimension(485, 35));
		comboPanel.add(comboLabel);// label
		comboPanel.add(waitSeconds);// combobox
		comboPanel.add(comboButton);
		panel.add(comboPanel);
	}

	private void setPOSNotificationSpeed(JPanel panel) {
		String[] comboItems = new String[10];
		for (int i = 0; i < 10; i++) {
			comboItems[i] = Integer.toString(i + 1)+ " seconds";
		}
		newOrderSeconds = new JComboBox<>(comboItems);
		newOrderSeconds.setSelectedIndex(9);

		JLabel comboLabel = new JLabel("New Order Lead Time: ");
		JButton comboButton = new JButton("Apply");
		comboButton.addActionListener(actionListener);
		comboButton.setActionCommand("NON");
		JPanel comboPanel = new JPanel();
		comboPanel.setLayout(new GridLayout(1, 3, 5, 10));
		comboPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		comboPanel.setPreferredSize(new Dimension(485, 35));
		comboPanel.setMaximumSize(new Dimension(485, 35));
		comboPanel.add(comboLabel);// label
		comboPanel.add(newOrderSeconds);// combobox
		comboPanel.add(comboButton);
		panel.add(comboPanel);
	}

	/*
	 * On the right side we have the waiters and what they are doing
	 */
	private void prepareRightPane() {
		JPanel wPanel = new JPanel();
		// new GridLayout(maxWaiters+1, 1)
		wPanel.setLayout(new BoxLayout(wPanel, BoxLayout.Y_AXIS));
		wPanel.setBorder(BorderFactory.createTitledBorder("Waiter Info"));

		// Waiter Thread Combo Box
		prepareWaiterSelBox(maxWaiters, wPanel);
		// waiter text bozes
		prepareWaiterStatusBoxes(wPanel);
		JScrollPane wScrollPane = new JScrollPane(wPanel);
		wScrollPane.setBorder(BorderFactory.createEmptyBorder());
		this.add(wScrollPane);
	}

	/*
	 * Control for seting/resetting active waiter count
	 */
	private void prepareWaiterSelBox(Integer maxWaiters, JPanel wPanel) {
		String[] comboItems = new String[maxWaiters];
		for (int i = 0; i < maxWaiters; i++) {
			comboItems[i] = Integer.toString(i + 1);
		}
		waiterNumber = new JComboBox<>(comboItems);
		waiterNumber.setSelectedIndex(1);

		JLabel comboLabel = new JLabel("Active Waiter count: ");
		JButton comboButton = new JButton("Apply");
		comboButton.addActionListener(actionListener);
		comboButton.setActionCommand("WAIT");
		JPanel comboPanel = new JPanel();
		comboPanel.setLayout(new GridLayout(1, 3, 5, 10));
		comboPanel.setBorder((BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				BorderFactory.createEmptyBorder(5, 5, 5, 5))));
		comboPanel.setPreferredSize(new Dimension(450, 35));
		comboPanel.setMaximumSize(new Dimension(450, 35));
		comboPanel.add(comboLabel);// label
		comboPanel.add(waiterNumber);// combobox
		comboPanel.add(comboButton);
		wPanel.add(comboPanel);

	}

	/*
	 * Each waiter has a status area, a JTextbox. add the dynamic waiter text
	 * boxes
	 */
	private void prepareWaiterStatusBoxes(JPanel wPanel) {

		waiterStatus = new JTextArea[maxWaiters];
		waiterPane = new JScrollPane[maxWaiters];
		for (int i = 0; i < maxWaiters; i++) {
			waiterStatus[i] = new JTextArea();
			waiterPane[i] = new JScrollPane(waiterStatus[i]);
			waiterStatus[i].setEditable(false);
			waiterPane[i].setBorder(BorderFactory.createTitledBorder("Waiter" + i));
			waiterPane[i].setPreferredSize(new Dimension(450, 90));
			waiterPane[i].setMaximumSize(new Dimension(450, 90));
			wPanel.add(waiterPane[i]);
		}
	}

	/*
	 * Auto update the Order status and Waiter text boxes
	 */
	private void refreshOrderModel() {

		// thread status update.
		threadStatusUpdate();

		// keep track of Orders Status
		orderStatusUpdate();

		this.validate();
	}

	public QueueStatus(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	public QueueStatus(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public QueueStatus(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	/*
	 * This method will check the Thread status
	 */
	private void threadStatusUpdate() {
		Thread oneWaiter;

		try {
			for (int i = 0; i < maxWaiters; i++) {
				String status = new String();
				waiterStatus[i].setText(status);
				oneWaiter = waiterThreadArray.get(i);
				if (oneWaiter.getState().toString().equals("NEW")
						|| oneWaiter.getState().toString().equals("TERMINATED")) {
					waiterStatus[i].setDisabledTextColor(getBackground());
					waiterStatus[i].setEnabled(false);
					waiterPane[i].setEnabled(false);
					if (oneWaiter.getState().toString().equals("TERMINATED")) {
						status = "Stopped";
						waiterStatus[i].setText(status);
					}
				} else {
					waiterStatus[i].setEnabled(true);
					waiterStatus[i].setEditable(false);
					waiterPane[i].setEnabled(true);
					status = "Waiting";
					waiterStatus[i].setText(status);

				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/*
	 * This method will check the Orders Collection and update the JTable
	 */
	private void orderStatusUpdate() {

		String status = new String("");
		Collection<OrderEntry> collection = shopQueue.getCollection();
		Iterator<OrderEntry> iterator = collection.iterator();
		// remove all entries from table
		if (orderModel.getRowCount() > 0) {
			for (int i = orderModel.getRowCount() - 1; i > -1; i--) {
				orderModel.removeRow(i);
			}
		}
		int row = 0;
		while (iterator.hasNext()) {
			OrderEntry oe = iterator.next();
			if (oe.getCustomerID().equals(Manager.TERMINATOR))
				break;
			orderModel.insertRow(row, new Object[] { oe.getCustomerID(), oe.getItemCount(), oe.getStatus(), oe.getProcessingThread() });
			row++;
			// parallel update to waiter status
			String processingThread = oe.getProcessingThread();
			if ((!(processingThread.equals(""))) && oe.getStatus().equals(OrderStatus.PREPARING.status)) {
				try {
					Integer index = Integer.valueOf(processingThread.substring(processingThread.length() - 1));
					waiterStatus[index]
							.setText("Processing Order for Customer ID" + oe.getCustomerID() + System.lineSeparator());
					try {
						if (oe.getOrderCount() > 0) {
							for (int i = 0; i < oe.getOrderCount(); i++) {

								status = Integer.toString(i + 1) + ")" + oe.getItem(i).getQuantity() + "No(s). "
										+ oe.getItem(i).getItem() + System.lineSeparator();
								waiterStatus[index].append(status);

							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						LogKeeper.getInstance().addLog(Thread.currentThread().getName(),
								"Error in adding Items to Status", e);

					}
				} catch (Exception e) {
					e.printStackTrace();
					LogKeeper.getInstance().addLog(Thread.currentThread().getName(), "Error in status update", e);
				}

			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		refreshOrderModel();

	}

	class QSActionListener implements ActionListener {

		public QSActionListener() {
			super();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("NON")) {

				Integer newOrderTime = newOrderSeconds.getSelectedIndex();
				LogKeeper.getInstance().addLog("Manager/QueueStatus",
						"New Order Lead Time = " + Manager.POS_SERVICE_TIME.toString());
				LogKeeper.getInstance().addLog("Manager/QueueStatus",
						"Setting New Order Lead Time = " + (newOrderTime + 1));
				Manager.POS_SERVICE_TIME = newOrderTime.intValue() + 1;
				JOptionPane.showMessageDialog(new JFrame(),
						"New Order Lead Time set to " + Manager.POS_SERVICE_TIME + " seconds");
			} else if (e.getActionCommand().equals("WPI")) {

				Integer waitTime = waitSeconds.getSelectedIndex();
				LogKeeper.getInstance().addLog("Manager/QueueStatus",
						"Prep.Lead Time =  " + Manager.WAIT_TIME_PER_ITEM);
				LogKeeper.getInstance().addLog("Manager/QueueStatus", "Setting Prep.Lead Time =  " + (waitTime + 1));
				Manager.WAIT_TIME_PER_ITEM = waitTime.intValue() + 1;
				JOptionPane.showMessageDialog(new JFrame(),
						"Lead Time in Kitchen set to " + Manager.WAIT_TIME_PER_ITEM + " seconds");
			
			} else if (e.getActionCommand().equals("WAIT")) {

				Integer maxWaiters = waiterNumber.getSelectedIndex();
				LogKeeper.getInstance().addLog("Manager/QueueStatus", "Setting Active Waiters to " + (maxWaiters + 1));
				for (int i = 0; i < waiterThreadArray.size(); i++) {
					// for threads more than specified number.
					if (i > (maxWaiters)) {
						if (waiterThreadArray.get(i).isAlive()) {
							// stop them.
							waiterArray.get(i).setExit(true);
						}
					} else {
						// threads within required number
						if (!(waiterThreadArray.get(i).isAlive())) {
							// if not started, start them.
							waiterArray.get(i).setExit(false);
							waiterThreadArray.get(i).start();
						}
					}
				}
				JOptionPane.showMessageDialog(new JFrame(),
						"Number of Active Waiters set to " + (maxWaiters.intValue()+1));
				// refresh the model
				refreshOrderModel();
			}
		}
	}

}
