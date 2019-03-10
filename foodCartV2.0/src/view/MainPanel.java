package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class MainPanel extends JPanel {

	public MainPanel() {
		//super(new BorderLayout(2, 2));
		super(new GridLayout());
		this.setSize(950,600);
		this.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		this.setPreferredSize(new Dimension(950, 600));
		this.setMaximumSize(new Dimension(950, 600));
	}

	public MainPanel(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	public MainPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public MainPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

}
