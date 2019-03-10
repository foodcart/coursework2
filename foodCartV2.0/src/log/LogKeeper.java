package log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author Vimal This is the Singleton Logger. We use the java.util.logging
 *         classes to execute logging logger is the logging object, and
 *         logKeeper is the interface in FoodCart for logger Serializing is
 *         handled by the LogManager
 */
public class LogKeeper {

	private static Logger logger;
	private static LogKeeper logKeeper;

	public static LogKeeper getInstance(String jarDir, String propFile) throws LogManagerException {
		if (!(logKeeper == null)) {
			return logKeeper;
		} else {
			return logKeeper = new LogKeeper(jarDir, propFile);
		}
	}

	public static LogKeeper getInstance() {
		return logKeeper;
	}

	private LogKeeper(String jarDir, String propFile) throws LogManagerException {

		LogHandler fHandler;
		String pattern = jarDir + File.separator + "logs" + File.separator + "java%u.log";
		logger = Logger.getLogger("FoodCart");
		try {
			LogManager.getLogManager().readConfiguration(new FileInputStream(jarDir + File.separator + propFile));
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			throw new LogManagerException("Failed to read log properties file: " + propFile, e);
		}
		// overide properties to set Level to ALL now
		logger.setLevel(Level.ALL);
		// logs to console
		// logger.addHandler(new ConsoleHandler());
		// foodCart filelogging
		try {
			fHandler = new LogHandler(pattern);
		} catch (SecurityException | IOException e) {
			throw new LogManagerException("Failed to init FileHandler, pattern: " + pattern, e);
		}
		fHandler.setFormatter(new LogFormatter());
		logger.addHandler(fHandler);
	}

	/*
	 * This method adds a Fine level log entry. it calles the logger to add the
	 * entry We do some formatting here, rest is taken care by the LogFormatter.
	 */
	public void addLog(String actor, String message) {
		logger.log(Level.FINE, actor + LogFormatter.fieldSeparator + message);
	}

	/*
	 * This method adds a Severe level log entry. it calles the logger to add
	 * the entry We do some formatting here, rest is taken care by the
	 * LogFormatter.
	 */
	public void addLog(String actor, String message, Throwable thrown) {
		logger.log(Level.SEVERE, actor + LogFormatter.fieldSeparator + message, thrown);
	}
	
	public String getLogMessage(){
		Handler[] myHandlers = logger.getHandlers();
		LogHandler one = (LogHandler) myHandlers[0];
		return one.getLogText();
		
	}
}
