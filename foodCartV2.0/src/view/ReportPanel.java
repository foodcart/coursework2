/**
 * 
 */
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.LayoutManager;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import model.Item;
import model.ItemList;
import model.Order;
import misc.ReportData;
import model.ReportTableModel;
import model.OrderList;

/**
 * @author Anwar 
 *
 */
public class ReportPanel extends JPanel {

	/**
	 * the variables
	 */
	private OrderList orderList;
	private ItemList itemList;

	/**
	 * 
	 */
	public ReportPanel() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	public void createLink(OrderList orderList, ItemList itemList) {
		this.orderList = orderList;
		this.itemList = itemList;

	}
	/**
	 * This method gets the report ready to be attached to the
	 * JFrame calling it
	 */
	public void preparePanelForDisplay(){
		generateReport(false);
	}
	
	/**
	 * Call the report in a new JFrame. This call is expected
	 * in the system.exit of the Manager, after closing the current window.
	 */
	public void popupReport(){
		generateReport(true);
	}
	
	
	
	
	/**
	 * this is the private method  to create panels and labels for the report
	 * @param boolean => is call on window close event or not
	 */
	private void generateReport(boolean onClose) {
		misc.ReportData data;
		double totalrevenue = 0;
		double totaldiscount = 0;
		int totalorders = 0;
		Order oneOrder;
		// temp store for calculations
		Map<String, ReportData> store = new HashMap<String, ReportData>();
		// Item Model = {Item Code, Desc, Category, Price, No.of Orders, Total
		// Discount,
		// Total Revenue}
		DefaultTableModel itemModel = new ReportTableModel(0, 7);
		Collection<Order> orderTree = orderList.getOrderItems();
		Iterator<Order> iterator = orderTree.iterator();
		while (iterator.hasNext()) {
			oneOrder = iterator.next();
			if (store.containsKey(oneOrder.getItem())) {
				data = store.get(oneOrder.getItem());
				data.orderCount++;
				data.revenue += oneOrder.getTotal();
				data.totalDiscount += (oneOrder.getDiscount() * data.price);
			} else {
				data = new ReportData();
				try {
					Item item = itemList.findByName(oneOrder.getItem());
					data.price = item.getCost();
					data.category = item.getCategory();
					data.id = item.getId();
				} catch (Exception e) {
					// TODO: handle exception
				}
				data.revenue = oneOrder.getTotal();
				data.totalDiscount = (oneOrder.getDiscount() * data.price);
				data.orderCount = 1;
				store.put(oneOrder.getItem(), data);
			}
		}

		int row = 0;
		for (Entry<String, ReportData> entry : store.entrySet()) {
			data = entry.getValue();
			itemModel.insertRow(row, new Object[] { data.id, entry.getKey(), data.category, data.price, data.orderCount,
					data.totalDiscount, data.revenue });
			totalrevenue += data.revenue;
			totalorders += data.orderCount;
			totaldiscount += data.totalDiscount;
		}

		Color white = new Color(255, 255, 255);

		// { "ID", "Item Desc.", "Category", "Price", "Orders", "Discount",
		// "Revenue" };
		JTable itemReport = new JTable(itemModel);
		itemReport.setAutoCreateRowSorter(true);
		itemReport.setBackground(white);

		TableRowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>(itemReport.getModel());
		itemReport.setRowSorter(rowSorter);

		ArrayList<RowSorter.SortKey> sortKeyList = new ArrayList<>(25);
		sortKeyList.add(new RowSorter.SortKey(6, SortOrder.DESCENDING));
		sortKeyList.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
		rowSorter.setSortKeys(sortKeyList);

		// prepare right alignment rendering for certain columns of table.
		DefaultTableCellRenderer rAlignRndr = new DefaultTableCellRenderer();
		rAlignRndr.setHorizontalAlignment(JLabel.RIGHT);

		// set table properties
		TableColumn column = itemReport.getColumnModel().getColumn(0);
		column.setHeaderValue(new String("ID"));
		column.setMinWidth(30);

		column = itemReport.getColumnModel().getColumn(1);
		column.setHeaderValue(new String("Item Desc"));
		column.setMinWidth(120);

		column = itemReport.getColumnModel().getColumn(2);
		column.setHeaderValue(new String("Category"));
		column.setCellRenderer(rAlignRndr);
		column.setMinWidth(50);

		column = itemReport.getColumnModel().getColumn(3);
		column.setHeaderValue(new String("Price(AED)"));
		column.setCellRenderer(rAlignRndr);
		column.setMinWidth(60);

		column = itemReport.getColumnModel().getColumn(4);
		column.setHeaderValue(new String("Orders"));
		column.setCellRenderer(rAlignRndr);
		column.setMinWidth(60);

		column = itemReport.getColumnModel().getColumn(5);
		column.setHeaderValue(new String("Discount(AED)"));
		column.setCellRenderer(rAlignRndr);
		column.setMinWidth(60);

		column = itemReport.getColumnModel().getColumn(6);
		column.setHeaderValue(new String("Revenue(AED)"));
		column.setCellRenderer(rAlignRndr);
		column.setMinWidth(60);

		JScrollPane reportHolder = new JScrollPane(itemReport);
		reportHolder.setBackground(white);

		JPanel report = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		JPanel reportBox = new JPanel();
		reportBox.setLayout(new BoxLayout(reportBox, BoxLayout.PAGE_AXIS));
		reportBox.setBorder(BorderFactory.createTitledBorder("Report"));
		reportBox.setBackground(white);
		reportBox.setPreferredSize(new Dimension(950, 500));
		reportBox.setMinimumSize(new Dimension(950, 500));

		JLabel Ldate = new JLabel("Report generated on: " + DateFormat.getDateTimeInstance().format(new Date()));
		Ldate.setHorizontalAlignment(JLabel.LEFT);
		JLabel headLabel = new JLabel("Sales Summary");
		headLabel.setHorizontalAlignment(JLabel.LEFT);
		// Set a bigger possible font for the heading
		headLabel.setFont(new Font(headLabel.getFont().getName(), Font.BOLD, 12));
		JPanel headHolder = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		headHolder.setBackground(new Color(255, 255, 255));
		headHolder.add(headLabel);
		JPanel timeHolder = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		timeHolder.setBackground(new Color(255, 255, 255));
		timeHolder.add(Ldate);

		reportBox.add(headHolder);
		reportBox.add(timeHolder);

		JPanel insightPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		insightPane.setBackground(new Color(255, 255, 255));
		JLabel spacer1 = new JLabel("   ");
		insightPane.add(spacer1);
		reportBox.add(insightPane);

		insightPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		insightPane.setBackground(new Color(255, 255, 255));
		spacer1 = new JLabel("Aggregates:");
		spacer1.setFont(new Font(headLabel.getFont().getName(), Font.BOLD + Font.ITALIC, 11));
		insightPane.add(spacer1);
		reportBox.add(insightPane);

		JLabel torders = new JLabel("Total Orders	:	" + Integer.toString(totalorders));
		insightPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		insightPane.setBackground(new Color(255, 255, 255));
		insightPane.add(torders);
		reportBox.add(insightPane);

		torders = new JLabel("Total Discounts	:	" + "AED " + Double.toString(totaldiscount));
		insightPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		insightPane.setBackground(new Color(255, 255, 255));
		insightPane.add(torders);
		reportBox.add(insightPane);

		torders = new JLabel("Total Revenue	:	" + "AED " + Double.toString(totalrevenue));
		insightPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		insightPane.setBackground(new Color(255, 255, 255));
		insightPane.add(torders);
		reportBox.add(insightPane);

		insightPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		insightPane.setBackground(new Color(255, 255, 255));
		spacer1 = new JLabel(" ");
		insightPane.add(spacer1);
		reportBox.add(insightPane);

		insightPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		insightPane.setBackground(new Color(255, 255, 255));
		spacer1 = new JLabel("Items:");
		spacer1.setFont(new Font(headLabel.getFont().getName(), Font.BOLD + Font.ITALIC, 11));
		insightPane.add(spacer1);
		reportBox.add(insightPane);

		reportBox.add(reportHolder);

		report.add(reportBox);
		report.setBackground(white);

		if (!onClose) {
			this.removeAll();
			this.add(report);
			this.repaint();
		} else {
			JFrame popup = new JFrame("Group 6 Coffee Shop - Sales Report");
			popup.setSize(900, 600);// 1200 width : 600 height
			popup.setLocationRelativeTo(null);// to set to center
			popup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			popup.getContentPane().add(report);
			popup.pack();
			popup.setVisible(true);

		}

	}
}
