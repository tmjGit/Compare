package li.tmj.log;

import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.writers.ConsoleWriter;
import org.pmw.tinylog.writers.FileWriter;
import org.pmw.tinylog.writers.SharedFileWriter;
import li.tmj.app.Application;
import li.tmj.conf.ConfigEntries;
import li.tmj.system.System;

public class Log  {
	protected static Level metaLevel; // log Meta information if static debug Level is metaLevel or wider. Default is DEBUG, so we log Meta information for DEBUG and TRACE level.
	protected static String logMetaFormat,logFormat;
	
	public static void init() {
		// Internal logging of this log setup:
		Configurator.defaultConfig().level( Level.TRACE ).formatPattern( "{date:yyyy-MM-dd HH:mm:ss} {level} [{thread}] {class}.{method}: {message}" ).activate();
		
		Logger.trace("log.init...");
		String logfile=ConfigEntries.logfilename();
		Logger.trace("logfile={}",logfile);
		if (logfile.equals(""))
			logfile=Application.NAME+".log";
		Logger.trace("logfile={}",logfile);
		String logpath=ConfigEntries.logfilepath();
		if(logpath.equals("")) {
			logpath=System.standardLogPath();
		}
		logfile=logpath+logfile;
		Logger.trace("logfile={}",logfile);
		Configurator.defaultConfig().writer(new SharedFileWriter(logfile, true)).activate(); 
//		Configurator.defaultConfig()    .writer(new FileWriter( logfile ))      .activate(); 
		
		logMetaFormat=ConfigEntries.logmetaformat();
		Logger.trace("logMetaFormat={}",logMetaFormat);
		logFormat=ConfigEntries.logformat();
		Logger.trace("logFormat={}",logFormat);
		
		metaLevel = readLevel(ConfigEntries.logmetalevel(),Level.DEBUG);
		Logger.trace("metaLevel="+metaLevel);
//		setMetaLevel(Level.DEBUG);// log Meta information if static debug Level is metaLevel or wider. Default is DEBUG, so we log Meta information for DEBUG and TRACE level.
		setLevel(readLevel(ConfigEntries.loglevel(),Level.WARNING));
		
		Configurator.currentConfig().addWriter(new ConsoleWriter()).activate();
//		Logger.error("Program \"{}\" launching, log initialized, logfile=\"{}\", level={}", Project.APPLICATION_NAME, logfile,Logger.getLevel()); // TODO debug
	}

	private static Level readLevel(String level,Level defaultL) {
//		String level=Application.confMainGet(confname);
		Level l=defaultL;
		if(!level.equals("")) {
			try {
				l=Level.valueOf(level.toUpperCase());
			} catch (Exception e) {
			}
		}
		return l;
	}
	
//	public static boolean logMeta() {
//		return (metaLevel.compareTo(Logger.getLevel()) >= 0);
//	}
//	
//	private static boolean logMeta(Level level) {
//		return (metaLevel.compareTo(level) >= 0);
//	}
	
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
		
//		public void setMetaLevel(Level metaLevel) {
//			Log.metaLevel=metaLevel;
//		}
//		
//		public Level getMetaLevel() {
//			return Log.metaLevel;
//		}

	}
