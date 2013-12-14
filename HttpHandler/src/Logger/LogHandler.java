package Logger;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LogHandler {
   private boolean Islogging;
   public  Logger logger;
   public LogHandler(boolean Islogging){
	   this.Islogging=Islogging;
	   if(Islogging){
		   LoggerInit();		   
	   }
   }
   private void LoggerInit(){
	   if(Islogging){
		   logger= Logger.getLogger(this.getClass());
		   Properties prop = new Properties();
			prop.setProperty("log4j.rootLogger", "DEBUG, CONSOLE");
			prop.setProperty("log4j.appender.CONSOLE", "org.apache.log4j.ConsoleAppender");
			prop.setProperty("log4j.appender.CONSOLE.layout", "org.apache.log4j.PatternLayout");
			prop.setProperty("log4j.appender.CONSOLE.layout.ConversionPattern", "%d{yyyy-MM-dd_HH:mm:ss,SSS} [%t] %-5p %C{1} : %m%n");
			PropertyConfigurator.configure(prop);
	   }
   }
}
