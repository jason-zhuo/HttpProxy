package Data;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Logger.LogHandler;
import Thread.Threadpool;

public class HttpProxy {
	static boolean islogging = true;
	static LogHandler log = new LogHandler(islogging);

	/**
	 * 描述： Start the httpproxy
	 * 
	 * @param int port number
	 */

	public static void startProxy(int port) {
		ServerSocket sSocket = null;
		Threadpool pool = Threadpool.getInstance();
		try {
			sSocket = new ServerSocket(port);
			log.logger.info("Http request transfer service start on Port: " + port);
			while (true) {
				Socket tmp = sSocket.accept();
				String SrcAddress= tmp.getInetAddress().toString().split("/")[1];
				log.logger.info("Incoming Address:" + SrcAddress
						+ ":" + tmp.getPort());
				pool.assign(new DataInterceptService(tmp));

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			pool.complete();
			if (sSocket != null) {
				try {
					sSocket.close();
				} catch (Exception e) {
					log.logger.error("Server socket close error!");
				}
			}

		}

	}
}
