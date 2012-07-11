package ece454p1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.ArrayList;

public class HttpRequest {
	
	public static String postServer(String path, Object data) {
		if (data == null) {
			return post(Config.SERVER_HOST, Config.SERVER_PORT, path, null);
		} else {
			return post(Config.SERVER_HOST, Config.SERVER_PORT, path, FileUtils.mapToJSON(data));
		}
	}
	
	public static String postServer(String path, String data) {
		return post(Config.SERVER_HOST, Config.SERVER_PORT, path, data);
	}
	// sample inputs
	// hostname = "hostname.com"
	// port = 80
	// path = "/servlet/SomeServlet"
	public static String post(String hostname, int port, String path, String data) {
		
	    StringBuilder response = new StringBuilder();
	    
	    if (data == null) {
	    	data = "";
	    }
	    
		try {
			// Create a socket to the host
		    InetAddress addr = InetAddress.getByName(hostname);
		    Socket socket = new Socket(addr, port);

		    // Send header
		    BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
		    wr.write("POST "+path+" HTTP/1.0\r\n");
		    wr.write("Content-Length: "+data.length()+"\r\n");
		    wr.write("Content-Type: application/json\r\n");
		    wr.write("\r\n");

		    // Send data
		    wr.write(data);
		    wr.flush();

		    // Get response
		    BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    String line;
		    boolean flag = false;
		    while ((line = rd.readLine()) != null) {
		        if (line.startsWith("Connection:")) {
		        	line = rd.readLine();
		        	flag = true;
		        } else if (flag) {
		        	response.append(line);
		        }
		    }
		    wr.close();
		    rd.close();
		} catch (Exception e) {
		}
		
		return response.toString();
	}
	
	/*
	public static void main(String[] args) {
		// delete the chunks and update header file
		HeaderFile torrent = FileUtils.readHeaderFile(new File(Config.SHARE_PATH+"f8854.pdf"+Config.HEADER_FILE_EXT));
		FileUtils.deleteChuckFiles("/f8854.pdf");
		ArrayList<File> files = FileUtils.divideFiles(new File(Config.SHARE_PATH+"/f8854.pdf"));
		files.remove(0);
		FileUtils.updateHeaderFileForWrite("/f8854.pdf", torrent, files);
		return ;
	}
	*/
}
