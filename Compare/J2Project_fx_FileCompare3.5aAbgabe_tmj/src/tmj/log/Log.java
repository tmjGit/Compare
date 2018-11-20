package tmj.log;

import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.writers.ConsoleWriter;
import org.pmw.tinylog.writers.FileWriter;
import org.pmw.tinylog.writers.SharedFileWriter;

import tmj.app.Application;
import tmj.system.System;

public class Log  {
	protected static Level metaLevel; // log Meta information if static debug Level is metaLevel or wider. Default is DEBUG, so we log Meta information for DEBUG and TRACE level.
	protected static String logMetaFormat,logFormat;
	
	public static void init() {
		// Internal logging of this log setup:
		Configurator.defaultConfig().level( Level.TRACE ).formatPattern( "{date:yyyy-MM-dd HH:mm:ss} {level} [{thread}] {class}.{method}: {message}" ).activate();
		
		Logger.trace("log.init...");
		String logfile=Application.confMainGet("logfilename");
		Logger.trace("logfile="+logfile);
		if (logfile.equals(""))
			logfile=Application.NAME+".log";
		Logger.trace("logfile="+logfile);
		String logpath=Application.confMainGet("logfilepath");
		if(logpath.equals(""))
			logpath=System.standardLogPath();
		logfile=logpath+logfile;
		Logger.trace("logfile="+logfile);
		Configurator.defaultConfig().writer(new SharedFileWriter(logfile, true)).activate(); 
		
		logMetaFormat=Application.confMainGet("logmetaformat");
		Logger.trace("logMetaFormat="+logMetaFormat);
		logFormat=Application.confMainGet("logformat");
		Logger.trace("logFormat="+logFormat);
		
		metaLevel = readLevel("logmetalevel",Level.DEBUG);
		Logger.trace("metaLevel="+metaLevel);
		setLevel(readLevel("loglevel",Level.WARNING));
		
		Configurator.currentConfig().addWriter(new ConsoleWriter()).activate();
	}

	private static Level readLevel(String confname,Level defaultL) {
		String s=Application.confMainGet(confname);
		Level l=defaultL;
		if(!s.equals("")) {
			try {
				l=Level.valueOf(s.toUpperCase());
			} catch (Exception e) {
			}
		}
		return l;
	}
	
	private static String logFormatString(Level level) {
		if(metaLevel.compareTo(level) >= 0)
			return logMetaFormat;
		else
			return logFormat;
	}
	
	public static void setLogFile(String logfile) {
		Configurator.currentConfig().writer(new FileWriter(logfile)).activate();
	}
	
	public static void setFormat(String formatPattern) {
		/* format pattern for log entries.
		 * default = "{date:yyyy-MM-dd HH:mm:ss} [{thread}] {class}.{method}()\n{level}: {message}"
		 * The date format pattern is compatible with SimpleDateFormat.
		 */
		Configurator.currentConfig().formatPattern(formatPattern).activate();
	}
	
	public static void setLevel(Level level) {
		Configurator.currentConfig().level(level).formatPattern( logFormatString(level) ).activate();
	}


}
