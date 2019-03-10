package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
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
import javax.swing.table.DefaultTableModel;

import controller.Manager;
import model.OrderEntry;
import model.OrderQueue;

public class QueueStatus extends JPanel  implements Observer {
	
	DefaultTableModel orderModel;
	OrderQueue shopQueue;

	public void linkToQueue(OrderQueue oq){
		this.shopQueue = oq;
	}
	public QueueStatus( ) {
		// TODO Auto-generated constructor stub
		super(new GridLayout(1, 2));
		int cpHeight = this.getHeight();
		this.getBounds().setSize(20, cpHeight);
		JPanel catPanel = new JPanel(new BorderLayout());
		catPanel.setBorder(BorderFactory.createTitledBorder("Order Status"));
		
		String[] columnNames = {"CustomerID","Item Count","Status"};
		orderModel = new DefaultTableModel(columnNames, 0);
		JTable statusTable = new JTable(orderModel);
		JScrollPane tableHolder = new JScrollPane(statusTable);
		catPanel.add(tableHolder);
		this.add(catPanel);
	}

	private void refreshOrderModel(){
		Collection<OrderEntry> collection = shopQueue.getCollection();
		Iterator<OrderEntry> iterator = collection.iterator();
		//remove all entries from table
		if (orderModel.getRowCount() > 0){
		for(int i = orderModel.getRowCount()-1; i > -1; i--){
			orderModel.removeRow(i);
		}
		}
		int row = 0;
		while(iterator.hasNext()){
			OrderEntry oe = iterator.next();
			if(oe.getCustomerID().equals(Manager.TERMINATOR))break;
			orderModel.insertRow(row, new Object[]{oe.getCustomerID(), oe.getItemCount(), oe.getStatus()});
			row++;
		}
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

	@Override
	public void update(Observable o, Object arg) {
		refreshOrderModel();
		
	}

}
