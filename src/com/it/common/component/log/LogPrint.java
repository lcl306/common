package com.it.common.component.log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * print application logs, configuration in log4j.properties
 * @author liu
 * */
public class LogPrint {

	private static Logger logger;

	static{
		logger = Logger.getLogger("appLog");
	}

	public static void debug(String str){
		logger.debug(str);
	}

	public static void info(String str){
		logger.info(str);
	}

	public static void warn(String str){
		logger.warn(str);
	}

	public static void error(String str){
		logger.error(str);
	}

	public static void fatal(String str){
		logger.fatal(str);
	}

	public static void log(String str, int level){
		Level v = Level.toLevel(level);
		logger.log(v, str);
	}

}
