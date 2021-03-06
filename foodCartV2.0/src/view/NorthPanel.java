package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import view.ClockPanel;

public class NorthPanel extends JPanel {

	/**
	 * @author Rohith
	 */
	private static final long serialVersionUID = -2694507633278988462L;

	private JButton newOrder;
	private JButton showOrders;
	private JButton printSummary;
	private JButton showQueue;

	public NorthPanel(ActionListener al) {
		super(new GridLayout(0, 1));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		// Add the header
		JLabel headLabel = new JLabel("Group 6 Coffee Shop");
		headLabel.setHorizontalAlignment(JLabel.CENTER);
		// Set a bigger possible font for the heading
		headLabel.setFont(new Font(headLabel.getFont().getName(), Font.CENTER_BASELINE, 14));
		this.add(headLabel);
		JPanel line1 = new JPanel(new GridLayout(0, 1));
		line1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10),
				BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLUE)));
		// Also add a clock
		line1.add(new ClockPanel());
		this.add(line1);

		// add the buttons
		JPanel actionBox = new JPanel(new FlowLayout(FlowLayout.CENTER));
		/*Button to start taking new Order*/
		newOrder = new JButton("New Order");
		newOrder.addActionListener(al);
		newOrder.setActionCommand("NEWO");
		newOrder.setToolTipText("Take New Order");
		actionBox.add(newOrder);
		
		/*Button for Display Order List*/
		showOrders = new JButton("Orders List");
		showOrders.addActionListener(al);
		showOrders.setActionCommand("LISO");
		showOrders.setToolTipText("View List of all Orders");
		actionBox.add(showOrders);
		
		/*Button for Sales Report*/
		printSummary = new JButton("Sales Report");
		printSummary.addActionListener(al);
		printSummary.setActionCommand("SUMM");
		printSummary.setToolTipText("Generate Sales Report");
		actionBox.add(printSummary);
		
		/*Add the button for showing shopping queue*/
		showQueue = new JButton("Shop Queue");
		showQueue.addActionListener(al);
		showQueue.setActionCommand("SQUE");
		printSummary.setToolTipText("View Shop Queue");
		actionBox.add(showQueue);
		
		/*button for display log*/
		showQueue = new JButton("Show Log");
		showQueue.addActionListener(al);
		showQueue.setActionCommand("SLOG");
		printSummary.setToolTipText("Display Application Log");
		actionBox.add(showQueue);
		// add actionbox to the Panel
		this.add(actionBox);
	}

	public void setButtonState(String buttonName, Boolean b) {
		switch (buttonName) {
		case "new":
			newOrder.setEnabled(b);
			break;
		case "order":
			showOrders.setEnabled(b);
			break;
		case "print":
			printSummary.setEnabled(b);
			break;
		case "queue":
			showQueue.setEnabled(b);
			break;
		}

	}

	public NorthPanel() {
		super();
	}

	public NorthPanel(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	public NorthPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public NorthPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

}
