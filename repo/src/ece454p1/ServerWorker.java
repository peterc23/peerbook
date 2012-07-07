package ece454p1;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ServerWorker implements Runnable{
	private Socket client;
	private ServerPeer peer;
	Thread t;
	//private boolean isWorking;
	
	ServerWorker(Socket client, ServerPeer peer, boolean work) {
		this.client = client;
		this.peer = peer;
		//this.isWorking = work;
		t = new Thread(this);
		t.start();
	}
	
	public Socket getSocket() {
		return client;
	}
	
	public synchronized void stop() {
		//isWorking = false;
		this.t.interrupt();
	}
	
	/*
	public synchronized void start() {
		t.start();
		//if (!isWorking) {
			//this.notify();
			//isWorking = true;
		//}
	}
	*/
	
	/*
	private synchronized void getConnection() {
		if (!isWorking) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}
	}*/
	
	@Override
	public void run() {
		// attempt to connect
		BufferedReader in = null;
		PrintWriter out = null;
		InputStream is = null;
		File file = null;
		try{
			is = client.getInputStream();
			out = new  PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(is));
	    } catch (IOException e) {
	    	System.out.println("in or out failed");
	    }

	    String message = null;
	    String response = null;
	    while(true){
	      try{
	        
	    	  // see if we are connected
	    	  // getConnection();
	    	  if (Thread.interrupted()) {
	    		  try {
		    		  if (is != null) is.close();
		    		  if (out != null) out.close();
		    		  if (in != null) in.close();
	    		  } catch (Exception e) {
	    			  // close all connections
	    		  }
	    		  return;
	    	  }
	    	  
	    	  // see if there is a message to send
	    	  message = in.readLine();
	    	  if (message == null) {
	    		  peer.removeWorker(client);
	    		  client.close();
	    		  break;
	    	  } else if (message.equals("checkconnection")) {
	    		  out.println("IamConnected");
	    	  } else if (message.matches("header101,[\\d]+,[a-zA-Z.\\d-_]+")) {
	    		  String [] messageArray = message.split(",");
	    		  out.println("ok101");
	    		  // download header file and clean
	    		  file = FileUtils.downloadAndCleanHeaderFile(is, Integer.parseInt(messageArray[1]), messageArray[2]);
	    		  // handle request and get response
	    		  response = peer.onClientRequest(null, file);
	    		  out.println(response);
	    	  } else if (message.matches("txt101,[\\d]+,[a-zA-Z.\\d-_]+")) {
	    		  String[] messagearray = message.split(",");
	    		  out.println("ok101");
	    		  ChunkInfo info = PeerManager.getChunkInfoFromFileName(messagearray[2]);
	    		  if (PeerManager.getManager().isReceivingChunk(info.headId, info.chunk)) {
	    			  out.print("no101");
	    		  } else {
		    		  PeerManager.getManager().receiveChunk(info.headId, info.chunk);
	    		  	  // save file
		    		  file = FileUtils.saveFileFromByte(is, Integer.parseInt(messagearray[1]), messagearray[2]);
		    		  FileUtils.updateChunkToHeaderFile(file);
		    		  // handle request and get response
	    		  	  response = peer.onClientRequest(null, file);
		    		  PeerManager.getManager().finishChunk(info.headId, info.chunk);
			    	  out.println(response);
	    		  }
	    	  } else {
		    	  // handle request and get response
		    	  response = peer.onClientRequest(message, null);
		    	  out.println(response);
	    	  }
	    	  
	       }catch (Exception e) {
	    	System.out.println(e);
	        System.out.println("Write failed");
	       }
	    }
	}
}
