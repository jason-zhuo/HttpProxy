package Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import Entry.ConfigLoader;
import Logger.LogHandler;

public class DataInterceptService implements Runnable {
	int CONNECT_PAUSE = 5;
	int TIMEOUT = 60*1000;
	int BUFSIZ = ConfigLoader.get_BUFSIZ();
	String host = "";
	String port = "";
	static String req_handler_IPaddr= ConfigLoader.get_IPAddress();
	static int req_handler_Port= ConfigLoader.get_Req_Server_Port();
	static boolean islogging = ConfigLoader.get_islogging();
	static LogHandler log = new LogHandler(islogging);
	Socket listeningsocket;

	public DataInterceptService(Socket s) {
		listeningsocket = s;
	}

	void pipe(InputStream is0, InputStream is1, OutputStream os0,
			OutputStream os1) throws IOException {
		try {
			int ir;
			byte bytes[] = new byte[BUFSIZ];
			while (true) {
				try {
					if ((ir = is0.read(bytes)) > 0) {
						os0.write(bytes, 0, ir);
					//	if (islogging)
					//		writeLog(bytes, 0, ir, true);
					} else if (ir < 0)
						break;
				} catch (InterruptedIOException e) {
				}
				try {
					if ((ir = is1.read(bytes)) > 0) {
						os1.write(bytes, 0, ir);
					//	if (islogging)
						//	writeLog(bytes, 0, ir, false);
					} else if (ir < 0)
						break;
				} catch (InterruptedIOException e) {
				}
			}
		} catch (Exception e0) {
			System.out.println("Pipe exception: " + e0);
		}
	}

	/**
	 * 描述： Parse and modify the request from Client
	 * 
	 * @param BufferedReader
	 *            Request line Reader
	 * @return String of the modified request
	 */
	public String modifyRequest(BufferedReader readerIn) {
		boolean done = false;
		StringBuilder requestBuilder = new StringBuilder();
		String line = "";
		while (!done) {
			try {
				line = readerIn.readLine();
				if (line.equals("")) {
					done = true;
				} else if (line.contains("Accept-Encoding:")) {
					continue;
					// line = "";
				} else if (line.contains("Host:")) {
					requestBuilder.append(line);
					requestBuilder.append("\r\n");
					host = line.split(" ")[1];
					if (islogging)
						log.logger.info("Host is :" + host);
//					if (host.contains(":")) {
//						port = host.split(":")[1];
//						if (islogging)
//							log.logger.info("The url contains special port number!");
//					}
				} else {
					requestBuilder.append(line);
					requestBuilder.append("\r\n");
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				if (islogging)
					log.logger.error("ModifyRequest Unhandled IO Exception! "+ e.getMessage());
			}

		}
		return requestBuilder.toString();
	}

	public void pipe(InputStream inputs, OutputStream outputs){
		try {
			int ir;
			byte [] buffer = new byte[5*1024];
			while ((ir=inputs.read(buffer))>0) 
				outputs.write(buffer,0,ir);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.logger.error("Pipe Error: "+e.getMessage());
		}
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Socket outbound = null;
		//int retrytimes = CONNECT_RETRIES;
		try {
			listeningsocket.setSoTimeout(TIMEOUT);
			listeningsocket.setKeepAlive(true);
			// InputStream is = listeningsocket.getInputStream();
			
			PrintWriter out;
			BufferedReader readerIn = new BufferedReader(new InputStreamReader(
					listeningsocket.getInputStream()));
			String request = modifyRequest(readerIn);
			//log.logger.info(request);
			outbound = new Socket(req_handler_IPaddr,req_handler_Port);
			outbound.setKeepAlive(true);
			outbound.setSoTimeout(TIMEOUT);  
			
			out = new PrintWriter(outbound.getOutputStream(),true);
			out.println(request);
			
		    
			InputStream Inputs  = outbound.getInputStream();
			
			OutputStream Outputs = listeningsocket.getOutputStream();

			pipe(Inputs,Outputs);
			//Inputs.close();
			//Outputs.close();
			readerIn.close();
			out.close();
			outbound.close();
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			if (islogging)
				log.logger.error("Socket exception! Probably the reciver is not working! "+ e.getMessage());
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if (islogging)
				log.logger.error("Unhandled IO exception!");
			// e.printStackTrace();
		}

	}
}
