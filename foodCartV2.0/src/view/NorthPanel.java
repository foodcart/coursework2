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
	 * 
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

		newOrder = new JButton("New Order");
		newOrder.addActionListener(al);
		newOrder.setActionCommand("NEWO");
		actionBox.add(newOrder);

		showOrders = new JButton("Show Orders");
		showOrders.addActionListener(al);
		showOrders.setActionCommand("LISO");
		actionBox.add(showOrders);

		printSummary = new JButton("Generate Report");
		printSummary.addActionListener(al);
		printSummary.setActionCommand("SUMM");
		actionBox.add(printSummary);

		showQueue = new JButton("Show Queue");
		showQueue.addActionListener(al);
		showQueue.setActionCommand("SQUE");
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
