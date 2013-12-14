package Entry;
import Data.HttpProxy;
import Logger.LogHandler;
  

public class Main {
	
	static int port;
	static boolean islogging = true;
	static LogHandler log = new LogHandler(islogging);
	public Main(){
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		log.logger.info("Loading Config File....");
		ConfigLoader.LoadConfig();
		port=ConfigLoader.get_ServicePortNum();
		log.logger.info("Loading Complete! Starting Service...");
		HttpProxy.startProxy(port);
	}

}
