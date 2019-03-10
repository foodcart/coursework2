package log;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {
	
	public static final String fieldSeparator = new String("|");
	
	@Override
    public String format(LogRecord record) {
		String message = new String("");
		try{message = record.getThrown().toString();}catch (Exception e) {
			// TODO: handle exception
		}
        return new Date(record.getMillis())+fieldSeparator
                +record.getMessage()
                +message
                +System.lineSeparator() ;      	
    }


}
