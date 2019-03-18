package view;

/*
 * This is the order taking Panel
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import controller.Manager;
import model.Order;
import model.OrderList;
import model.OrderQueue;
import log.LogKeeper;
import misc.BILLCOLS;
import misc.TABCOLS;
import misc.WorkingOrder;
import model.Item;
import model.ItemList;

public class OrderPanel extends JPanel {
/**
 * @author Anwar
 */
	
	private String myName = new String("Manager/OP");
	private Map<String, String> menuItems;
	private Map<String, String> menuCategories;
	private ItemList itemList;
	private JPanel westPane;
	private Item chosenItem;
	private ButtonGroup itemGrp;
	private ButtonGroup catGrp;
	private JPanel itemPanel;
	private JPanel panelCentre;
	private JPanel billContainer;
	private JButton removeItem;
	private JButton addItem;
	private JButton commitOrder;
	private volatile DefaultTableModel model;
	private ActionListener menuActionListener;
	private JTable oneOrder;

	// copy of shopping queue
	private OrderQueue shopQueue;

	// copy of OrderList
	private OrderList orderList;

	public OrderPanel() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		// initialize the model
		model = new DefaultTableModel(0, 5);
		menuActionListener = new menuActionListener();
		// create instance of menu Action listener.
		initPanels();
		setDefaultDisplay();

	}

	public void setDefaultViews() {
		try {
			if(model.getRowCount() > 0)
				for(int i = model.getRowCount()-1; i> -1; i--){
					model.removeRow(i);
				}
		} catch (Exception e) {
			// do nothing, as you can not remove it if it was not there.
		}

		setDefaultDisplay();
		paintItems();
	}

	public void createLink(ItemList itemList, OrderQueue shopQueue, OrderList orderList) {
		this.itemList = itemList;
		this.menuCategories = itemList.getMenuCategories();
		this.shopQueue = shopQueue;
		this.orderList = orderList;
		paintItems();
	}

	public OrderPanel(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	public OrderPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public OrderPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	private void initPanels() {

		prepareWestPanel();
		prepareCentrePanel();

	}

	private void setDefaultDisplay() {
		this.removeAll();
		this.add(westPane);
		this.add(panelCentre);
		this.validate();
		this.repaint();
	}

	private void prepareWestPanel() {
		westPane = new JPanel(new GridLayout(1, 2));
		westPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		int cpHeight = westPane.getHeight();
		westPane.getBounds().setSize(20, cpHeight);
		westPane.setPreferredSize(new Dimension(400, 100));
	}

	private void paintItems() {

		westPane.removeAll();
		JPanel catPanel = new JPanel(new GridLayout(15, 1, 5, 0));
		catPanel.setBorder(BorderFactory.createTitledBorder("Categories"));
		catGrp = new ButtonGroup();
		// Instance of Category Rabdiobutton action listener
		ActionListener catActionListener = new crActionListener();
		// add radio buttons to button group catGrp.
		JRadioButton rbCategoryPlaceholder;
		for (Entry<String, String> entry : menuCategories.entrySet()) {
			rbCategoryPlaceholder = new JRadioButton(entry.getValue());
			rbCategoryPlaceholder.addActionListener(catActionListener);
			catGrp.add(rbCategoryPlaceholder);
			catPanel.add(rbCategoryPlaceholder);

		}
		westPane.add(catPanel);
		// add the MenuItems Panel
		itemGrp = new ButtonGroup();
		setMenuButtons("");
		westPane.add(itemPanel);
		westPane.revalidate();
		westPane.repaint();
	}

	private void setMenuButtons(String Category) {

		String chosenCategory = Category;

		// new JPanel for Radiobuttons of Menu
		itemPanel = new JPanel(new GridLayout(15, 1, 5, 0));
		itemPanel.setBorder(BorderFactory.createTitledBorder("Menu Items"));
		// get the menu items in the chosen category
		menuItems = itemList.getMenuItems(chosenCategory);
		// start creating radio buttons in the itemPanel
		JRadioButton rbMenuPlaceHolder;
		try {
			for (Entry<String, String> entry : menuItems.entrySet()) {
				rbMenuPlaceHolder = new JRadioButton(entry.getValue());
				rbMenuPlaceHolder.setActionCommand(entry.getKey());
				rbMenuPlaceHolder.addActionListener(menuActionListener);
				itemGrp.add(rbMenuPlaceHolder);
				itemPanel.add(rbMenuPlaceHolder);
			}
		} catch (Exception e) {
			// reportException(e, "setMenuButtons(Category = " + Category +
			// ")");
		}
	}

	private void prepareCentrePanel() {

		panelCentre = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 2));
		JPanel leftPane = new JPanel();
		leftPane.setLayout(new BoxLayout(leftPane, BoxLayout.PAGE_AXIS));
		JLabel spacer1 = new JLabel(" ");
		spacer1.setMinimumSize(new Dimension(120, 10));
		spacer1.setMaximumSize(new Dimension(120, 10));
		leftPane.add(spacer1);

		addItem = new JButton("Add Item ->");
		addItem.setMaximumSize(new Dimension(120, 25));
		addItem.addActionListener(menuActionListener);
		addItem.setActionCommand("ADDI");
		leftPane.add(addItem);

		JLabel spacer2 = new JLabel(" ");
		spacer2.setMinimumSize(new Dimension(120, 20));
		spacer2.setMaximumSize(new Dimension(120, 20));
		leftPane.add(spacer2);

		removeItem = new JButton("<- Remove Item");
		removeItem.setMaximumSize(new Dimension(120, 25));
		removeItem.addActionListener(menuActionListener);
		removeItem.setActionCommand("REMI");
		leftPane.add(removeItem);

		panelCentre.add(leftPane);

		JPanel rightPane = new JPanel();
		rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.PAGE_AXIS));
		//rightPane.setMaximumSize(new Dimension(300, 400));

		// the table
		oneOrder = new JTable(model);
		oneOrder.setShowGrid(false);

		// prepare right alignment rendering for certain columns of table.
		DefaultTableCellRenderer rAlignRndr = new DefaultTableCellRenderer();
		rAlignRndr.setHorizontalAlignment(JLabel.RIGHT);

		// set table properties
		TableColumn column = oneOrder.getColumnModel().getColumn(0);
		column.setHeaderValue(new String("#"));
		column.setMinWidth(30);

		column = oneOrder.getColumnModel().getColumn(1);
		column.setHeaderValue(new String("Item"));
		column.setMinWidth(120);

		column = oneOrder.getColumnModel().getColumn(2);
		column.setHeaderValue(new String("Nos."));
		column.setCellRenderer(rAlignRndr);
		column.setMinWidth(50);

		column = oneOrder.getColumnModel().getColumn(3);
		column.setHeaderValue(new String("Price/Unit(AED)"));
		column.setCellRenderer(rAlignRndr);
		column.setMinWidth(100);

		JScrollPane tableHolder = new JScrollPane(oneOrder);
		tableHolder.setBorder(BorderFactory.createTitledBorder("Order"));
		//tableHolder.setMaximumSize(new Dimension(300, 400));
		rightPane.add(tableHolder);

		commitOrder = new JButton("        Generate Bill        ");
		commitOrder.setMaximumSize(new Dimension(300, 25));
		commitOrder.addActionListener(menuActionListener);
		commitOrder.setActionCommand("BILL");

		JPanel billButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));

		// billButtons.add(doTotal);
		billButtons.add(commitOrder);

		rightPane.add(billButtons);

		rightPane.setPreferredSize(new Dimension(300, 400));

		panelCentre.add(rightPane);

		panelCentre.setPreferredSize(new Dimension(430, 400));
	}

	/**
	 * Remove item from model
	 */
	private void removeItem() {
		try {
			int selectedRowID = oneOrder.getSelectedRow();
			if (selectedRowID >= 0) {
				model.removeRow(selectedRowID);
			} else {
				Manager.dialog("Please select an item to remove", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception e) {
			LogKeeper.getInstance().addLog(myName, "Error in Remove Item", e);
		}
	}

	/**
	 * add item to model
	 */
	private void addItem() {
		String colVal;
		boolean exists = false;
		int counter = 0;
		int quantity = 0;
		// get the item count, and add to it
		int count = model.getRowCount();
		try {
			if (!(chosenItem == null)) {
				if (!(count == 0)) {
					// check if this item is already there in the list
					for (int row = 0; row < model.getRowCount(); row++) {
						// check if an item with same description exists
						if (chosenItem.getId().equals(model.getValueAt(row, 4))) {
							// found item, just increment count and price.
							exists = true;
							// get quantity
							colVal = model.getValueAt(row, 2).toString();
							quantity = Integer.parseInt(colVal) + 1;
							model.setValueAt(quantity, row, 2);
						}
					}
					if (exists == false) {
						// this is a new item
						colVal = model.getValueAt(count - 1, 0).toString();
						counter = Integer.parseInt(colVal);
					}
				} else {
					// first item,do nothing here
				}
				// New Item, increment counters and add it here
				if (exists == false) {
					counter++;
					quantity = 1; // default quantity
					model.insertRow(count, new Object[] { counter, chosenItem.getDescription(), quantity,
							chosenItem.getCost(), chosenItem.getId() });
				}
			} else {
				Manager.dialog("Please select an item from the Menu", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception e) {
			LogKeeper.getInstance().addLog(myName, "Error in Add Item", e);
		}

	}



	/*
	 * 
	 */
	private void prepareBillPanel() {
		billContainer = new JPanel();
		billContainer.setLayout(new BoxLayout(billContainer, BoxLayout.PAGE_AXIS));
		billContainer.setBorder(BorderFactory.createTitledBorder("Customer Bill"));
		billContainer.setBackground(new Color(255, 255, 255));
		billContainer.setPreferredSize(new Dimension(500, 410));
	}

	/*
	 * 
	 */
	private void generateBill() {
		if (!(model.getRowCount() > 0)) {
			Manager.dialog("Please add items to the Order list before generating bill", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		prepareBillPanel();
		WorkingOrder wo;
		int quantity = 0;
		int counter = 0;
		int counter1 = 0;
		int counter3 = 0;
		// temp container for orders - category c1 and c2
		Map<Integer, WorkingOrder> c1 = new HashMap<Integer, WorkingOrder>();
		// temp container for orders - category c1 and c3
		Map<Integer, WorkingOrder> c3 = new HashMap<Integer, WorkingOrder>();
		// temp container after applying discount
		Map<Integer, WorkingOrder> currOrder = new HashMap<Integer, WorkingOrder>();
		try {
			// add all orders in Model to C1, C2 or C3
			for (int row = 0; row < model.getRowCount(); row++) {
				quantity = Integer.parseInt(model.getValueAt(row, TABCOLS.QUAN.getValue()).toString());
				while (quantity > 0) {
					wo = new WorkingOrder();
					wo.item = model.getValueAt(row, TABCOLS.DESC.getValue()).toString();
					wo.category = model.getValueAt(row, TABCOLS.ID.getValue()).toString();
					wo.category = wo.category.substring(wo.category.length() - 2);
					wo.price = Double.parseDouble(model.getValueAt(row, TABCOLS.PRICE.getValue()).toString());
					wo.quantity = 1;
					wo.discount = 0;
					if (wo.category.equals("C1") || wo.category.equals("C2")) {
						c1.put(counter1, wo);
						counter1++;
					} else {
						if (wo.category.equals("C3")) {
							c3.put(counter3, wo);
							counter3++;
						}
					}

					quantity--;

				}

			}
		} catch (Exception e) {
			// unknown exception, so report this
			LogKeeper.getInstance().addLog(myName + "/generateBill", "Error while Generating Bill", e);
		}
		/* apply discount. Any C1/C2 + C3 gets a 20% discount on C3. */
		/* we know we have number of items = counter . */
		counter = 0;
		WorkingOrder c3wo;
		WorkingOrder cwo;
		boolean isFound = false;
		// process C3 first
		for (Entry<Integer, WorkingOrder> c3entry : c3.entrySet()) {
			c3wo = c3entry.getValue();
			// if any c1 or c 2 item is available, give 20% discount on C3
			if (c1.size() > 0) {
				// move one of the c1 items to currOrder, and remove it from c1.
				for (Entry<Integer, WorkingOrder> entry : c1.entrySet()) {
					cwo = entry.getValue();
					// check if current order has same entry, then update
					// quantity, else put
					isFound = false;
					// find and update.
					for (Entry<Integer, WorkingOrder> coentry : currOrder.entrySet()) {
						if (coentry.getValue().item.equals(cwo.item)) {
							isFound = true;
							int cic = coentry.getKey();
							WorkingOrder ciwo = coentry.getValue();
							ciwo.quantity = ciwo.quantity + cwo.quantity;
							currOrder.remove(cic);
							currOrder.put(cic, ciwo);// update new quantity
							break;
						}

					}
					if (!isFound) {
						// not found so increment counter and add items
						currOrder.put(counter, cwo);
						counter++;
					}
					// in either case above, we remove item from c1
					c1.remove(entry.getKey());
					break;
				}
				// add the discounted item
				c3wo.discount = 0.2; // 20% discount.
				currOrder.put(counter, c3wo);// updated
				counter++;
			} else {
				// add the non discounted item
				isFound = false;
				// if found increment quantity
				for (Entry<Integer, WorkingOrder> coentry : currOrder.entrySet()) {
					if (coentry.getValue().item.equals(c3wo.item) && (coentry.getValue().discount == c3wo.discount)) {
						isFound = true;
						int cic = coentry.getKey();
						WorkingOrder ciwo = coentry.getValue();
						ciwo.quantity = ciwo.quantity + c3wo.quantity;
						currOrder.remove(cic);
						currOrder.put(cic, ciwo);// update new quantity
						break;
					}
				}
				if (!isFound) {
					// not found so increment counter and add items
					currOrder.put(counter, c3wo);
					counter++;
				}

			}

		}
		// now process any remaining c1.
		for (Entry<Integer, WorkingOrder> entry : c1.entrySet()) {
			cwo = entry.getValue();
			isFound = false;
			// if item already there, increment quantity
			for (Entry<Integer, WorkingOrder> coentry : currOrder.entrySet()) {
				if (coentry.getValue().item.equals(cwo.item)) {
					isFound = true;
					int cic = coentry.getKey();
					WorkingOrder ciwo = coentry.getValue();
					// if required, we can add a buy 1, get one free discount
					// here, but not now.
					// we increment quantity for now
					ciwo.quantity = ciwo.quantity + cwo.quantity;
					currOrder.remove(cic);
					currOrder.put(cic, ciwo);// update new quantity
					break;
				}
			}
			if (!isFound) {
				// not found so increment counter and add items
				currOrder.put(counter, cwo);
				counter++;
			}
		}
		DefaultTableModel billModel = new javax.swing.table.DefaultTableModel(0, 6);
		Order order;
		int modelrow = 0;
		// currOrder has the final items, now update to OrderList
		double total = 0;
		double grandTotal = 0;
		double order_price = 0;
		// get the last customer in orderlist, and increment
		int customer = orderList.getLastCustomer() + 1;
		for (Entry<Integer, WorkingOrder> coentry : currOrder.entrySet()) {
			order_price = coentry.getValue().price;
			total = (coentry.getValue().price - (coentry.getValue().price * coentry.getValue().discount))
					* coentry.getValue().quantity;
			// keep grandtotal
			grandTotal += total;
			try {
				order = orderList.add(customer, coentry.getValue().item, coentry.getValue().quantity,
						coentry.getValue().discount, total);
				// get price from menulist
				billModel.insertRow(modelrow, new Object[] { Integer.toString(order.getID()), order.getItem(),
						Integer.toString(order.getQuantity()), Double.toString(order_price),
						Double.toString((order.getDiscount() * order_price)), Double.toString(order.getTotal()) });
				modelrow++;
			} catch (Exception e) {
				LogKeeper.getInstance().addLog(myName + "generateBill", "Error in buildModel", e);
			}
		}
		// orders added, also update the shopqueue
		updateShopQueue(customer);
		// add grand total
		if (billModel.getRowCount() > 0) {
			billModel.insertRow(modelrow,
					new Object[] { "*", "Grand Total", "*", "*", "*", Double.toString(grandTotal) });
		}
		// prepare the bill header.
		createBillHeader(customer);
		// add the orders table
		createBillTable(billModel);
		try {
			this.removeAll();
			this.add(billContainer);
			this.revalidate();
			this.repaint();
		} catch (Exception e) {
			LogKeeper.getInstance().addLog(myName + "generateBill", "Error in billDisplay", e);
		}

	}

	/*
	 * This method updates the Shop Queue with new Order
	 */
	private void updateShopQueue(Integer customer) {
		try {
			ArrayList<Order> orderArray = new ArrayList<Order>();
			Map<Integer, Order> orders = orderList.getOrderItems(customer.intValue());

			for (Entry<Integer, Order> entry : orders.entrySet()) {
				orderArray.add(entry.getValue());
			}
			if (orderArray.size() > 0) {
				Thread.currentThread().setName(myName);
				shopQueue.add(customer, orderArray.toArray(new Order[orderArray.size()]));
			}
		} catch (Exception e1) {
			LogKeeper.getInstance().addLog(myName + "generateBill", "Error in appending shopQueue", e1);
		}
	}

	/*
	 * 
	 */
	private void createBillHeader(Integer customer) {
		JLabel Ldate = new JLabel(DateFormat.getDateTimeInstance().format(new Date()));
		JLabel headLabel = new JLabel("Group 6 Coffee Shop");
		headLabel.setHorizontalAlignment(JLabel.CENTER);
		// Set a bigger possible font for the heading
		headLabel.setFont(new Font(headLabel.getFont().getName(), Font.CENTER_BASELINE, 12));
		JPanel headHolder = new JPanel();
		headHolder.setMaximumSize(new Dimension(340, 25));
		headHolder.setAlignmentX(Component.CENTER_ALIGNMENT);
		headHolder.add(headLabel);
		JPanel timeHolder = new JPanel();
		timeHolder.setMaximumSize(new Dimension(340, 20));
		timeHolder.setBackground(new Color(255, 255, 255));
		timeHolder.setAlignmentX(Component.CENTER_ALIGNMENT);
		timeHolder.add(Ldate);

		JPanel CustomerPane = new JPanel(new GridLayout(1, 2));
		CustomerPane.setBackground(new Color(255, 255, 255));
		CustomerPane.setMaximumSize(new Dimension(340, 40));
		JLabel CustLabel = new JLabel("Customer:");
		JLabel CustID = new JLabel(Integer.toString(customer));
		CustID.setFont(new Font(headLabel.getFont().getName(), Font.CENTER_BASELINE, 20));
		CustomerPane.add(CustLabel);
		CustomerPane.add(CustID);
		try {
			// clear any existing bill on screen
			billContainer.removeAll();
			// build the bill screen
			billContainer.add(headHolder);
			billContainer.add(timeHolder);
			billContainer.add(CustomerPane);
		} catch (Exception e) {
			LogKeeper.getInstance().addLog(myName + "generateBill", "Error in Update GUI", e);
		}
	}

	/*
	 * 
	 */

	private void createBillTable(DefaultTableModel billModel) {
		try {
			JTable oneBill = new JTable(billModel);
			oneBill.setShowGrid(false);

			// prepare right alignment rendering for certain columns of table.
			DefaultTableCellRenderer rAlignRndr = new DefaultTableCellRenderer();
			rAlignRndr.setHorizontalAlignment(JLabel.RIGHT);
			// set table properties
			TableColumn column = oneBill.getColumnModel().getColumn(BILLCOLS.ID.getValue());
			column.setHeaderValue(new String("Order"));
			column.setMinWidth(30);

			column = oneBill.getColumnModel().getColumn(BILLCOLS.NAME.getValue());
			column.setHeaderValue(new String("Item"));
			column.setMinWidth(120);

			column = oneBill.getColumnModel().getColumn(BILLCOLS.QUANTITY.getValue());
			column.setHeaderValue(new String("Nos."));
			column.setCellRenderer(rAlignRndr);
			column.setMinWidth(50);

			column = oneBill.getColumnModel().getColumn(BILLCOLS.PRICE.getValue());
			column.setHeaderValue(new String("Price(AED)"));
			column.setCellRenderer(rAlignRndr);
			column.setMinWidth(60);

			column = oneBill.getColumnModel().getColumn(BILLCOLS.DISCOUNT.getValue());
			column.setHeaderValue(new String("Discount(AED)"));
			column.setCellRenderer(rAlignRndr);
			column.setMinWidth(60);

			column = oneBill.getColumnModel().getColumn(BILLCOLS.TOTAL.getValue());
			column.setHeaderValue(new String("Total(AED)"));
			column.setCellRenderer(rAlignRndr);
			column.setMinWidth(60);

			JScrollPane billHolder = new JScrollPane(oneBill);
			billHolder.setBorder(BorderFactory.createTitledBorder("Orders"));
			billContainer.add(billHolder);

		} catch (Exception e) {
			LogKeeper.getInstance().addLog(myName + "generateBill", "Error in buildTable", e);

		}
	}

	/*
	 * sub class actionListener for Category
	 */
	class crActionListener implements ActionListener {
		@Override
		/** Listens to the radio buttons in Category. */
		public void actionPerformed(ActionEvent e) {
			chosenItem = null;
			westPane.remove(itemPanel);
			setMenuButtons(e.getActionCommand());
			westPane.add(itemPanel);
			westPane.revalidate();
			westPane.repaint();
		}

	}

	/*
	 * 
	 */
	class menuActionListener implements ActionListener {
		@Override
		/** Listens to the radio buttons in Menu Items. */
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("REMI")) {
				// "remove item"
				removeItem();
			} else if (e.getActionCommand().equals("ADDI")) {
				// add item
				addItem();
			} else if (e.getActionCommand().equals("BILL")) {
				generateBill();
			} else
				chosenItem = itemList.findByID(e.getActionCommand());
		}

	}

}
