package Entry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import Logger.LogHandler;

public class ConfigLoader {
	private static int _BUFSIZ;
	private static int _THREADSIZE;
	private static int _ServicePortNum;
	private static String _IPAddress;
	private static boolean _islogging;
	private static int _Req_Server_Port;
	static boolean islogging = true;
	static LogHandler log = new LogHandler(islogging);
	public static void LoadConfig() {
		File conf = new File("Config");
		try {
			FileReader frd = new FileReader(conf);
			BufferedReader bfrd = new BufferedReader(frd);
			String temp;
			while ((temp = bfrd.readLine()) != null) {
				if (temp.contains("BUFSIZ")) {
					_BUFSIZ = Integer.parseInt(temp.split(" = ")[1]);
				} else if (temp.contains("THREADSIZE")) {
					_THREADSIZE = Integer.parseInt(temp.split(" = ")[1]);
				} else if (temp.contains("ServicePortNum")) {
					_ServicePortNum = Integer.parseInt(temp.split(" = ")[1]);
				} else if (temp.contains("Req_Server_Port")) {
					_Req_Server_Port = Integer.parseInt(temp.split(" = ")[1]);
				} else if (temp.contains("IPAddress")) {
					_IPAddress = temp.split(" = ")[1];
				} else if (temp.contains("islogging")) {
					String value = temp.split(" = ")[1];
					if (value.equalsIgnoreCase("true")) {
						_islogging = true;
					} else {
						_islogging = false;
					}
				}
			}
			bfrd.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			if (islogging)
				log.logger.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if (islogging)
				log.logger.error(e.getMessage());
		}

	}

	public static int get_Req_Server_Port() {
		return _Req_Server_Port;
	}

	public static int get_BUFSIZ() {
		return _BUFSIZ;
	}

	public static int get_THREADSIZE() {
		return _THREADSIZE;
	}

	public static int get_ServicePortNum() {
		return _ServicePortNum;
	}

	public static String get_IPAddress() {
		return _IPAddress;
	}

	public static boolean get_islogging() {
		return _islogging;
	}

}
