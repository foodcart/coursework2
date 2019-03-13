package view;
/*
 * This is the Anchoring Panel. And will be the WEST in Window.
 * Other Panels called will be fitted into this one.
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class MainPanel extends JPanel {

	public MainPanel() {
		//super(new BorderLayout(2, 2));
		super( );
		super.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setSize(950,550);
		this.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		this.setPreferredSize(new Dimension(950, 525));
		this.setMaximumSize(new Dimension(950, 525));
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
