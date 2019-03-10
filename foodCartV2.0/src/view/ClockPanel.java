package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.util.Date;

class ClockPanel extends JPanel {

	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel clock = new JLabel();

	  public ClockPanel() {
	    setLayout(new BorderLayout());
	    updateTime();
	    add(clock);
	    Timer timer = new Timer(500, new ActionListener() {
	      @Override
	      public void actionPerformed(ActionEvent e) {
	    	  updateTime();
	      }
	    });
	    timer.setRepeats(true);
	    timer.setCoalesce(true);
	    timer.setInitialDelay(0);
	    timer.start();
	  }

	  public void updateTime() {
	    clock.setText(DateFormat.getDateTimeInstance().format(new Date()));
	  }
}  