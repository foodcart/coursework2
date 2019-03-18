/**
 * 
 */
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import controller.Manager;
import log.LogKeeper;
import model.Order;
import misc.BILLCOLS;
import model.ItemList;
import model.OrderList;

/**
 * @author Rohit
 *
 */
public class ListOfOrdersPanel extends JPanel {

	/**
	 * 
	 */

	private OrderList orderList;
	private ItemList itemList;
	private DefaultTableModel orderModel;

	public ListOfOrdersPanel() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.orderModel = new DefaultTableModel(0, 8);
		preparePanels();
	}

	public void createLink(OrderList orderList, ItemList itemList) {
		this.orderList = orderList;
		this.itemList = itemList;

	}

	public void preparePanelForDisplay(){
		populateData();
		this.revalidate();
	}
	
	private void preparePanels() {

		JPanel liso = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
		JPanel lisobox = new JPanel();
		lisobox.setLayout(new BoxLayout(lisobox, BoxLayout.PAGE_AXIS));
		lisobox.setBorder(BorderFactory.createTitledBorder("Order Summary"));
		lisobox.setBackground(new Color(255, 255, 255));
		lisobox.setPreferredSize(new Dimension(950, 500));

		JTable orderTable = new JTable(orderModel);

		// prepare right alignment rendering for certain columns of table.
		DefaultTableCellRenderer rAlignRndr = new DefaultTableCellRenderer();
		rAlignRndr.setHorizontalAlignment(JLabel.RIGHT);
		// set table properties
		TableColumn column = orderTable.getColumnModel().getColumn(misc.BILLCOLS.ID.getValue());
		column.setHeaderValue(new String("Order"));
		column.setMinWidth(30);

		column = orderTable.getColumnModel().getColumn(1);
		column.setHeaderValue(new String("Customer"));
		column.setMinWidth(30);

		column = orderTable.getColumnModel().getColumn(2);
		column.setHeaderValue(new String("Item"));
		column.setMinWidth(120);

		column = orderTable.getColumnModel().getColumn(3);
		column.setHeaderValue(new String("Nos."));
		column.setCellRenderer(rAlignRndr);
		column.setMinWidth(30);

		column = orderTable.getColumnModel().getColumn(4);
		column.setHeaderValue(new String("Price"));
		column.setCellRenderer(rAlignRndr);
		column.setMinWidth(30);

		column = orderTable.getColumnModel().getColumn(5);
		column.setHeaderValue(new String("Discount"));
		column.setCellRenderer(rAlignRndr);
		column.setMinWidth(30);

		column = orderTable.getColumnModel().getColumn(6);
		column.setHeaderValue(new String("Total"));
		column.setCellRenderer(rAlignRndr);
		column.setMinWidth(50);

		column = orderTable.getColumnModel().getColumn(7);
		column.setHeaderValue(new String("TimeStamp"));
		column.setCellRenderer(rAlignRndr);
		column.setMinWidth(120);

		JScrollPane tableHolder = new JScrollPane(orderTable);

		lisobox.add(tableHolder);

		liso.add(lisobox);
		this.add(liso);
		this.revalidate();
		this.repaint();

	}

	/**
	 * This method populates the data from OrderList to create a table of orders to display
	 */
	private void populateData() {
		Collection<Order> orderTree;
		double order_price;

		int tcount = 0;

		try {
			orderTree = orderList.getOrderItems();
			if (orderTree == null) {
				Manager.dialog("No Orders in the OrderList", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Iterator<Order> orderIterator = orderTree.iterator();
			while (orderIterator.hasNext()) {
				Order oneOrder = orderIterator.next();
				order_price = itemList.findByName(oneOrder.getItem().toString()).getCost();

				orderModel.insertRow(tcount,
						new Object[] { oneOrder.getID(), oneOrder.getCustomer(), oneOrder.getItem(),
								oneOrder.getQuantity(), order_price, (oneOrder.getDiscount() * order_price),
								oneOrder.getTotal(), oneOrder.getTime() });
				tcount++;

			}
		} catch (Exception e) {
			LogKeeper.getInstance().addLog("Manager/ListOfOrder", "Error", e);
		}

	}

}
