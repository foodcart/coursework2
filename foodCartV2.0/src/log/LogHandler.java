package log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;

import controller.Manager;

public class LogHandler extends FileHandler {
	
	private String fullLog = new String();
	
	public String getLogText(){
		return fullLog;
	}

	public LogHandler() throws IOException, SecurityException {
		// TODO Auto-generated constructor stub
		super();
	}

	public LogHandler(String pattern) throws IOException, SecurityException {
		super(pattern);
		// TODO Auto-generated constructor stub
	}

	public LogHandler(String pattern, boolean append) throws IOException, SecurityException {
		super(pattern, append);
		// TODO Auto-generated constructor stub
	}

	public LogHandler(String pattern, int limit, int count) throws IOException, SecurityException {
		super(pattern, limit, count);
		// TODO Auto-generated constructor stub
	}

	public LogHandler(String pattern, int limit, int count, boolean append) throws IOException, SecurityException {
		super(pattern, limit, count, append);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public synchronized void publish(LogRecord record) {
		
		String message = null;
		
		super.publish(record);
		if (!isLoggable(record)) {
            return;}else{
            try {
            	message = getFormatter().format(record);
				fullLog = fullLog + message;
			} catch (Exception e) {
				// TODO: handle exception
			}
        }
	}

}
