package log;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {
	
	public static final String fieldSeparator = new String("|");
	
	@Override
    public String format(LogRecord record) {
        return //record.getThreadID()+"::"+record.getSourceClassName()+"::"
                //+record.getSourceMethodName()+"::"
                new Date(record.getMillis())+fieldSeparator
                +record.getMessage()+ System.lineSeparator() ;
    }


}
