package ece454p1;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class ClientWorker implements Runnable {

	private Socket client;
	private String hostname;
	private String port;
	private Queue<MessageTransport> messageQueue;
	private MessageListener listener;
	Thread t;
	private boolean isKilled;
	
	ClientWorker(String hostname, String port) {
		this.client = null;
		this.hostname = hostname;
		this.port = port;
		messageQueue = new LinkedList<MessageTransport>();
		t = new Thread(this);
		t.start();
		this.isKilled = false;
	}
	
	public boolean isConnected() {
		return client != null;
	}

	public void setListener(MessageListener listener) {
		this.listener = listener;
	}
	
	public Socket getsocket() {
		return client;
	}
	
	public synchronized void sendMessage(String message) {
		MessageTransport transport = new MessageTransport();
		transport.isFile = false;
		transport.text = message;
		messageQueue.add(transport);
		if (messageQueue.size() == 1) this.notify();
	}
	
	public synchronized void sendFile(String fullRelativePath, String filePath) {
		MessageTransport transport = new MessageTransport();
		transport.isHeader = FileUtils.isHeaderFile(new File(filePath)); 
		transport.isFile = true;
		transport.fullRelativePath = fullRelativePath;
		transport.filePath = filePath;
		messageQueue.add(transport);
		if (messageQueue.size() == 1) this.notify();
	}
	
	private synchronized MessageTransport getMessage() {
		try {
			if (messageQueue.isEmpty()) this.wait();
		} catch (InterruptedException e) {
		   // we been interrupt, kill this thread
			System.out.println("Got interrupted");
			return null;
		} catch (Exception e) {
			// do nothing
			return null;
		}
		return messageQueue.remove();
	}
	
	private static void writeFileToStream(File file, OutputStream os) {
		FileInputStream fstream = null;
		BufferedInputStream bis = null;
		byte[] bytearray = new byte[(int)file.length()];
		try {
			fstream = new FileInputStream(file);
			bis = new BufferedInputStream(fstream);
			bis.read(bytearray,0,bytearray.length);
			os.write(bytearray,0,bytearray.length);
			os.flush();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (fstream != null) fstream.close();
				if (bis != null) bis.close();
			} catch (IOException e) {				
				System.out.println(e);
			}
		}
	}
	
	public synchronized boolean isKilled() {
		return this.isKilled;
	}
	
	public synchronized void kill() {
		this.isKilled = true;
		try {
			this.t.interrupt();
		} catch (Exception e) {
			System.out.println("This thread cannot be stoped");
		}
	}
	
	@Override
	public void run() {
		
		while (true) {
			// connect to socket
			while (client == null) {
				try {
					client = new Socket(hostname, Integer.parseInt(port));
				} catch (Exception e) {
				}
			}
			if (listener != null) listener.onConnect();
			
			System.out.println("Client Connected");
			System.out.println("Localport: "+client.getLocalPort());
			System.out.println("Remoteport: "+client.getPort());
			
			// write
		    PrintWriter out = null;
		    BufferedReader in = null;
		    File file = null;
		    OutputStream os = null;
		    try{
		      out = new PrintWriter(client.getOutputStream(), true);
		      os = client.getOutputStream();
		      in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		    } catch (IOException e) {
		      System.out.println("in or out failed");
		    }
	
		    MessageTransport message = null;
		    String response = null;
		    while(true){
		      try{		    	  
		    	  // see if there is a message to send
		    	  message = getMessage();
		    	  if (isKilled()) {
		    		  // exit run loop
		    		  return;
		    	  }
		    	  
		    	  // check connection
		    	  out.println("checkconnection");
		    	  if (in.readLine() == null) {
		    		  // put message back in queue
		    		  // sendMessage(message.text);
		    		  // this client peer server is disconnected
		    		  break;
		    	  }
		    	  
		    	  if (!message.isFile) {
		    		  // a message
		    		  out.println(message.text);
		    	  } else if (message.isHeader) {
		    		  // a header file
		    		  if (message.filePath == null) continue;
		    		  file = new File(message.filePath);
		    		  if (!file.exists()) continue;
		    		  out.println("header101,"+(int)file.length()+","+message.fullRelativePath);
		    		  String abc = in.readLine();
		    		  if (abc.equals("ok101")) {
		    			  writeFileToStream(file, os);
		    		  } else {
		    			  System.out.println("Client don't want header file");
		    		  }
		    	  } else {
		    		  // a chunk file
		    		  if (message.filePath == null) continue;
		    		  file = new File(message.filePath);
		    		  if (!file.exists()) continue;
		    		  out.println("txt101,"+(int)file.length()+","+message.fullRelativePath);
		    		  String abc = in.readLine();
		    		  
		    		  if (abc.equals("no101")) {
		    			  // do nothing, since onMessageResponse will already notify peer manager to scan
		    		  } else if (abc.equals("ok101")) {
		    			  writeFileToStream(file, os);
		    		  }
		    	  }
		    	  
		    	  // get response
		    	  response = in.readLine();
		    	  if (listener != null) listener.onMessageResponse(response);
		       }catch (IOException e) {
		        System.out.println("Write failed");
		        System.out.println(e);
		       } catch (NullPointerException e) {
		    	   break;
		       }
		    }
		    
		    try {
				client.close();
			} catch (IOException e) {
				System.out.println("Closing Socket");
				System.out.println(e);
			}
		    // reconnect
		    listener.onDisconnected();
		    client = null;
		}
	}

}
