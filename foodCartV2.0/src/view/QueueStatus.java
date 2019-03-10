package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import controller.Manager;
import controller.Waiter;
import log.LogKeeper;
import model.OrderEntry;
import model.OrderQueue;
import model.OrderStatus;

public class QueueStatus extends JPanel implements Observer {

	DefaultTableModel orderModel;
	OrderQueue shopQueue;
	JTextArea[] waiterStatus;
	JScrollPane[] waiterPane;
	ArrayList<Thread> waiterThreadArray;
	Integer maxWaiters;

	public void linkToQueue(OrderQueue oq) {
		this.shopQueue = oq;
	}

	public void linkToWaiters(ArrayList<Thread> waiterThreadArray) {
		this.waiterThreadArray = waiterThreadArray;
	}

	public QueueStatus(Integer maxWaiters) {

		super(new GridLayout(1, 2));

		this.maxWaiters = maxWaiters;
		int cpHeight = this.getHeight();
		this.getBounds().setSize(20, cpHeight);
		// Order Status
		JPanel catPanel = new JPanel(new BorderLayout());
		catPanel.setBorder(BorderFactory.createTitledBorder("Order Status"));
		String[] columnNames = { "CustomerID", "Item Count", "Status" };
		orderModel = new DefaultTableModel(columnNames, 0);
		JTable statusTable = new JTable(orderModel);
		JScrollPane tableHolder = new JScrollPane(statusTable);
		catPanel.add(tableHolder);
		this.add(catPanel);
		// Waiter status
		JPanel wPanel = new JPanel(new GridLayout(maxWaiters, 1));
		wPanel.setBorder(BorderFactory.createTitledBorder("Waiter Status"));

		waiterStatus = new JTextArea[maxWaiters];
		waiterPane = new JScrollPane[maxWaiters];
		for (int i = 0; i < maxWaiters; i++) {
			waiterStatus[i] = new JTextArea();
			waiterPane[i] = new JScrollPane(waiterStatus[i]);
			waiterStatus[i].setEditable(false);
			waiterPane[i].setBorder(BorderFactory.createTitledBorder("Waiter" + i));
			wPanel.add(waiterPane[i]);
		}
		JScrollPane wScrollPane = new JScrollPane(wPanel); 
		this.add(wScrollPane);

	}

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
			orderModel.insertRow(row, new Object[] { oe.getCustomerID(), oe.getItemCount(), oe.getStatus() });
			row++;
			// parallel update to waiter status
			String processingThread = oe.getProcessingThread();
			if ((!(processingThread.equals(""))) && oe.getStatus().equals(OrderStatus.PREPARING.status)) {
				try {
					Integer index = Integer.valueOf(processingThread.substring(processingThread.length() - 1));
					waiterStatus[index]
							.setText("Processing Order for Customer ID" + oe.getCustomerID() + System.lineSeparator());
					for (int i = 0; i < oe.getItemCount(); i++) {
						waiterStatus[index].append(i + 1 + ")" + oe.getItem(i).getQuantity() + " "
								+ oe.getItem(i).getItem() + System.lineSeparator());
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

}
